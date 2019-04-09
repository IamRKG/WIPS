//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.connector;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.ConnectorFactory;
import com.ford.it.connector.ims.ImsConversation;
import com.ford.it.connector.ims.ImsOperation;
import com.ford.it.connector.record.DataArea;
import com.ford.it.logging.ILogger;
import com.ford.it.logging.LogFactory;
import com.ford.it.properties.PropertyManager;
import com.ford.purchasing.wips.common.connector.BiConsumerThrowsException;
import com.ford.purchasing.wips.common.connector.BiFunctionThrowsException;
import com.ford.purchasing.wips.common.connector.FunctionThrowsException;
import com.ford.purchasing.wips.common.connector.Predicate;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.exception.WipsImsTransactionException;

public class ImsConversationCtx {

    private final ImsConversation imsConversation;
    private final String lterm;
    private static final ILogger log = LogFactory.getInstance().getLogger("ImsConversationCtx");

    public ImsConversationCtx(final ImsConversation imsConversation, final String lterm) {
        this.imsConversation = imsConversation;
        this.lterm = lterm;
    }

    public ImsConversationCtx(final String lterm) {
        this(new ImsConversation(), lterm);
    }

    // Factory for producing transactions
    public <T, R> ImsTransaction<R> transact(final String txnName,
        final T value,
        final BiConsumerThrowsException<T, DataArea, ConnectorException> input,
        final BiFunctionThrowsException<DataArea, String, R, Exception> output) {
        try {
            final ImsOperation imsOperation = createImsOperation(txnName);
            imsOperation.setConversation(this.imsConversation);
            imsOperation.overrideCredentials(getWipsProxyId(),
                getWipsProxyPassword());
            imsOperation.overrideLtermName(this.lterm);
            final DataArea inputDataArea = (DataArea)imsOperation.getInputRecord();
            input.accept(value, inputDataArea);
            imsOperation.execute();
            final String rawOutput = imsOperation.getRawOutput();
            log.info("Raw Output" + rawOutput);
            final DataArea outputRecord = (DataArea)imsOperation.getOutputRecord();
            return new Success<R>(output.apply(outputRecord, rawOutput), this);
        } catch (final WipsImsTransactionException wipsTransactionException) {
            return new Failure<R>(wipsTransactionException, this);
        } catch (final Exception e) {
            log.throwing("", "", e);
            return new Failure<R>(new Exception(
                "Unable to process currently. Please contact your system administrator",
                e), this);
        }
    }

    ImsOperation createImsOperation(final String imsTransaction)
        throws ConnectorException {
        return (ImsOperation)ConnectorFactory.getInstance()
            .getOperation(imsTransaction);
    }

    String getWipsProxyId() {
        return PropertyManager.getInstance().getString(WipsConstant.WIPS_PROXY_USERNAME);
    }

    String getWipsProxyPassword() {
        return PropertyManager.getInstance().getString(WipsConstant.WIPS_PROXY_PWD);
    }

    public void endConversation() throws ConnectorException {
        this.imsConversation.endConversation();
    }

    public static class Success<T> implements ImsTransaction<T> {
        private T value;
        private final ImsConversationCtx ctx;

        private Success(final T value, final ImsConversationCtx ctx) {
            this.value = value;
            this.ctx = ctx;
        }

        @Override
        public <R> ImsTransaction<R> thenTransact(final String txnName,
            final BiConsumerThrowsException<T, DataArea, ConnectorException> input,
            final BiFunctionThrowsException<DataArea, String, R, Exception> output) {
            return this.ctx.transact(txnName, (T)this.value, input, output);
        }

        @Override
        public T get() {
            return this.value;
        }

        @Override
        public <R, E extends Exception> ImsTransaction<R> map(
            final FunctionThrowsException<T, R, E> mapper) {
            try {
                return new Success<R>(mapper.apply(this.value), this.ctx);
            } catch (final Exception e) {
                log.throwing("", "", e);
                return new Failure(e, this.ctx);
            }
        }

        @Override
        public <R> ImsTransaction<R> thenTransactIf(final Predicate<T> predicate,
            final String txnName,
            final BiConsumerThrowsException<T, DataArea, ConnectorException> input,
            final BiFunctionThrowsException<DataArea, String, R, Exception> output) {
            if (predicate.test(get())) {
                return thenTransact(txnName, input, output);
            }
            return (ImsTransaction<R>)this;
        }

        @Override
        public <R> ImsTransaction<R> thenTransactWhile(final Predicate<T> predicate,
            final String txnName,
            final BiConsumerThrowsException<T, DataArea, ConnectorException> input,
            final BiFunctionThrowsException<DataArea, String, R, Exception> output) {
            while (predicate.test(this.value)) {
                final ImsTransaction<R> success = thenTransact(txnName, input, output);
                this.value = (T)success.get();
            }
            return (ImsTransaction<R>)this;
        }

        @Override
        public <E extends Exception> T orElse(final Function<E, T> mapper) {
            return this.value;
        }

        @Override
        public T orElse(final T defaultValue) {
            return this.value;
        }

        @Override
        public <R, E extends Exception> ImsTransaction<R> orElseTransact(final String txnName,
            final FunctionThrowsException<E, T, E> fn,
            final BiConsumerThrowsException<T, DataArea, ConnectorException> input,
            final BiFunctionThrowsException<DataArea, String, R, Exception> output) {
            return (ImsTransaction<R>)this;
        }

        @Override
        public <R> ImsTransaction<R> thenNestTransactionsIf(final Predicate<T> predicate,
            final Function<ImsTransaction<T>, ImsTransaction<R>> mapper) {
            if (predicate.test(get())) {
                return mapper.apply(this);
            }
            return (ImsTransaction<R>)this;
        }

        @Override
        public <R> ImsTransaction<R> thenNestTransactionsIfElse(final Predicate<T> predicate,
            final Function<ImsTransaction<T>, ImsTransaction<R>> mapperIf,
            final Function<ImsTransaction<T>, ImsTransaction<R>> mapperElse) {
            if (predicate.test(get())) {
                return mapperIf.apply(this);
            } else {
                return mapperElse.apply(this);
            }
        }

        @Override
        public <R> ImsTransaction<R> thenNestTransactWhile(final Predicate<T> predicate,
            final Function<ImsTransaction<T>, ImsTransaction<R>> mapper) {
            while (predicate.test(get())) {
                final ImsTransaction<R> success = mapper.apply(this);
                this.value = (T)success.get();
            }
            return (ImsTransaction<R>)this;
        }

        @Override
        public <R> ImsTransaction<R> thenDoTransactWhile(final Predicate<T> predicate, final String txnName,
            final BiConsumerThrowsException<T, DataArea, ConnectorException> input,
            final BiFunctionThrowsException<DataArea, String, R, Exception> output) {
            do {
                final ImsTransaction<R> success = thenTransact(txnName, input, output);
                this.value = (T)success.get();
            } while (predicate.test(this.value));
            return (ImsTransaction<R>)this;
        }

        @Override
        public <R> ImsTransaction<R> thenDoNestTransactWhile(final Predicate<T> predicate,
            final Function<ImsTransaction<T>, ImsTransaction<R>> mapper) {
            do {
                final ImsTransaction<R> success = mapper.apply(this);
                this.value = (T)success.get();
            } while (predicate.test(get()));
            return (ImsTransaction<R>)this;
        }
    }

    static class Failure<T> implements ImsTransaction<T> {
        private final Exception exception;
        private final ImsConversationCtx ctx;

        private Failure(final Exception exception, final ImsConversationCtx ctx) {
            this.exception = exception;
            this.ctx = ctx;
        }

        @Override
        public <R> ImsTransaction<R> thenTransact(final String txnName,
            final BiConsumerThrowsException<T, DataArea, ConnectorException> input,
            final BiFunctionThrowsException<DataArea, String, R, Exception> output) {
            return (ImsTransaction<R>)this;
        }

        @Override
        public T get() {
            throw new RuntimeException(this.exception);
        }

        @Override
        public <R, E extends Exception> ImsTransaction<R> map(
            final FunctionThrowsException<T, R, E> mapper) {
            return (ImsTransaction<R>)this;
        }

        @Override
        public <R> ImsTransaction<R> thenTransactIf(final Predicate<T> predicate,
            final String txnName,
            final BiConsumerThrowsException<T, DataArea, ConnectorException> input,
            final BiFunctionThrowsException<DataArea, String, R, Exception> output) {
            return (ImsTransaction<R>)this;
        }

        @Override
        public <R> ImsTransaction<R> thenTransactWhile(final Predicate<T> predicate,
            final String txnName,
            final BiConsumerThrowsException<T, DataArea, ConnectorException> input,
            final BiFunctionThrowsException<DataArea, String, R, Exception> output) {
            return (ImsTransaction<R>)this;
        }

        @Override
        public <E extends Exception> T orElse(final Function<E, T> mapper) {
            return mapper.apply((E)this.exception);
        }

        @Override
        public T orElse(final T defaultValue) {
            return defaultValue;
        }

        @Override
        public <R, E extends Exception> ImsTransaction<R> orElseTransact(final String txnName,
            final FunctionThrowsException<E, T, E> fn,
            final BiConsumerThrowsException<T, DataArea, ConnectorException> input,
            final BiFunctionThrowsException<DataArea, String, R, Exception> output) {
            try {
                final T value = fn.apply((E)this.exception);
                return this.ctx.transact(txnName, value, input, output);
            } catch (final Exception e) {
                log.throwing("", "", e);
                return new Failure(e, this.ctx);
            }
        }

        @Override
        public <R> ImsTransaction<R> thenNestTransactionsIf(final Predicate<T> predicate,
            final Function<ImsTransaction<T>, ImsTransaction<R>> mapper) {
            return (ImsTransaction<R>)this;
        }

        @Override
        public <R> ImsTransaction<R> thenNestTransactionsIfElse(final Predicate<T> predicate,
            final Function<ImsTransaction<T>, ImsTransaction<R>> mapperIf,
            final Function<ImsTransaction<T>, ImsTransaction<R>> mapperElse) {
            return (ImsTransaction<R>)this;
        }

        @Override
        public <R> ImsTransaction<R> thenNestTransactWhile(final Predicate<T> predicate,
            final Function<ImsTransaction<T>, ImsTransaction<R>> mapper) {
            return (ImsTransaction<R>)this;
        }

        @Override
        public <R> ImsTransaction<R> thenDoTransactWhile(final Predicate<T> predicate, final String txnName,
            final BiConsumerThrowsException<T, DataArea, ConnectorException> input,
            final BiFunctionThrowsException<DataArea, String, R, Exception> output) {
            return (ImsTransaction<R>)this;
        }

        @Override
        public <R> ImsTransaction<R> thenDoNestTransactWhile(final Predicate<T> predicate,
            final Function<ImsTransaction<T>, ImsTransaction<R>> mapper) {
            return (ImsTransaction<R>)this;
        }

    }
}
