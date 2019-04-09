//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.pcs.beans;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.atp.S01i0TransactionOutput;
import com.ford.purchasing.wips.common.connector.BiConsumerThrowsException;
import com.ford.purchasing.wips.common.layer.WipsConstant;

public class C36uTransactionInput {

    public static void populate(final DataArea inputDataArea, final short pfKey) {
        inputDataArea.put("TPI-IOINDIC", WipsConstant.InputI);
        inputDataArea.put("TPI-PFKEY", pfKey);
        inputDataArea.put("TPI-SELVOL", WipsConstant.BLANK_SPACE_1);
        inputDataArea.put("TPI-SELPRC", WipsConstant.BLANK_SPACE_1);
        inputDataArea.put("TPI-SELAPP", WipsConstant.BLANK_SPACE_1);
        inputDataArea.put("TPI-SELPCS", WipsConstant.BLANK_SPACE_1);
        inputDataArea.put("TPI-SELPCT", WipsConstant.BLANK_SPACE_1);
        inputDataArea.put("TPI-SELINT", WipsConstant.BLANK_SPACE_1);
        inputDataArea.put("TPI-SELUOM", WipsConstant.BLANK_SPACE_1);
        inputDataArea.put("TPI-SELCOMM", WipsConstant.BLANK_SPACE_4);
        inputDataArea.put("TPI-SELBUS", WipsConstant.BLANK_SPACE_5);
        inputDataArea.put("TPI-SELDEL", WipsConstant.BLANK_SPACE_1);
    }

    public static BiConsumerThrowsException<PriceClaimSummaryInformation, DataArea, ConnectorException> loadC36uInput(
        final short pfKey) {
        return new BiConsumerThrowsException<PriceClaimSummaryInformation, DataArea, ConnectorException>() {

            @Override
            public void accept(
                final PriceClaimSummaryInformation priceClaimSummaryInformation,
                final DataArea inputDataArea)
                throws ConnectorException {
                populate(inputDataArea, pfKey);
            }
        };
    }

    public BiConsumerThrowsException<S01i0TransactionOutput, DataArea, ConnectorException> loadC36uS01i0Input(
        final short pfKey) {
        return new BiConsumerThrowsException<S01i0TransactionOutput, DataArea, ConnectorException>() {

            @Override
            public void accept(
                final S01i0TransactionOutput priceClaimSummaryInformation,
                final DataArea inputDataArea)
                throws ConnectorException {
                populate(inputDataArea, pfKey);
            }
        };
    }

}
