//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.atp;

import com.ford.purchasing.wips.common.layer.SupplierDetail;

public class StrategySupplier extends SupplierDetail {
    private String supplierCode;
    private String percentage;

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(final String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(final String percentage) {
        this.percentage = percentage;
    }
}
