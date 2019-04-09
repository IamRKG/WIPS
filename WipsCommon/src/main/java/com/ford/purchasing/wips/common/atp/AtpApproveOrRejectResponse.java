//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.atp;

import java.util.List;

import com.ford.purchasing.wips.common.layer.WipsBaseResponse;
import com.ford.purchasing.wips.common.layer.WipsPendingApprovalOutput;

public class AtpApproveOrRejectResponse extends WipsBaseResponse {
    private String approveOrRejectMessage;
    private boolean actionAlreadyTakenFlag;
    private List<WipsPendingApprovalOutput> atp = null;
    private List<ApproveWarningHandler> warningMessagesList = null;

    public String getApproveOrRejectMessage() {
        return this.approveOrRejectMessage;
    }

    public void setApproveOrRejectMessage(final String approveOrRejectMessage) {
        this.approveOrRejectMessage = approveOrRejectMessage;
    }

    public boolean isActionAlreadyTakenFlag() {
        return this.actionAlreadyTakenFlag;
    }

    public void setActionAlreadyTakenFlag(final boolean actionAlreadyTakenFlag) {
        this.actionAlreadyTakenFlag = actionAlreadyTakenFlag;
    }

    public List<WipsPendingApprovalOutput> getAtp() {
        return this.atp;
    }

    public void setAtp(final List<WipsPendingApprovalOutput> atp) {
        this.atp = atp;
    }

    public List<ApproveWarningHandler> getWarningMessagesList() {
        return this.warningMessagesList;
    }

    public void setWarningMessagesList(
            final List<ApproveWarningHandler> warningMessagesList) {
        this.warningMessagesList = warningMessagesList;
    }

}
