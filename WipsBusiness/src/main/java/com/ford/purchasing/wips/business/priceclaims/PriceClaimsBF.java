//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.business.priceclaims;

import java.util.List;

import javax.inject.Inject;

import com.ford.purchasing.wips.business.layer.WipsBaseBF;
import com.ford.purchasing.wips.common.layer.PendingApprovalItemsResponse;
import com.ford.purchasing.wips.common.layer.WipsBaseResponse;
import com.ford.purchasing.wips.common.layer.WipsPendingApprovalOutput;
import com.ford.purchasing.wips.common.priceclaims.PriceClaimsApproveOrRejectResponse;
import com.ford.purchasing.wips.common.priceclaims.PriceClaimsRequest;
import com.ford.purchasing.wips.domain.layer.PendingApprovalAS;
import com.ford.purchasing.wips.domain.priceclaims.service.PriceClaimsAS;

/**
 * Business Facade class for ATP Approval Module functionality.
 */
public class PriceClaimsBF extends WipsBaseBF {

    @Inject
    private PendingApprovalAS pendingApprovalAS;

    @Inject
    private PriceClaimsAS priceClaimsAS;

    private List<WipsPendingApprovalOutput> populatePriceClaimsList(final PriceClaimsRequest request) {
        WipsBaseResponse response = new WipsBaseResponse();
        PendingApprovalItemsResponse pendingApprovalItemsResponse = new PendingApprovalItemsResponse();
        response = this.pendingApprovalAS.getPendingClaims(request);
        if (!response.isErrorFlag()) {
            pendingApprovalItemsResponse = (PendingApprovalItemsResponse)response;
        }
        return pendingApprovalItemsResponse.getPriceClaims();

    }

    public WipsBaseResponse saveRemarksAndApproveOrRejectPriceClaims(final PriceClaimsRequest request) {
        PriceClaimsApproveOrRejectResponse response = new PriceClaimsApproveOrRejectResponse();
        final WipsBaseResponse approveResponse = this.priceClaimsAS.approveOrRejectPriceClaimRemarks(request);
        if (!approveResponse.isErrorFlag()) {
            response = (PriceClaimsApproveOrRejectResponse)approveResponse;
            response.setPriceClaims(populatePriceClaimsList(request));
            return response;
        }
        return approveResponse;
    }
}
