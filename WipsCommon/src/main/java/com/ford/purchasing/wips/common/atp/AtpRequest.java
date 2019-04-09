//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.atp;

import com.ford.purchasing.wips.common.layer.PendingApprovalRequest;

public class AtpRequest extends PendingApprovalRequest {

    private String atpNumber;

    public String getAtpNumber() {
        return this.atpNumber;
    }

    public void setAtpNumber(final String atpNumber) {
        this.atpNumber = atpNumber;
    }

}
