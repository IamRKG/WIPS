package com.ford.purchasing.wips.domain.layer;

import javax.inject.Inject;

import com.ford.purchasing.wips.common.layer.PendingApprovalRequest;
import com.ford.purchasing.wips.common.layer.WipsBaseResponse;
import com.ford.purchasing.wips.domain.atp.connector.M00mQ01xTransaction;
import com.ford.purchasing.wips.domain.atp.connector.Q01xTransaction;
import com.ford.purchasing.wips.domain.connector.Ims;

@SuppressWarnings("javadoc")
public class PendingApprovalAS {
    @Inject
    protected M00mQ01xTransaction m00mQ01xTransaction;

    @Inject
    protected Q01xTransaction q01xTransaction;

    @Inject
    private Ims ims;
    @Inject
    RetrievePendingAtpsAndLumpsums pendingAtpsAndLumpsums;
    @Inject
    RetrievePendingClaims retrievePendingClaims;

    public PendingApprovalAS() {

    }

    public PendingApprovalAS(final Ims ims) {
        this.ims = ims;
    }

    public WipsBaseResponse getPendingClaims(
            final PendingApprovalRequest pendingApprovalRequest) {
        this.retrievePendingClaims.setPendingApprovalRequest(pendingApprovalRequest);
        return this.ims.converse(pendingApprovalRequest.getLterm(),
                this.retrievePendingClaims);

    }

    public WipsBaseResponse getPendingAtpsAndLumpsums(
            final PendingApprovalRequest pendingApprovalRequest) {
        this.pendingAtpsAndLumpsums.setPendingApprovalRequest(pendingApprovalRequest);
        return this.ims.converse(pendingApprovalRequest.getLterm(),
                this.pendingAtpsAndLumpsums);
    }

}
