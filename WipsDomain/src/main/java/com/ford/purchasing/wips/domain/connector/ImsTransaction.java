//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.connector;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.connector.BiConsumerThrowsException;
import com.ford.purchasing.wips.common.connector.BiFunctionThrowsException;
import com.ford.purchasing.wips.common.connector.FunctionThrowsException;
import com.ford.purchasing.wips.common.connector.Predicate;

@SuppressWarnings("javadoc")
public interface ImsTransaction<T> {

    // Non-Terminal Operations
    public abstract <R> ImsTransaction<R> thenTransact(final String txnName,
            final BiConsumerThrowsException<T, DataArea, ConnectorException> input,
            final BiFunctionThrowsException<DataArea, String, R, Exception> output);

    public abstract <R, E extends Exception> ImsTransaction<R> map(
            FunctionThrowsException<T, R, E> mapper);

    public abstract <R> ImsTransaction<R> thenTransactIf(Predicate<T> predicate,
            final String txnName,
            final BiConsumerThrowsException<T, DataArea, ConnectorException> input,
            final BiFunctionThrowsException<DataArea, String, R, Exception> output);

    public <R> ImsTransaction<R> thenNestTransactionsIf(Predicate<T> predicate,
            Function<ImsTransaction<T>, ImsTransaction<R>> mapper);

    public <R> ImsTransaction<R> thenNestTransactionsIfElse(Predicate<T> predicate,
            Function<ImsTransaction<T>, ImsTransaction<R>> mapperIf,
            Function<ImsTransaction<T>, ImsTransaction<R>> mapperElse);

    public <R, E extends Exception> ImsTransaction<R> orElseTransact(String txnName,
            FunctionThrowsException<E, T, E> fn,
            BiConsumerThrowsException<T, DataArea, ConnectorException> input,
            BiFunctionThrowsException<DataArea, String, R, Exception> output);

    public abstract <R> ImsTransaction<R> thenTransactWhile(Predicate<T> predicate,
            final String txnName,
            final BiConsumerThrowsException<T, DataArea, ConnectorException> input,
            final BiFunctionThrowsException<DataArea, String, R, Exception> output);

    public abstract <R> ImsTransaction<R> thenDoTransactWhile(Predicate<T> predicate,
        final String txnName,
        final BiConsumerThrowsException<T, DataArea, ConnectorException> input,
        final BiFunctionThrowsException<DataArea, String, R, Exception> output);

    public abstract <R> ImsTransaction<R> thenNestTransactWhile(Predicate<T> predicate,
        Function<ImsTransaction<T>, ImsTransaction<R>> mapper);

    public abstract <R> ImsTransaction<R> thenDoNestTransactWhile(Predicate<T> predicate,
        Function<ImsTransaction<T>, ImsTransaction<R>> mapper);

    // Terminal Operations
    public abstract <E extends Exception> T orElse(Function<E, T> mapper);

    public abstract T get();

    public abstract T orElse(T defaultValue);

}
