//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.atp.beans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.atp.AtpRemarksRequest;
import com.ford.purchasing.wips.common.atp.AtpRequest;
import com.ford.purchasing.wips.common.connector.BiConsumerThrowsException;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.WipsImsInput;
import com.ford.purchasing.wips.common.layer.util.StringUtil;
import com.ford.purchasing.wips.common.layer.util.TransactionOutputUtil;

@SuppressWarnings("javadoc")
public class G56xTransactionInput extends WipsImsInput {

    public static final String TPO_TEXT = "TPO-TEXT";

    String copyRemark;
    String canRemark1;
    String canRemark2;
    String canRemark3;
    String canRemark4;
    String actionTaken;
    List<String> remarksText;
    List<DataArea> bufferedSegLoop = new ArrayList<DataArea>();

    private static final String CONFIRM = "Confirm";
    private static final String APPROVE = "Approve";

    public List<String> getRemarksText() {
        return this.remarksText;
    }

    public void setRemarksText(final List<String> remarksText) {

        this.remarksText = remarksText;
    }

    public String getActionTaken() {
        return this.actionTaken;
    }

    public void setActionTaken(final String actionTaken) {
        this.actionTaken = actionTaken;
    }

    public String getCopyRemark() {
        return this.copyRemark;
    }

    public void setCopyRemark(final String copyRemark) {

        this.copyRemark = copyRemark;
    }

    public String getCanRemark1() {
        return this.canRemark1;
    }

    public void setCanRemark1(final String canRemark1) {

        this.canRemark1 = canRemark1;
    }

    public String getCanRemark2() {
        return this.canRemark2;
    }

    public void setCanRemark2(final String canRemark2) {

        this.canRemark2 = canRemark2;
    }

    public String getCanRemark3() {
        return this.canRemark3;
    }

    public void setCanRemark3(final String canRemark3) {

        this.canRemark3 = canRemark3;
    }

    public String getCanRemark4() {
        return this.canRemark4;
    }

    public void setCanRemark4(final String canRemark4) {

        this.canRemark4 = canRemark4;
    }

    public List<DataArea> getBufferedSegLoop() {
        return this.bufferedSegLoop;
    }

    public void setBufferedSegLoop(final List<DataArea> bufferedSegLoop) {

        this.bufferedSegLoop = bufferedSegLoop;
    }

    public void populate(final DataArea inputDataArea, final G56xTransactionInput g56xInput) throws ConnectorException {

        inputDataArea.put("TPI-IOINDIC", WipsConstant.InputI);
        inputDataArea.put("TPI-PFKEY", g56xInput.getTpiPfKey());
        inputDataArea.put("TPI-PAGENO", g56xInput.getTpiPageNo());
        inputDataArea.put("TPI-COPYRMK", g56xInput.getCopyRemark());
        inputDataArea.put("TPI-CANRMK1", g56xInput.getCanRemark1());
        inputDataArea.put("TPI-CANRMK2", g56xInput.getCanRemark2());
        inputDataArea.put("TPI-CANRMK3", g56xInput.getCanRemark3());
        inputDataArea.put("TPI-CANRMK4", g56xInput.getCanRemark4());
        // Use outputDataArea for further input.
        final List<DataArea> inputDocList = g56xInput.getBufferedSegLoop();
        final List<String> outputFromDataAreaList = new ArrayList<String>();
        for (final Iterator<DataArea> i = inputDocList.iterator(); i.hasNext();) {
            final DataArea inputDataAreaTemp = i.next();
            outputFromDataAreaList
                .add(TransactionOutputUtil.getString(inputDataAreaTemp, TPO_TEXT));
        }
        final List<DataArea> inputList =
            TransactionOutputUtil.getArray(inputDataArea, "TPI-BUFFER-SEGLOOP");
        int index = 0;
        for (final Iterator<DataArea> i = inputList.iterator(); i.hasNext();) {
            final DataArea inputDataAreaTemp = i.next();
            inputDataAreaTemp.put("TPI-SEL", WipsConstant.BLANK_SPACE_1);
            inputDataAreaTemp.put("TPI-TEXT", outputFromDataAreaList.get(index));
            index++;
        }
        DataArea dataArea;
        if (WipsConstant.CLEAR_REMARKS.equals(g56xInput.getActionTaken())) {
            final List<DataArea> inputDocIndList =
                TransactionOutputUtil.getArray(inputDataArea, "TPI-BUFFER-SEGLOOP");
            for (final Iterator<DataArea> i = inputDocIndList.iterator(); i.hasNext();) {
                dataArea = i.next();
                dataArea.put("TPI-SEL", WipsConstant.BLANK_SPACE_1);
                dataArea.put("TPI-TEXT", StringUtil.createBlankSpaces(72));
            }
        }

    }

    public BiConsumerThrowsException<AtpRemarksOutput, DataArea, ConnectorException> loadG56xInputSystemRemarks(
        final AtpRequest atpRequest) {
        return new BiConsumerThrowsException<AtpRemarksOutput, DataArea, ConnectorException>() {

            @Override
            public void accept(final AtpRemarksOutput atpRemarksOutput, final DataArea inputDataArea)
                throws ConnectorException {

                final G56xTransactionInput g56xInput = atpRemarksOutput.getG56xInput();
                g56xInput.setTpiPageNo(g56xInput.getTpiPageNo());
                g56xInput.setTpiIoindic(WipsConstant.InputI);
                g56xInput.setTpiPfKey(WipsConstant.PFKEY8);
                g56xInput.setLterm(atpRequest.getLterm());

                populate(inputDataArea, g56xInput);
            }
        };
    }

    public BiConsumerThrowsException<AtpRemarksOutput, DataArea, ConnectorException> loadG56xInputForNextUser(
        final AtpRequest atpRequest) {
        return new BiConsumerThrowsException<AtpRemarksOutput, DataArea, ConnectorException>() {

            @Override
            public void accept(final AtpRemarksOutput atpRemarksOutput, final DataArea inputDataArea)
                throws ConnectorException {
                final G56xTransactionInput g56xInput = atpRemarksOutput.getG56xInput();
                g56xInput.setTpiPageNo(g56xInput.getTpiPageNo());
                g56xInput.setTpiIoindic(WipsConstant.InputI);
                g56xInput.setTpiPfKey(
                    atpRemarksOutput.getMoreRemarks().contains("MORE") ? WipsConstant.PFKEY8 : WipsConstant.PFKEY16);
                g56xInput.setLterm(atpRequest.getLterm());

                populate(inputDataArea, g56xInput);
            }

        };
    }

    public BiConsumerThrowsException<AtpRemarksOutput, DataArea, ConnectorException> loadG56xInputForNextUserForSave(
        final AtpRequest atpRequest) {
        return new BiConsumerThrowsException<AtpRemarksOutput, DataArea, ConnectorException>() {

            @Override
            public void accept(final AtpRemarksOutput atpRemarksOutput, final DataArea inputDataArea)
                throws ConnectorException {
                final G56xTransactionInput g56xInput = atpRemarksOutput.getG56xInput();
                g56xInput.setTpiPageNo(g56xInput.getTpiPageNo());
                g56xInput.setTpiIoindic(WipsConstant.InputI);
                g56xInput.setTpiPfKey(WipsConstant.PFKEY16);
                g56xInput.setLterm(atpRequest.getLterm());

                populate(inputDataArea, g56xInput);
            }

        };
    }

    public BiConsumerThrowsException<AtpRemarksOutput, DataArea, ConnectorException> loadG56xInputForClearUserRemarks(
        final AtpRemarksRequest atpRemarksRequest) {
        return new BiConsumerThrowsException<AtpRemarksOutput, DataArea, ConnectorException>() {

            @Override
            public void accept(final AtpRemarksOutput atpRemarksOutput, final DataArea inputDataArea)
                throws ConnectorException {
                final G56xTransactionInput g56xTransactionInput = atpRemarksOutput.getG56xInput();
                g56xTransactionInput.setTpiIoindic(WipsConstant.InputI);
                g56xTransactionInput.setTpiPfKey(WipsConstant.PFKEY0);
                g56xTransactionInput.setLterm(atpRemarksRequest.getLterm());
                g56xTransactionInput.setRemarksText(new ArrayList<String>());
                g56xTransactionInput.setActionTaken(WipsConstant.CLEAR_REMARKS);

                populate(inputDataArea, g56xTransactionInput);
            }

        };

    }

    public BiConsumerThrowsException<AtpRemarksOutput, DataArea, ConnectorException> loadG56xInputForSaveUserRemarks(
        final AtpRemarksRequest atpRemarksRequest, final List<String> userRemarksToSave, final int linesAlreadyProcessed) {
        return new BiConsumerThrowsException<AtpRemarksOutput, DataArea, ConnectorException>() {

            @Override
            public void accept(final AtpRemarksOutput output, final DataArea inputDataArea) throws ConnectorException {
                final G56xTransactionInput g56xInput = output.getG56xInput();
                g56xInput.setActionTaken(WipsConstant.SAVE_REMARKS);
                g56xInput.setTpiPfKey(WipsConstant.PFKEY0);
                final List<DataArea> outputLoop = g56xInput.getBufferedSegLoop();
                int lineNumber = linesAlreadyProcessed;
                for (final DataArea dataArea : outputLoop) {
                    if (lineNumber < userRemarksToSave.size()) {
                        dataArea.put(TPO_TEXT, userRemarksToSave.get(lineNumber)
                            .toString());
                    } else {
                        dataArea.put(TPO_TEXT,
                            StringUtil.createBlankSpaces(72));
                    }
                    lineNumber++;
                }
                g56xInput.setTpiPageNo(output.getPageNo());
                g56xInput.setLterm(atpRemarksRequest.getLterm());
                populate(inputDataArea, g56xInput);
            }
        };
    }

    public BiConsumerThrowsException<AtpRemarksOutput, DataArea, ConnectorException> loadG56xInputApproveOrReject(
        final AtpRemarksRequest atpRemarksRequest, final AtpRemarksOutput g56xInputHolder) {
        return new BiConsumerThrowsException<AtpRemarksOutput, DataArea, ConnectorException>() {

            @Override
            public void accept(final AtpRemarksOutput output, final DataArea inputDataArea) throws ConnectorException {
                G56xTransactionInput g56xInput = output.getG56xInput();
                if (output.getG56xInput() == null) {
                    g56xInput = g56xInputHolder.getG56xInput();
                } else {
                    g56xInputHolder.setG56xInput(g56xInput);
                }
                g56xInput.setTpiIoindic(WipsConstant.InputI);
                g56xInput.setTpiPfKey(WipsConstant.PFKEY18);
                g56xInput.setLterm(atpRemarksRequest.getLterm());
                g56xInput.setActionTaken(atpRemarksRequest.getActionTaken());
                if (atpRemarksRequest.getActionTaken().contains(APPROVE)
                    || atpRemarksRequest.getActionTaken().contains(CONFIRM))
                    g56xInput.setTpiPfKey(WipsConstant.PFKEY13);
                else
                    g56xInput.setTpiPfKey(WipsConstant.PFKEY14);
                populate(inputDataArea, g56xInput);
            }
        };
    }

    public BiConsumerThrowsException<AtpRemarksOutput, DataArea, ConnectorException> loadG56xInputForNextPageUserRemarks(
        final AtpRemarksRequest atpRemarksRequest) {
        return new BiConsumerThrowsException<AtpRemarksOutput, DataArea, ConnectorException>() {

            @Override
            public void accept(final AtpRemarksOutput output, final DataArea inputDataArea) throws ConnectorException {
                final G56xTransactionInput g56xInput = output.getG56xInput();
                g56xInput.setActionTaken(WipsConstant.SAVE_REMARKS);
                g56xInput.setTpiPfKey(WipsConstant.PFKEY8);
                g56xInput.setTpiPageNo(output.getPageNo());
                g56xInput.setLterm(atpRemarksRequest.getLterm());
                populate(inputDataArea, g56xInput);
            }
        };
    }

}
