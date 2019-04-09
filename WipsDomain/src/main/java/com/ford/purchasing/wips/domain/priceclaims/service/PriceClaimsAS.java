//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.priceclaims.service;

import javax.inject.Inject;

import com.ford.purchasing.wips.common.layer.WipsBaseResponse;
import com.ford.purchasing.wips.common.priceclaims.PriceClaimsRequest;
import com.ford.purchasing.wips.domain.connector.Ims;
import com.ford.purchasing.wips.domain.priceclaims.beans.PriceClaimsResponse;

@SuppressWarnings("javadoc")
public class PriceClaimsAS {

    private Ims ims;

    @Inject
    private RetrievePriceClaim retrievePriceClaim;

    @Inject
    private RetrievePriceClaimFinancialInfo retrievePriceClaimFinancialInfo;

    @Inject
    public PriceClaimsAS(final Ims ims) {
        this.ims = ims;
    }

    public WipsBaseResponse retrievePriceClaim(final PriceClaimsRequest priceClaimsRequest) {
        // TODO Remove the setter and replace with constructor injection
        this.retrievePriceClaim.setPriceClaimsRequest(priceClaimsRequest);
        return this.ims.converse(priceClaimsRequest.getLterm(), this.retrievePriceClaim);
    }

    public WipsBaseResponse retrievePriceClaimFinancialInfo(final PriceClaimsRequest priceClaimsRequest) {
        this.retrievePriceClaimFinancialInfo.setPriceClaimsRequest(priceClaimsRequest);
        return this.ims.converse(priceClaimsRequest.getLterm(), this.retrievePriceClaimFinancialInfo);
    }

    public PriceClaimsResponse savePriceClaimRemarks(final PriceClaimsRequest priceClaimsRequest) {
        return this.ims.converse(priceClaimsRequest.getLterm(), new SavePriceClaimRemarks(priceClaimsRequest));
    }

    public WipsBaseResponse approveOrRejectPriceClaimRemarks(final PriceClaimsRequest priceClaimsRequest) {
        return this.ims.converse(priceClaimsRequest.getLterm(), new ApproveOrRejectPriceClaim(priceClaimsRequest));
    }

}
