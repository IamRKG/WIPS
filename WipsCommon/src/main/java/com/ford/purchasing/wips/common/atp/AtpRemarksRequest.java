//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.atp;

public class AtpRemarksRequest extends AtpRequest {
    private String[] userRemarks;

    public String[] getUserRemarks() {
        return this.userRemarks;
    }

    public void setUserRemarks(final String[] userRemarks) {
        this.userRemarks = userRemarks;
    }

}
