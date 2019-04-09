//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.priceclaims;

import java.util.List;

public class PCSBusinessUnit {
    private String businessUnit;
    private List<BusinessUnitDetail> pcsBusinessUnitDetails;
    private PCSFinancialInfoDetail businessUnitTotalDetail;

    public List<BusinessUnitDetail> getPcsBusinessUnitDetails() {
        return this.pcsBusinessUnitDetails;
    }

    public void setPcsBusinessUnitDetails(final List<BusinessUnitDetail> pcsBusinessUnitDetails) {

        this.pcsBusinessUnitDetails = pcsBusinessUnitDetails;

    }

    public String getBusinessUnit() {
        return this.businessUnit;
    }

    public void setBusinessUnit(final String businessUnit) {

        this.businessUnit = businessUnit;

    }

    public PCSFinancialInfoDetail getBusinessUnitTotalDetail() {
        return this.businessUnitTotalDetail;
    }

    public void setBusinessUnitTotalDetail(final PCSFinancialInfoDetail businessUnitTotalDetail) {

        this.businessUnitTotalDetail = businessUnitTotalDetail;

    }

    @Override
    public boolean equals(final Object that) {
        if (that instanceof PCSBusinessUnit) {
            return this.businessUnit.equals(((PCSBusinessUnit)that).getBusinessUnit());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.businessUnit.hashCode();
    }
}


