//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.atp;

import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.connector.BiFunctionThrowsException;
import com.ford.purchasing.wips.common.layer.WipsImsOutput;
import com.ford.purchasing.wips.common.layer.exception.WipsImsTransactionException;

@SuppressWarnings("javadoc")
public class S01i1TransactionOutput extends WipsImsOutput {
    private String confirmMessage;

    public String getConfirmMessage() {
        return this.confirmMessage;
    }

    public void setConfirmMessage(final String confirmMessage) {

        this.confirmMessage = confirmMessage;
    }

    public BiFunctionThrowsException<DataArea, String, S01i1TransactionOutput, Exception> getS01i1Output() {
        return new BiFunctionThrowsException<DataArea, String, S01i1TransactionOutput, Exception>() {

            @Override
            public S01i1TransactionOutput apply(final DataArea outputDataArea, final String rawOutput) throws Exception {
                final S01i1TransactionOutput transactionOutput = new S01i1TransactionOutput();
                if (rawOutput.length() == 1616) {
                    transactionOutput.setConfirmMessage(rawOutput.substring(62, 138));
                    transactionOutput.setErrorFlag(false);
                } else {
                    throw new WipsImsTransactionException("", rawOutput.substring(38, 113));
                }
                return transactionOutput;
            }

        };
    }
}
