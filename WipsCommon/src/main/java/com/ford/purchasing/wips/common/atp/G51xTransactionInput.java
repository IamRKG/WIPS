//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.atp;

import java.util.List;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.connector.BiConsumerThrowsException;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.WipsImsInput;
import com.ford.purchasing.wips.common.layer.util.StringUtil;
import com.ford.purchasing.wips.common.layer.util.TransactionOutputUtil;

public class G51xTransactionInput extends WipsImsInput {

    public BiConsumerThrowsException<G51xTransactionOutput, DataArea, ConnectorException> loadG51xInput() {
        return new BiConsumerThrowsException<G51xTransactionOutput, DataArea, ConnectorException>() {

            @Override
            public void accept(final G51xTransactionOutput t, final DataArea inputDataArea)
                    throws ConnectorException {
                final G51xTransactionInput g51XInput = new G51xTransactionInput();
                g51XInput.setTpiIoindic(WipsConstant.InputI);
                g51XInput.setTpiPfKey(WipsConstant.PFKEY13);
                g51XInput.setTpiPageNo(WipsConstant.PAGENUMBERONE);
                g51XInput.setTpiSelect(StringUtil.createBlankSpaces(1));
                g51XInput.populate(inputDataArea);
            }

        };
    }

    private void populate(final DataArea inputDataArea) throws ConnectorException {
        inputDataArea.put("TPI-IOINDIC", this.getTpiIoindic());
        inputDataArea.put("TPI-PFKEY", this.getTpiPfKey());
        inputDataArea.put("TPI-PAGENO", this.getTpiPageNo());
        final List<DataArea> segLoop = TransactionOutputUtil.getArray(inputDataArea, "TPI-BUFFER-SEGLOOP");
        for (final DataArea dataArea : segLoop) {
            dataArea.put("TPI-SELECT", this.getTpiSelect());
        }
    }

}
