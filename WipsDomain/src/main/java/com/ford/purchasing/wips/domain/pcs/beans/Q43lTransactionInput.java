//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.pcs.beans;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.connector.BiConsumerThrowsException;
import com.ford.purchasing.wips.common.layer.PendingApprovalRequest;
import com.ford.purchasing.wips.common.layer.WipsConstant;

public class Q43lTransactionInput {

    public BiConsumerThrowsException<Q43lTransactionOutput, DataArea, ConnectorException> loadQ43lInput(
        final PendingApprovalRequest priceClaimsRequest, final short pfkey) {
        return new BiConsumerThrowsException<Q43lTransactionOutput, DataArea, ConnectorException>() {

            @Override
            public void accept(
                final Q43lTransactionOutput q43lTransactionOutput,
                final DataArea inputDataArea)
                throws ConnectorException {
                inputDataArea.put("TPI-INPUT.TPI-IOINDIC", WipsConstant.InputI);
                inputDataArea.put("TPI-INPUT.TPI-PFKEY", pfkey);
                inputDataArea.put("TPI-INPUT.TPI-SELLINE", WipsConstant.BLANK_SPACE_2);
                inputDataArea.put("TPI-INPUT.TPI-CLAIM", priceClaimsRequest.getEntityNumber());
                inputDataArea.put("TPI-INPUT.TPI-PAGESKIP", WipsConstant.BLANK_SPACE_2);
            }
        };
    }
}
