//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.atp.beans;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.connector.BiConsumerThrowsException;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.WipsImsInput;

public class G55xTransactionInput extends WipsImsInput {

    private String rankNumber;

    public String getRankNumber() {
        return this.rankNumber;
    }

    public void setRankNumber(final String rankNumber) {
        this.rankNumber = rankNumber;
    }

    public BiConsumerThrowsException<G55xTransactionOutput, DataArea, ConnectorException> loadG55xInput() {
        return new BiConsumerThrowsException<G55xTransactionOutput, DataArea, ConnectorException>() {

            @Override
            public void accept(final G55xTransactionOutput input, final DataArea inputDataArea)
                    throws ConnectorException {
                final G55xTransactionInput g55Input = new G55xTransactionInput();
                g55Input.setTpiPageNo(WipsConstant.PAGENUMBERONE);
                g55Input.setRankNumber(rankNumber);
                g55Input.setTpiIoindic(WipsConstant.InputI);
                g55Input.setTpiPfKey(WipsConstant.PFKEY8);
                g55Input.populate(inputDataArea, g55Input);
            }
        };

    }

    protected void populate(DataArea inputDataArea, G55xTransactionInput g55Input) {
        inputDataArea.put("TPI-IOINDIC", g55Input.getTpiIoindic());
        inputDataArea.put("TPI-PFKEY", g55Input.getTpiPfKey());
        inputDataArea.put("TPI-PAGENO", g55Input.getTpiPageNo());
        inputDataArea.put("TPI-RANKNUM", g55Input.getRankNumber());
    }

}
