//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.atp;

import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.connector.BiFunctionThrowsException;
import com.ford.purchasing.wips.common.layer.WipsImsOutput;

public class G51xTransactionOutput extends WipsImsOutput {

    public BiFunctionThrowsException<DataArea, String, G51xTransactionOutput, Exception> getG51xOutput() {
        return new BiFunctionThrowsException<DataArea, String, G51xTransactionOutput, Exception>() {

            @Override
            public G51xTransactionOutput apply(final DataArea outputDataArea,
                    final String rawOutput) throws Exception {
                return new G51xTransactionOutput();
            }
        };
    }

}
