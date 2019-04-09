//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.lumpsum;

import java.util.List;

import com.ford.purchasing.wips.common.atp.ApproveWarningHandler;
import com.ford.purchasing.wips.common.layer.WipsBaseResponse;
import com.ford.purchasing.wips.common.layer.WipsPendingApprovalOutput;

public class LumpSumApproveOrRejectResponse extends WipsBaseResponse {
    String approveOrRejectMessage;
    boolean actionAlreadyTakenFlag;
    private List<WipsPendingApprovalOutput> lumpSum;
    private List<ApproveWarningHandler> warningMessagesList = null;
    private boolean approvalWarningFlag;

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

    public List<ApproveWarningHandler> getWarningMessagesList() {
        return this.warningMessagesList;
    }

    public void setWarningMessagesList(
            final List<ApproveWarningHandler> warningMessagesList) {

        this.warningMessagesList = warningMessagesList;
    }

    public List<WipsPendingApprovalOutput> getLumpSum() {
        return lumpSum;
    }

    public void setLumpSum(final List<WipsPendingApprovalOutput> lumpSum) {
        this.lumpSum = lumpSum;
    }

    public boolean isApprovalWarningFlag() {
        return approvalWarningFlag;
    }

    public void setApprovalWarningFlag(final boolean approvalWarningFlag) {
        this.approvalWarningFlag = approvalWarningFlag;
    }

}
