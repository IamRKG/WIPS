//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.connector;

/**
 * Place class description here.
 */
public interface FunctionThrowsException<T, R, E extends Exception> {
    public R apply(T t) throws E;
}


