//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.connector;

public interface BiFunctionThrowsException<T, U, R, E extends Exception> {
    public R apply(T t, U u) throws E;
}
