//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.atp;

public class ApproveWarningHandler {

    private String messageCode;
    private String errorMessage;
    private String actionTaken;

    public String getMessageCode() {
        return this.messageCode;
    }

    public void setMessageCode(final String messageCode) {
        this.messageCode = messageCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getActionTaken() {
        return this.actionTaken;
    }

    public void setActionTaken(final String actionTaken) {
        this.actionTaken = actionTaken;
    }

}
