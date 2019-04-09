package com.ford.purchasing.wips.domain.atp.beans;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.connector.BiConsumerThrowsException;
import com.ford.purchasing.wips.common.layer.PendingApprovalRequest;
import com.ford.purchasing.wips.common.layer.WipsBaseRequest;
import com.ford.purchasing.wips.common.layer.WipsImsInput;

public class M00mTransactionInput extends WipsImsInput {


    public BiConsumerThrowsException<M00mTransactionOutput, DataArea, ConnectorException> loadM00mSwitchJobInput(
            final WipsBaseRequest priceClaimsRequest) {
        return new BiConsumerThrowsException<M00mTransactionOutput, DataArea, ConnectorException>() {

            @Override
            public void accept(
                final M00mTransactionOutput m00mTransactionOutput,
                    final DataArea inputDataArea)
                    throws ConnectorException {
                final WipsImsInput m00mInput =
                        new WipsImsInput().populateSwitchJobCodeInput(priceClaimsRequest);
                m00mInput.populateM00m(inputDataArea);
            }
        };
    }

    public BiConsumerThrowsException<M00mTransactionOutput, DataArea, ConnectorException> loadM00mInput(
        final PendingApprovalRequest pendingApprovalRequest) {
        return new BiConsumerThrowsException<M00mTransactionOutput, DataArea, ConnectorException>() {

            @Override
            public void accept(
                final M00mTransactionOutput m00mTransactionOutput,
                    final DataArea inputDataArea)
                    throws ConnectorException {
                final WipsImsInput m00mInput =
                    new WipsImsInput().populateM00mlInput(pendingApprovalRequest);
                m00mInput.populateM00m(inputDataArea);
            }
        };
    }
}
