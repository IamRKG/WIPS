//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.layer.exception;

@SuppressWarnings("javadoc")
public class WipsImsTransactionException extends Exception {

    private static final long serialVersionUID = 1L;
    private final String errorMessage;
    private final String transactionCode;

    public WipsImsTransactionException(final String transactionCode,
            final String errorMessage) {
        this.transactionCode = transactionCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public String getTransactionCode() {
        return this.transactionCode;
    }

}
