//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.pcs.beans;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.connector.BiConsumerThrowsException;
import com.ford.purchasing.wips.common.layer.WipsConstant;

@SuppressWarnings("javadoc")
public class Cr3vTransactionInput {

    public static BiConsumerThrowsException<Cr3vTransactionOutput, DataArea, ConnectorException> loadCr3vInput() {
        return new BiConsumerThrowsException<Cr3vTransactionOutput, DataArea, ConnectorException>() {

            @Override
            public void accept(final Cr3vTransactionOutput cr3vTransactionOutput,
                    final DataArea inputDataArea)
                    throws ConnectorException {
                inputDataArea.put("TPI-IOINDIC", WipsConstant.InputI);
                inputDataArea.put("TPI-PFKEY", WipsConstant.PFKEY1);
            }
        };
    }

}
