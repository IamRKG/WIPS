//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.priceclaims;

public class PurchasingManagerDetail {
    private String pmCode;
    private String pmName;
    private PCSFinancialInfoDetail pcsFinancialInfoDetail;

    public String getPmCode() {
        return this.pmCode;
    }

    public void setPmCode(final String pmCode) {

        this.pmCode = pmCode;

    }

    public String getPmName() {
        return this.pmName;
    }

    public void setPmName(final String pmName) {

        this.pmName = pmName;

    }

    public PCSFinancialInfoDetail getPcsFinancialInfoDetail() {
        return this.pcsFinancialInfoDetail;
    }

    public void setPcsFinancialInfoDetail(final PCSFinancialInfoDetail pcsFinancialInfoDetail) {

        this.pcsFinancialInfoDetail = pcsFinancialInfoDetail;

    }

}
