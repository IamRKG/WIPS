//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.layer;

import java.util.ArrayList;
import java.util.List;

public class PendingApprovalItemsResponse extends WipsBaseResponse {
    private String categoryCode;
    private List<WipsPendingApprovalOutput> atp = new ArrayList<WipsPendingApprovalOutput>();
    private List<WipsPendingApprovalOutput> lumpSum = new ArrayList<WipsPendingApprovalOutput>();
    private List<WipsPendingApprovalOutput> priceClaims = new ArrayList<WipsPendingApprovalOutput>();

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(final String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public List<WipsPendingApprovalOutput> getAtp() {
        return atp;
    }

    public void setAtp(final List<WipsPendingApprovalOutput> atp) {
        this.atp = atp;
    }

    public List<WipsPendingApprovalOutput> getLumpSum() {
        return lumpSum;
    }

    public void setLumpSum(final List<WipsPendingApprovalOutput> lumpSum) {
        this.lumpSum = lumpSum;
    }

    public List<WipsPendingApprovalOutput> getPriceClaims() {
        return priceClaims;
    }

    public void setPriceClaims(List<WipsPendingApprovalOutput> priceClaims) {
        this.priceClaims = priceClaims;
    }

}
