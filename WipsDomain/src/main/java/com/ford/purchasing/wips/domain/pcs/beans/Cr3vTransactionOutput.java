//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.pcs.beans;

import java.util.List;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.connector.BiFunctionThrowsException;
import com.ford.purchasing.wips.common.layer.WipsImsOutput;
import com.ford.purchasing.wips.common.layer.util.TransactionOutputUtil;

public class Cr3vTransactionOutput extends WipsImsOutput {

    List<String> strRemarks;

    public List<String> getStrRemarks() {
        return this.strRemarks;
    }

    public void setStrRemarks(final List<String> strRemarks) {

        this.strRemarks = strRemarks;
    }

    public static Cr3vTransactionOutput from(final DataArea outputDataArea)
        throws ConnectorException {
        final Cr3vTransactionOutput output = new Cr3vTransactionOutput();
        output.setStrRemarks(TransactionOutputUtil.getListOfValuesWithoutSpaces(outputDataArea, "TPO-POARKS"));
        return output;
    }

    public static BiFunctionThrowsException<DataArea, String, Cr3vTransactionOutput, Exception> getCr3vOutput() {
        return new BiFunctionThrowsException<DataArea, String, Cr3vTransactionOutput, Exception>() {

            @Override
            public Cr3vTransactionOutput apply(
                final DataArea outputDataArea,
                final String rawOutput) throws Exception {
                return Cr3vTransactionOutput.from(outputDataArea);
            }

        };
    }

}
