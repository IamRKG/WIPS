//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.atp.beans;

import java.util.ArrayList;
import java.util.List;

import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.atp.StrategyPlantDetail;
import com.ford.purchasing.wips.common.atp.StrategyTemporaryOutput;
import com.ford.purchasing.wips.common.connector.BiFunctionThrowsException;
import com.ford.purchasing.wips.common.layer.WipsImsOutput;
import com.ford.purchasing.wips.common.layer.util.TransactionOutputUtil;
import com.ford.purchasing.wips.domain.layer.WipsTransactionConstant;

public class G55xTransactionOutput extends WipsImsOutput {

    private List plants;
    private boolean morePlants;
    private String rankNumber;

    public List getPlants() {
        return this.plants;
    }

    public void setPlants(final List plants) {
        this.plants = plants;
    }

    public void setMorePlants(final boolean morePlants) {
        this.morePlants = morePlants;
    }

    public boolean isMorePlants() {
        return this.morePlants;
    }

    public void setRankNumber(final String rankNumber) {
        this.rankNumber = rankNumber;
    }

    public String getRankNumber() {
        return this.rankNumber;
    }

    public BiFunctionThrowsException<DataArea, String, G55xTransactionOutput, Exception> getG55xOutput(
            final G55xTransactionOutput g55xOutput) {
        return new BiFunctionThrowsException<DataArea, String, G55xTransactionOutput, Exception>() {

            @Override
            public G55xTransactionOutput apply(final DataArea outputDataArea, final String rawOutput) throws Exception {
                final AtpStrategyHelper atpStrategyHelper = new AtpStrategyHelper();
                g55xOutput.plants = new ArrayList<StrategyPlantDetail>();
                g55xOutput.setMorePlants(
                        TransactionOutputUtil.getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-MORE")
                                .contains(WipsTransactionConstant.MORE));
                g55xOutput.setRankNumber(
                        TransactionOutputUtil.getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-RANKNUM-CHAR"));
                final List<StrategyTemporaryOutput> plants = atpStrategyHelper.getPlants(outputDataArea);
                g55xOutput.getPlants().addAll(atpStrategyHelper.groupDataByPlant(plants));
                g55xOutput.setErrorFlag(false);
                return g55xOutput;
            }

        };
    }
}
