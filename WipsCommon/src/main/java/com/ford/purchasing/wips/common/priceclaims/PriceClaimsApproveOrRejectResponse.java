//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.priceclaims;

import java.util.List;

import com.ford.purchasing.wips.common.atp.ApproveWarningHandler;
import com.ford.purchasing.wips.common.layer.WipsBaseResponse;
import com.ford.purchasing.wips.common.layer.WipsPendingApprovalOutput;

public class PriceClaimsApproveOrRejectResponse extends WipsBaseResponse {
    String approveOrRejectMessage;
    private List<WipsPendingApprovalOutput> priceClaims;
    boolean actionAlreadyTakenFlag;
    private List<ApproveWarningHandler> warningMessagesList = null;
    private boolean approvalWarningFlag;

    public boolean isActionAlreadyTakenFlag() {
        return this.actionAlreadyTakenFlag;
    }

    public void setActionAlreadyTakenFlag(final boolean actionAlreadyTakenFlag) {
        this.actionAlreadyTakenFlag = actionAlreadyTakenFlag;
    }

    public List<ApproveWarningHandler> getWarningMessagesList() {
        return this.warningMessagesList;
    }

    public void setWarningMessagesList(final List<ApproveWarningHandler> warningMessagesList) {
        this.warningMessagesList = warningMessagesList;
    }

    public boolean isApprovalWarningFlag() {
        return this.approvalWarningFlag;
    }

    public void setApprovalWarningFlag(final boolean approvalWarningFlag) {
        this.approvalWarningFlag = approvalWarningFlag;
    }

    public String getApproveOrRejectMessage() {
        return this.approveOrRejectMessage;
    }

    public void setApproveOrRejectMessage(final String approveOrRejectMessage) {

        this.approveOrRejectMessage = approveOrRejectMessage;
    }

    public List<WipsPendingApprovalOutput> getPriceClaims() {
        return this.priceClaims;
    }

    public void setPriceClaims(final List<WipsPendingApprovalOutput> priceClaims) {

        this.priceClaims = priceClaims;

    }

}
