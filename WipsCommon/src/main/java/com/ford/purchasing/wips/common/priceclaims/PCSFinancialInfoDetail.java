//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.priceclaims;

public class PCSFinancialInfoDetail {
    private String claimTOPercentage;
    private String frznTO;
    private String nDPCSave;
    private String nDPCSavePercentage;

    public String getClaimTOPercentage() {
        return this.claimTOPercentage;
    }

    public void setClaimTOPercentage(final String claimTOPercentage) {

        this.claimTOPercentage = claimTOPercentage;
    }

    public String getFrznTO() {
        return this.frznTO;
    }

    public void setFrznTO(final String frznTO) {

        this.frznTO = frznTO;
    }

    public String getNDPCSave() {
        return this.nDPCSave;
    }

    public void setNDPCSave(final String nDPCSave) {

        this.nDPCSave = nDPCSave;
    }

    public String getNDPCSavePercentage() {
        return this.nDPCSavePercentage;
    }

    public void setNDPCSavePercentage(final String nDPCSavePercentage) {

        this.nDPCSavePercentage = nDPCSavePercentage;
    }

}
