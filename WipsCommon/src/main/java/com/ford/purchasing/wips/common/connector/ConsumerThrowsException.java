//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.connector;

public interface ConsumerThrowsException<T, E extends Exception> {
    public void accept(T item) throws E;
}
