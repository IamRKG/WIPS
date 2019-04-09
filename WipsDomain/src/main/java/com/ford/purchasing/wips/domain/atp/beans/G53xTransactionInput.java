//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.atp.beans;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.atp.AtpRequest;
import com.ford.purchasing.wips.common.atp.EppsTransactionInput;
import com.ford.purchasing.wips.common.atp.S01i0TransactionOutput;
import com.ford.purchasing.wips.common.connector.BiConsumerThrowsException;
import com.ford.purchasing.wips.common.layer.WipsBaseRequest;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.WipsImsInput;

public class G53xTransactionInput extends WipsImsInput {

    String ptpnId;
    String ptpnMore;
    String rankNo;
    String recapReason;
    String effectiveDate;
    String ztoind1;
    String ztoind2;
    String ztoind3;
    String authCd1;
    String authCd2;
    String authCd3;
    String ashppt1;
    String ashppt2;
    String ashppt3;
    String autoPo;
    String command;
    String actionTaken;

    public String getActionTaken() {
        return actionTaken;
    }

    public void setActionTaken(final String actionTaken) {
        this.actionTaken = actionTaken;
    }

    public String getPtpnId() {
        return this.ptpnId;
    }

    public void setPtpnId(final String ptpnId) {

        this.ptpnId = ptpnId;
    }

    public String getPtpnMore() {
        return this.ptpnMore;
    }

    public void setPtpnMore(final String ptpnMore) {

        this.ptpnMore = ptpnMore;
    }

    public String getRankNo() {
        return this.rankNo;
    }

    public void setRankNo(final String rankNo) {

        this.rankNo = rankNo;
    }

    public String getRecapReason() {
        return this.recapReason;
    }

    public void setRecapReason(final String recapReason) {

        this.recapReason = recapReason;
    }

    public String getEffectiveDate() {
        return this.effectiveDate;
    }

    public void setEffectiveDate(final String effectiveDate) {

        this.effectiveDate = effectiveDate;
    }

    public String getAuthCd1() {
        return this.authCd1;
    }

    public void setAuthCd1(final String authCd1) {

        this.authCd1 = authCd1;
    }

    public String getAuthCd2() {
        return this.authCd2;
    }

    public void setAuthCd2(final String authCd2) {

        this.authCd2 = authCd2;
    }

    public String getAuthCd3() {
        return this.authCd3;
    }

    public void setAuthCd3(final String authCd3) {

        this.authCd3 = authCd3;
    }

    public String getAshppt1() {
        return this.ashppt1;
    }

    public void setAshppt1(final String ashppt1) {

        this.ashppt1 = ashppt1;
    }

    public String getAshppt2() {
        return this.ashppt2;
    }

    public void setAshppt2(final String ashppt2) {

        this.ashppt2 = ashppt2;
    }

    public String getAshppt3() {
        return this.ashppt3;
    }

    public void setAshppt3(final String ashppt3) {

        this.ashppt3 = ashppt3;
    }

    public String getAutoPo() {
        return this.autoPo;
    }

    public void setAutoPo(final String autoPo) {

        this.autoPo = autoPo;
    }

    public String getCommand() {
        return this.command;
    }

    public void setCommand(final String command) {

        this.command = command;
    }

    public String getZtoind1() {
        return ztoind1;
    }

    public void setZtoind1(final String ztoind1) {
        this.ztoind1 = ztoind1;
    }

    public String getZtoind2() {
        return ztoind2;
    }

    public void setZtoind2(final String ztoind2) {
        this.ztoind2 = ztoind2;
    }

    public String getZtoind3() {
        return ztoind3;
    }

    public void setZtoind3(final String ztoind3) {
        this.ztoind3 = ztoind3;
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

    public BiConsumerThrowsException<Q01xTransactionOutput, DataArea, ConnectorException> loadG53xInput(
            final G53xTransactionInput g53xTransactionInput, final short pfkey) {
        return new BiConsumerThrowsException<Q01xTransactionOutput, DataArea, ConnectorException>() {

            @Override
            public void accept(final Q01xTransactionOutput input,
                    final DataArea inputDataArea)
                    throws ConnectorException {
                final G53xTransactionInput g53Input = new G53xTransactionInput();
                g53Input.populate(inputDataArea, g53xTransactionInput, pfkey);
            }
        };

    }

    public BiConsumerThrowsException<G53xTransactionOutput, DataArea, ConnectorException> loadG53xInputFromG53(
            final short pfkey) {
        return new BiConsumerThrowsException<G53xTransactionOutput, DataArea, ConnectorException>() {

            @Override
            public void accept(final G53xTransactionOutput input,
                    final DataArea inputDataArea)
                    throws ConnectorException {
                final G53xTransactionInput g53Input = new G53xTransactionInput();
                g53Input.populate(inputDataArea, input.getG53xTransactionInput(), pfkey);
            }
        };

    }

    protected void populate(final DataArea inputDataArea,
            final G53xTransactionInput g53xInput, final short pfkey) {

        inputDataArea.put("TPI-IOINDIC", WipsConstant.InputI);
        inputDataArea.put("TPI-PFKEY", pfkey);
        inputDataArea.put("TPI-PAGENO", g53xInput.getTpiPageNo());
        inputDataArea.put("TPI-PTPNID", g53xInput.getPtpnId());
        inputDataArea.put("TPI-PTPNMORE", g53xInput.getPtpnMore());
        inputDataArea.put("TPI-RANKNO", g53xInput.getRankNo());
        inputDataArea.put("TPI-RECPRSN", g53xInput.getRecapReason());
        inputDataArea.put("TPI-EFFDATE", g53xInput.getEffectiveDate());
        inputDataArea.put("TPI-ZTOIND1", g53xInput.getZtoind1());
        inputDataArea.put("TPI-ZTOIND2", g53xInput.getZtoind2());
        inputDataArea.put("TPI-ZTOIND3", g53xInput.getZtoind3());
        inputDataArea.put("TPI-AUTHCD1", g53xInput.getAuthCd1());
        inputDataArea.put("TPI-AUTHCD2", g53xInput.getAuthCd2());
        inputDataArea.put("TPI-AUTHCD3", g53xInput.getAuthCd3());
        inputDataArea.put("TPI-ASHPPT1", g53xInput.getAshppt1());
        inputDataArea.put("TPI-ASHPPT2", g53xInput.getAshppt2());
        inputDataArea.put("TPI-ASHPPT3", g53xInput.getAshppt3());
        inputDataArea.put("TPI-AUTOPO", g53xInput.getAutoPo());
        inputDataArea.put("TPI-COMAND", g53xInput.getCommand());

    }

    public BiConsumerThrowsException<G53xTransactionOutput, DataArea, ConnectorException> loadG53xInput(
            final AtpRequest atpRequest, final String actionTaken) {
        return new BiConsumerThrowsException<G53xTransactionOutput, DataArea, ConnectorException>() {

            @Override
            public void accept(final G53xTransactionOutput g53xTransactionOutput,
                    final DataArea inputDataArea)
                    throws ConnectorException {
                final G53xTransactionInput g53xInput =
                        g53xTransactionOutput.getG53xTransactionInput();
                g53xInput.setTpiIoindic(WipsConstant.InputI);
                g53xInput.setTpiPfKey(WipsConstant.PFKEY9);
                g53xInput.setActionTaken(actionTaken);
                g53xInput.setLterm(atpRequest.getLterm());

                populate(inputDataArea, g53xInput, g53xInput.getTpiPfKey());
            }
        };
    }

    public BiConsumerThrowsException<S01i0TransactionOutput, DataArea, ConnectorException> loadG53xInputApproveOrReject(
            final G53xTransactionInput g53xTransactionInput, final short pfkey) {
        return new BiConsumerThrowsException<S01i0TransactionOutput, DataArea, ConnectorException>() {

            @Override
            public void accept(final S01i0TransactionOutput input,
                    final DataArea inputDataArea)
                    throws ConnectorException {
                final G53xTransactionInput g53Input = new G53xTransactionInput();
                g53Input.populate(inputDataArea, g53xTransactionInput, pfkey);
            }
        };

    }

}
