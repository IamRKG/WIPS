package com.ford.purchasing.wips.common.lumpsum;

import com.ford.purchasing.wips.common.layer.SupplierDetail;

@SuppressWarnings("javadoc")
public class SupplierInformation extends SupplierDetail {

    private String parentCode;

    public String getParentCode() {
        return this.parentCode;
    }

    public void setParentCode(final String parentCode) {
        this.parentCode = parentCode;
    }

}
