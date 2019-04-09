//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.atp.service;

import javax.inject.Inject;

import com.ford.purchasing.wips.common.atp.AtpRequest;
import com.ford.purchasing.wips.common.layer.WipsBaseResponse;
import com.ford.purchasing.wips.domain.connector.Ims;

public class AtpApproveOrRejectAS {

    @Inject
    private Ims ims;

    public WipsBaseResponse approveOrRejectAtp(final AtpRequest atpRecapRequest) {
        return this.ims.converse(atpRecapRequest.getLterm(),
            new ApproveOrRejectAtp(atpRecapRequest));
    }

}
