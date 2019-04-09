//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.priceclaims;

import com.ford.purchasing.wips.common.layer.PendingApprovalRequest;

public class PriceClaimsRequest extends PendingApprovalRequest {

    private String priceClaimNumber;
    private String supplier;
    private String[] remarks;
    private boolean remarksEditFlag;
    private String selectedYear;
    private String selectedPM;


    public boolean isRemarksEditFlag() {
        return this.remarksEditFlag;
    }

    public void setRemarksEditFlag(final boolean remarksEditFlag) {
        this.remarksEditFlag = remarksEditFlag;
    }

    public String getSupplier() {
        return this.supplier;
    }

    public void setSupplier(final String supplier) {
        this.supplier = supplier;
    }

    public String getPriceClaimNumber() {
        return this.priceClaimNumber;
    }

    public void setPriceClaimNumber(final String priceClaimNumber) {
        this.priceClaimNumber = priceClaimNumber;
    }

    public String[] getRemarks() {
        return this.remarks;
    }

    public void setRemarks(final String[] remarks) {
        this.remarks = remarks;
    }

    public String getSelectedPM() {
        return this.selectedPM;
    }

    public void setSelectedPM(String selectedPM) {
        
        this.selectedPM = selectedPM;
        
    }

    public String getSelectedYear() {
        return this.selectedYear;
    }

    public void setSelectedYear(String selectedYear) {
        
        this.selectedYear = selectedYear;
        
    }

}