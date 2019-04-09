//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.atp;

import java.util.List;

public class StrategyPlantDetail {
    private String plantName;
    private String nationalCompany;
    private String apw;
    private List<StrategySupplier> strategysuppliers;

    public List<StrategySupplier> getStrategysuppliers() {
        return this.strategysuppliers;
    }

    public void setStrategysuppliers(final List<StrategySupplier> strategysuppliers) {
        this.strategysuppliers = strategysuppliers;
    }

    public String getPlantName() {
        return this.plantName;
    }

    public void setPlantName(final String plantName) {
        this.plantName = plantName;
    }

    public String getNationalCompany() {
        return this.nationalCompany;
    }

    public void setNationalCompany(final String nationalCompany) {
        this.nationalCompany = nationalCompany;
    }

    public String getApw() {
        return this.apw;
    }

    public void setApw(final String apw) {
        this.apw = apw;
    }

}
