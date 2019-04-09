package com.ford.purchasing.wips.common.layer;

public class WipsPcsOutput extends WipsPendingApprovalOutput{
    private String effectiveDate;
    private String claimTitle;
    private String supplier;
    private String supplierName;

    public String getEffectiveDate() {
        return this.effectiveDate;
    }

    public void setEffectiveDate(final String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getClaimTitle() {
        return this.claimTitle;
    }

    public void setClaimTitle(final String claimTitle) {
        this.claimTitle = claimTitle;
    }

    public String getSupplier() {
        return this.supplier;
    }

    public void setSupplier(final String supplier) {
        this.supplier = supplier;
    }

    public String getSupplierName() {
        return this.supplierName;
    }

    public void setSupplierName(final String supplierName) {
        this.supplierName = supplierName;

    }


}
