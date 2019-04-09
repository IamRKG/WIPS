//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.layer;

import java.util.Arrays;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ford.purchasing.wips.common.layer.util.WipsUtil;

@SuppressWarnings("javadoc")
public class WipsBaseResponse {
    Status status = Response.Status.OK;
    private boolean errorFlag = false;
    private String errorMessage;
    private String ltermToken;
    private String userRacfId;

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getUserRacfId() {
        return this.userRacfId;
    }

    public void setUserRacfId(final String userRacfID) {
        this.userRacfId = userRacfID;
    }

    public boolean isErrorFlag() {
        return this.errorFlag;
    }

    public void setErrorFlag(final boolean errorFlag) {
        this.errorFlag = errorFlag;
    }

    public String getLtermToken() {
        return this.ltermToken;
    }

    public void setLtermToken(final String ltermToken) {
        this.ltermToken = ltermToken;
    }

    public void populateErrorDetails(final String errorMessage) {
        this.setErrorMessage(errorMessage);
        this.setErrorFlag(true);
        this.setStatus(populateStatusCode(errorMessage));
    }

    private Status populateStatusCode(final String errorMessage) {
        if (WipsUtil.isExceptionContainingMessageCode(errorMessage,
                Arrays.asList(WipsConstant.getNoWorklistFoundCode()))
                || errorMessage.contains(WipsConstant.LUMPSUM_CURRENTLY_IN_USE)
                || errorMessage.contains(WipsConstant.CLAIM_CURRENTLY_IN_USE)
                || errorMessage.contains(WipsConstant.MORE_THAN_18_MONTHS)
                || errorMessage.contains(WipsConstant.NO_PRICE_CLAIMS))
            return Response.Status.CONFLICT;
        return Response.Status.ACCEPTED;
    }

}
