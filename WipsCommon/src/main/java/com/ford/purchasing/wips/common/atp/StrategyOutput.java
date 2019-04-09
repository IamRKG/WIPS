//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.atp;

import java.util.List;

import com.ford.purchasing.wips.common.layer.WipsImsOutput;

public class StrategyOutput extends WipsImsOutput {
    private String atpNumber;
    private String atpType;
    private String strategy;
    private String part;
    private String enggLevel;
    private String trendRate;
    private String buyer;
    private String status;
    private List<StrategyPlantDetail> plantDetails;

    public List<StrategyPlantDetail> getPlantDetails() {
        return this.plantDetails;
    }

    public void setPlantDetails(final List<StrategyPlantDetail> plantDetails) {
        this.plantDetails = plantDetails;
    }

    public String getAtpNumber() {
        return this.atpNumber;
    }

    public void setAtpNumber(final String atpNumber) {

        this.atpNumber = atpNumber;
    }

    public String getAtpType() {
        return this.atpType;
    }

    public void setAtpType(final String atpType) {
        this.atpType = atpType;
    }

    public String getStrategy() {
        return this.strategy;
    }

    public void setStrategy(final String strategy) {
        this.strategy = strategy;
    }

    public String getPart() {
        return this.part;
    }

    public void setPart(final String part) {
        this.part = part;
    }

    public String getEnggLevel() {
        return this.enggLevel;
    }

    public void setEnggLevel(final String enggLevel) {
        this.enggLevel = enggLevel;
    }

    public String getTrendRate() {
        return this.trendRate;
    }

    public void setTrendRate(final String trendRate) {
        this.trendRate = trendRate;
    }

    public String getBuyer() {
        return this.buyer;
    }

    public void setBuyer(final String buyer) {
        this.buyer = buyer;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

}
