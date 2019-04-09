//****************************************************************
//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
package com.ford.purchasing.wips.domain.connector;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.ConnectorFactory;
import com.ford.it.connector.ims.ImsConversation;
import com.ford.it.connector.ims.ImsOperation;
import com.ford.it.connector.record.DataArea;
import com.ford.it.logging.ILogger;
import com.ford.it.logging.LogFactory;
import com.ford.purchasing.wips.common.connector.BiFunctionThrowsException;
import com.ford.purchasing.wips.common.connector.ConsumerThrowsException;
import com.ford.purchasing.wips.common.layer.WipsBaseResponse;
import com.ford.purchasing.wips.common.layer.exception.WipsImsTransactionException;

public class Ims {
    private static final String CLASS_NAME = Ims.class.getName();
    private static ILogger log = LogFactory.getInstance().getLogger(CLASS_NAME);
    private ImsConversationCtx ctx;

    public <T extends WipsBaseResponse> T converse(final String lterm,
            final Function<ImsConversationCtx, ImsTransaction<T>> transactionChain,
            final Function<Exception, T> failureResponse) {
        final String methodName = "converse";
        try {
            this.ctx = new ImsConversationCtx(lterm);
            final ImsTransaction<T> transaction = transactionChain.apply(this.ctx);
            return transaction.orElse(failureResponse);
        } finally {
            try {
                this.ctx.endConversation();
            } catch (final ConnectorException connectorException) {
                log.throwing(CLASS_NAME, methodName, connectorException);
            }
        }
    }

    public <T extends WipsBaseResponse> T converse(final String lterm,
            final Function<ImsConversationCtx, ImsTransaction<T>> fn) {
        final Function<Exception, T> defaultResponder = new Function<Exception, T>() {

            @Override
            public T apply(final Exception t) {
                final T defaultResponse = (T) new WipsBaseResponse();
                if (t instanceof WipsImsTransactionException) {
                    defaultResponse.populateErrorDetails(((WipsImsTransactionException) t).getErrorMessage());
                } else {
                    defaultResponse.populateErrorDetails(
                            "Unable to process currently. Please contact your system administrator");
                }
                return defaultResponse;
            }

        };
        return this.converse(lterm, fn, defaultResponder);
    }

    public <R> R login(final String txnName, final String userRacfId, final String pwd,
            final ConsumerThrowsException<DataArea, ConnectorException> input,
            final BiFunctionThrowsException<DataArea, String, R, Exception> output) throws Exception {
        final ImsConversation imsConversation = new ImsConversation();
        final String methodName = "login";
        try {
            final ImsOperation imsOperation = createImsOperation(txnName);
            imsOperation.setConversation(imsConversation);
            imsOperation.overrideCredentials(userRacfId, pwd);
            final DataArea inputDataArea = (DataArea) imsOperation.getInputRecord();
            input.accept(inputDataArea);
            imsOperation.execute();
            final String rawOutput = imsOperation.getRawOutput();
            log.info("Raw Output" + rawOutput);
            final DataArea outputRecord = (DataArea) imsOperation.getOutputRecord();
            return output.apply(outputRecord, rawOutput);
        } finally {
            try {
                imsConversation.endConversation();
            } catch (final ConnectorException connectorException) {
                log.throwing(CLASS_NAME, methodName, connectorException);
            }
        }
    }

    ImsOperation createImsOperation(final String imsTransaction) throws ConnectorException {
        return (ImsOperation) ConnectorFactory.getInstance().getOperation(imsTransaction);
    }

}
