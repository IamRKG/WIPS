package com.ford.purchasing.wips.common.layer;

@SuppressWarnings("javadoc")
public class WipsLumsumOutput extends WipsPendingApprovalOutput {

    private String cause;

    private String supplierCode;

    public String getCause() {
        return this.cause;
    }

    public void setCause(final String cause) {
        this.cause = cause;
    }

    public String getSupplierCode() {
        return this.supplierCode;
    }

    public void setSupplierCode(final String supplierCode) {
        this.supplierCode = supplierCode;
    }

}
