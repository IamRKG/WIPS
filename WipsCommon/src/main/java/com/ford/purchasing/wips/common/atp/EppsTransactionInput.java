package com.ford.purchasing.wips.common.atp;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.connector.BiConsumerThrowsException;
import com.ford.purchasing.wips.common.layer.WipsBaseRequest;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.WipsImsInput;

@SuppressWarnings("javadoc")
public class EppsTransactionInput extends WipsImsInput {

    private String altJobCode;

    public String getAltJobCode() {
        return this.altJobCode;
    }

    public void setAltJobCode(final String altJobCode) {
        this.altJobCode = altJobCode;
    }

    public void populate(final DataArea inputDataArea) {
        inputDataArea.put("TP-INPUT-FIELDS.TPI-IOINDIC", this.getTpiIoindic());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-PFKEY", this.getTpiPfKey());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-RACF-ID", this.getUserRacfId());

    }

    public EppsTransactionInput createEppsInput(
            final WipsBaseRequest priceClaimsRequest) {
        this.setTpiIoindic(WipsConstant.InputO);
        this.setTpiPfKey(WipsConstant.PFKEY0);
        this.setUserRacfId(priceClaimsRequest.getUserRacfId());
        this.setImsAction(priceClaimsRequest.getActionTaken());
        this.setLterm(priceClaimsRequest.getLterm());
        return this;
    }

    public BiConsumerThrowsException<WipsBaseRequest, DataArea, ConnectorException> loadEppsInput() {
        return new BiConsumerThrowsException<WipsBaseRequest, DataArea, ConnectorException>() {
            @Override
            public void accept(
                    final WipsBaseRequest priceClaimsRequest,
                    final DataArea inputDataArea)
                    throws ConnectorException {
                final EppsTransactionInput wipsEppsInput =
                        new EppsTransactionInput().createEppsInput(priceClaimsRequest);
                wipsEppsInput.populate(inputDataArea);
            }
        };
    }
}
