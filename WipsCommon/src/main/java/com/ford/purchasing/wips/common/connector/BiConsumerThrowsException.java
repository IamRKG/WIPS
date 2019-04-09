//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.connector;

/**
 * Place class description here.
 */
public interface BiConsumerThrowsException<T, U, E extends Exception> {
    public void accept(T t, U u) throws E;
}
