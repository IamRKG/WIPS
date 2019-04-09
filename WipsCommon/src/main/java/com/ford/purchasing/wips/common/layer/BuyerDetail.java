//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.layer;

public class BuyerDetail {

    private String buyerCode;
    private String buyerName;

    public BuyerDetail(final String buyerCode, final String buyerName) {
        this.buyerCode = buyerCode;
        this.buyerName = buyerName;
    }

    public String getBuyerCode() {
        return this.buyerCode;
    }

    public String getBuyerName() {
        return this.buyerName;
    }

    public String getBuyerCodeAndName() {
        return this.buyerCode + " - " + this.buyerName;
    }

}
