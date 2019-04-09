//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.connector;

/**
 * Place class description here.
 */
public interface Function<T, R> {
    public R apply(T t);
}
