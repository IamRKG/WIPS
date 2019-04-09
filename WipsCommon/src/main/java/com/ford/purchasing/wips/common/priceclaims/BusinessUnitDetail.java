//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.priceclaims;

public class BusinessUnitDetail {
    private String commodityCode;
    private PCSFinancialInfoDetail pcsFinancialInfoDetail;

    public String getCommodityCode() {
        return this.commodityCode;
    }

    public void setCommodityCode(final String commodityCode) {

        this.commodityCode = commodityCode;
    }

    public PCSFinancialInfoDetail getPcsFinancialInfoDetail() {
        return this.pcsFinancialInfoDetail;
    }

    public void setPcsFinancialInfoDetail(PCSFinancialInfoDetail pcsFinancialInfoDetail) {
        
        this.pcsFinancialInfoDetail = pcsFinancialInfoDetail;
        
    }


}
