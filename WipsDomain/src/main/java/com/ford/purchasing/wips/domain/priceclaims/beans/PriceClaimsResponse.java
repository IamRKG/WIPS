//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.priceclaims.beans;

import java.util.List;

import com.ford.purchasing.wips.common.layer.ApprovalDetail;
import com.ford.purchasing.wips.common.layer.WipsBaseResponse;
import com.ford.purchasing.wips.domain.pcs.beans.FinancialImpactInformation;
import com.ford.purchasing.wips.domain.pcs.beans.PriceClaimSummaryInformation;

public class PriceClaimsResponse extends WipsBaseResponse {

    PriceClaimSummaryInformation pcsSummaryInformation;
    List<ApprovalDetail> priceClaimRemarks;
    List<String> documentRemarks;
    private FinancialImpactInformation financialImpactInformation;

    public List<ApprovalDetail> getPriceClaimRemarks() {
        return this.priceClaimRemarks;
    }

    public void setPriceClaimRemarks(final List<ApprovalDetail> priceClaimRemarks) {

        this.priceClaimRemarks = priceClaimRemarks;
    }

    public PriceClaimSummaryInformation getPcsSummaryInformation() {
        return pcsSummaryInformation;
    }

    public void setPcsSummaryInformation(
        final PriceClaimSummaryInformation pcsSummaryInformation) {
        this.pcsSummaryInformation = pcsSummaryInformation;
    }

    public List<String> getDocumentRemarks() {
        return documentRemarks;
    }

    public void setDocumentRemarks(final List<String> documentRemarks) {
        this.documentRemarks = documentRemarks;
    }

    public FinancialImpactInformation getFinancialImpactInformation() {
        return this.financialImpactInformation;
    }

    public void setFinancialImpactInformation(final FinancialImpactInformation financialImpactInformation) {

        this.financialImpactInformation = financialImpactInformation;
    }

}
