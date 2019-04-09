//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.atp.service;

import javax.inject.Inject;

import com.ford.purchasing.wips.common.atp.AtpApproveOrRejectResponse;
import com.ford.purchasing.wips.common.atp.AtpRemarksRequest;
import com.ford.purchasing.wips.common.atp.AtpRequest;
import com.ford.purchasing.wips.common.layer.WipsBaseResponse;
import com.ford.purchasing.wips.domain.atp.AtpRemarksResponse;
import com.ford.purchasing.wips.domain.connector.Ims;

@SuppressWarnings("javadoc")
public class AtpRemarksAS {

    @Inject
    private Ims ims;

    public WipsBaseResponse getRemarks(final AtpRequest request) {
        return this.ims.converse(request.getLterm(), new RetrieveAtpRemarks(request));
    }

    public AtpRemarksResponse saveRemarks(final AtpRemarksRequest request) {
        return this.ims.converse(request.getLterm(), new SaveAtpRemarks(request));
    }

    public AtpApproveOrRejectResponse saveRemarksAndApproveOrRejectAtp(
        final AtpRemarksRequest request) {
        return this.ims.converse(request.getLterm(), new SaveRemarksAndApproveOrReject(request));
    }

}
