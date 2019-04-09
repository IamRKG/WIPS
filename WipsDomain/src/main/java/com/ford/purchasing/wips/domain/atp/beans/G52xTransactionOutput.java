//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.atp.beans;

import java.util.List;

import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.atp.StrategyOutput;
import com.ford.purchasing.wips.common.atp.StrategyPlantDetail;
import com.ford.purchasing.wips.common.connector.BiFunctionThrowsException;
import com.ford.purchasing.wips.common.layer.WipsImsOutput;
import com.ford.purchasing.wips.common.layer.exception.WipsImsTransactionException;
import com.ford.purchasing.wips.common.layer.util.TransactionOutputUtil;
import com.ford.purchasing.wips.domain.layer.WipsTransactionConstant;

@SuppressWarnings("javadoc")
public class G52xTransactionOutput extends WipsImsOutput {

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

    public BiFunctionThrowsException<DataArea, String, StrategyOutput, Exception> getG52xOutput() {
        return new BiFunctionThrowsException<DataArea, String, StrategyOutput, Exception>() {

            @Override
            public StrategyOutput apply(final DataArea outputDataArea, final String rawOutput) throws Exception {
                StrategyOutput g52xOutput = new StrategyOutput();
                final String transactionName = TransactionOutputUtil.getString(outputDataArea,
                        "TP-OUTPUT-BUFFER-FIELDS.TPO-PROGRAM");
                if (WipsTransactionConstant.G52X_TRANSACTION_NAME.equals(transactionName)) {
                    final AtpStrategyHelper atpStrategyHelper = new AtpStrategyHelper();
                    g52xOutput = atpStrategyHelper.getStrategyDetails(outputDataArea);
                    g52xOutput.setErrorFlag(false);
                } else {
                    throw new WipsImsTransactionException(WipsTransactionConstant.AAIMG53X_G52X_TRANSACTION_NAME,
                            rawOutput.substring(63, 139));
                }
                return g52xOutput;
            }

        };
    }

}
