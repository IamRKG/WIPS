//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.layer;

@SuppressWarnings("javadoc")
public class WipsImsOutput {

    private boolean errorFlag = false;

    private String errorMessage;

    private String programName;

    private String transactionName;

    public boolean isErrorFlag() {
        return this.errorFlag;
    }

    public void setTransactionName(final String transactionName) {
        this.transactionName = transactionName;
    }

    public String getTransactionName() {
        return this.transactionName;
    }

    public void setErrorFlag(final boolean errorFlag) {
        this.errorFlag = errorFlag;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getProgramName() {
        return this.programName;
    }

    public void setProgramName(final String programName) {
        this.programName = programName;
    }

}
