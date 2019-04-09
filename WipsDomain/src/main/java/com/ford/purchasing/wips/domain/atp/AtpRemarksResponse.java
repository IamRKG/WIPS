//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.atp;

import com.ford.purchasing.wips.common.layer.WipsBaseResponse;
import com.ford.purchasing.wips.domain.atp.beans.AtpRemarksOutput;

public class AtpRemarksResponse extends WipsBaseResponse {

    private AtpRemarksOutput remarks;

    public AtpRemarksOutput getRemarks() {
        return this.remarks;
    }

    public void setRemarks(final AtpRemarksOutput remarks) {
        this.remarks = remarks;
    }

}
