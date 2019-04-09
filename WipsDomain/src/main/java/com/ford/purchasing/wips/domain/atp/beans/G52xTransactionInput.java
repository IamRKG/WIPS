//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.atp.beans;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.atp.StrategyOutput;
import com.ford.purchasing.wips.common.connector.BiConsumerThrowsException;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.WipsImsInput;

public class G52xTransactionInput extends WipsImsInput {

    public BiConsumerThrowsException<StrategyOutput, DataArea, ConnectorException> loadG52xInput() {
        return new BiConsumerThrowsException<StrategyOutput, DataArea, ConnectorException>() {

            @Override
            public void accept(final StrategyOutput input, final DataArea inputDataArea) throws ConnectorException {
                final G52xTransactionInput g53Input = new G52xTransactionInput();
                g53Input.setTpiIoindic(WipsConstant.InputI);
                g53Input.setTpiPfKey(WipsConstant.PFKEY20);
                g53Input.setTpiPageNo("01");
                g53Input.populate(inputDataArea, g53Input);
            }
        };

    }

    private void populate(DataArea inputDataArea, G52xTransactionInput g53Input) {
        inputDataArea.put("TPI-IOINDIC", g53Input.getTpiIoindic());
        inputDataArea.put("TPI-PFKEY", g53Input.getTpiPfKey());
        inputDataArea.put("TPI-PAGENO", g53Input.getTpiPageNo());

    }
}
