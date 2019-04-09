//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.atp;

import com.ford.purchasing.wips.common.layer.SupplierDetail;

@SuppressWarnings("javadoc")
public class AtpSupplierDetail extends SupplierDetail {

    private String newPrice;
    private String priceChange;
    private String annualCost;
    private String costSign;
    private String sourcingPercentage;
    private String authority;
    private String webquoteAttachment;
    private String ppc;
    private String usdLiteral;
    private String recapType;

    public String getNewPrice() {
        return this.newPrice;
    }

    public void setNewPrice(final String newPrice) {
        this.newPrice = newPrice;
    }

    public String getPriceChange() {
        return this.priceChange;
    }

    public void setPriceChange(final String priceChange) {
        this.priceChange = priceChange;
    }

    public String getAnnualCost() {
        return this.annualCost;
    }

    public void setAnnualCost(final String annualCost) {
        this.annualCost = annualCost;
    }

    public String getSourcingPercentage() {
        return this.sourcingPercentage;
    }

    public void setSourcingPercentage(final String sourcingPercentage) {
        this.sourcingPercentage = sourcingPercentage;
    }

    public String getAuthority() {
        return this.authority;
    }

    public void setAuthority(final String authority) {
        this.authority = authority;
    }

    public String getWebquoteAttachment() {
        return this.webquoteAttachment;
    }

    public void setWebquoteAttachment(final String webquoteAttachment) {
        this.webquoteAttachment = webquoteAttachment;
    }

    public String getPpc() {
        return this.ppc;
    }

    public void setPpc(final String ppc) {
        this.ppc = ppc;
    }

    public String getUsdLiteral() {
        return this.usdLiteral;
    }

    public void setUsdLiteral(final String usdLiteral) {
        this.usdLiteral = usdLiteral;
    }

    public String getCostSign() {
        return this.costSign;
    }

    public void setCostSign(final String costSign) {

        this.costSign = costSign;
    }

    public String getRecapType() {
        return this.recapType;
    }

    public void setRecapType(final String recapType) {

        this.recapType = recapType;
    }

}
