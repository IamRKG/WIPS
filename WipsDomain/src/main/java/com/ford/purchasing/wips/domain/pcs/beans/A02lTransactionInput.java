//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.pcs.beans;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.connector.BiConsumerThrowsException;
import com.ford.purchasing.wips.common.layer.WipsConstant;

public class A02lTransactionInput {

    public static BiConsumerThrowsException<A02lTransactionOutput, DataArea, ConnectorException> loadA02lInput(final short pfKey) {
        return new BiConsumerThrowsException<A02lTransactionOutput, DataArea, ConnectorException>() {
            @Override
            public void accept(final A02lTransactionOutput a02lTransactionOutput, final DataArea inputDataArea)
                throws ConnectorException {
                inputDataArea.put("TPI-IOINDIC", WipsConstant.InputI);
                inputDataArea.put("TPI-PFKEY", pfKey);
            }
        };
    }
}
