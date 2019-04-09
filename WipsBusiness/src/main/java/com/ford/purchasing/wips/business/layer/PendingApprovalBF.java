package com.ford.purchasing.wips.business.layer;

import javax.inject.Inject;

import com.ford.it.logging.ILogger;
import com.ford.it.logging.LogFactory;
import com.ford.purchasing.wips.common.layer.PendingApprovalItemsResponse;
import com.ford.purchasing.wips.common.layer.PendingApprovalRequest;
import com.ford.purchasing.wips.common.layer.WipsBaseResponse;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.domain.layer.PendingApprovalAS;

@SuppressWarnings("javadoc")
public class PendingApprovalBF extends WipsBaseBF {

    @Inject
    private PendingApprovalAS pendingApprovalAS;
    private static final String CLASS_NAME = PendingApprovalBF.class.getName();
    private static ILogger log = LogFactory.getInstance().getLogger(CLASS_NAME);

    public PendingApprovalItemsResponse getPendingApprovals(
            final PendingApprovalRequest pendingApprovalRequest) {
        final String methodName = "getPendingApprovals";
        log.entering(CLASS_NAME, methodName);
        PendingApprovalItemsResponse pendingApprovalItemsResponse = null;
        WipsBaseResponse response = null;
        if (pendingApprovalRequest.getCategory()
                .getCategoryCode()
                .equals(WipsConstant.PRICE_CLAIMS_ENTITY_CODE)) {
            response =
                    this.pendingApprovalAS.getPendingClaims(pendingApprovalRequest);
        } else {
            response =
                    this.pendingApprovalAS.getPendingAtpsAndLumpsums(pendingApprovalRequest);
        }
        if (response.isErrorFlag()
            && (response.getErrorMessage().contains("MSG-2968")
                || response.getErrorMessage().contains("MSG-3544"))) {
            pendingApprovalItemsResponse = new PendingApprovalItemsResponse();
        } else {
            pendingApprovalItemsResponse = (PendingApprovalItemsResponse)response;
        }
        log.exiting(CLASS_NAME, methodName);
        return pendingApprovalItemsResponse;
    }

}
