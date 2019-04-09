//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.atp;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.connector.BiConsumerThrowsException;

@SuppressWarnings("javadoc")
public class S01i0TransactionInput {

    public BiConsumerThrowsException<S01i0TransactionOutput, DataArea, ConnectorException> loadS01i0Input(
            final short pfKey) {
        return new BiConsumerThrowsException<S01i0TransactionOutput, DataArea, ConnectorException>() {

            @Override
            public void accept(final S01i0TransactionOutput priceClaimRemarksOutput, final DataArea inputDataArea)
                    throws ConnectorException {
                inputDataArea.put("TPI-PFKEY", pfKey);
            }

        };
    }

}
