//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.atp.beans;

import java.util.ArrayList;
import java.util.List;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.atp.ApproveWarningHandler;
import com.ford.purchasing.wips.common.atp.AtpRemark;
import com.ford.purchasing.wips.common.connector.BiFunctionThrowsException;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.WipsImsOutput;
import com.ford.purchasing.wips.common.layer.exception.WipsImsTransactionException;
import com.ford.purchasing.wips.common.layer.util.TransactionOutputUtil;
import com.ford.purchasing.wips.common.layer.util.WipsUtil;
import com.ford.purchasing.wips.domain.layer.WipsTransactionConstant;

@SuppressWarnings("javadoc")
public class AtpRemarksOutput extends WipsImsOutput {

    public static final String TPO_PROGRAM = "TP-OUTPUT-BUFFER-FIELDS.TPO-PROGRAM";

    private List<AtpRemark> remark;
    private String moreRemarks;
    private String partNumber;
    private String buyerCode;
    private String engineeringLevel;
    private String pageNo;
    private String atpNumber;
    private boolean remarksSuccessfullySaved = false;
    private G56xTransactionInput g56xInput;
    private List<String> confirmMessage;
    private String actionTaken;
    private int linesProcessed = 0;

    public boolean isRemarksSuccessfullySaved() {
        return this.remarksSuccessfullySaved;
    }

    public void setRemarksSuccessfullySaved(final boolean remarksSuccessfullySaved) {
        this.remarksSuccessfullySaved = remarksSuccessfullySaved;
    }

    public String getAtpNumber() {
        return this.atpNumber;
    }

    public void setAtpNumber(final String atpNumber) {
        this.atpNumber = atpNumber;
    }

    public String getPageNo() {
        return this.pageNo;
    }

    public void setPageNo(final String pageNo) {
        this.pageNo = pageNo;
    }

    public List<AtpRemark> getRemark() {
        return this.remark;
    }

    public void setRemark(final List<AtpRemark> remark) {
        this.remark = remark;
    }

    public String getMoreRemarks() {
        return this.moreRemarks;
    }

    public void setMoreRemarks(final String moreRemarks) {
        this.moreRemarks = moreRemarks;
    }

    public G56xTransactionInput getG56xInput() {
        return this.g56xInput;
    }

    public void setG56xInput(final G56xTransactionInput g56xInput) {
        this.g56xInput = g56xInput;
    }

    public String getPartNumber() {
        return this.partNumber;
    }

    public void setPartNumber(final String partNumber) {
        this.partNumber = partNumber;
    }

    public String getBuyerCode() {
        return this.buyerCode;
    }

    public void setBuyerCode(final String buyerCode) {
        this.buyerCode = buyerCode;
    }

    public String getEngineeringLevel() {
        return this.engineeringLevel;
    }

    public void setEngineeringLevel(final String engineeringLevel) {
        this.engineeringLevel = engineeringLevel;
    }

    public List<String> getConfirmMessage() {
        return this.confirmMessage;
    }

    public void setConfirmMessage(final List<String> confirmMessage) {
        this.confirmMessage = confirmMessage;
    }

    public String getActionTaken() {
        return this.actionTaken;
    }

    public void setActionTaken(final String actionTaken) {
        this.actionTaken = actionTaken;
    }

    public int linesProcessed() {
        return this.linesProcessed;
    }

    public void increaseLinesProcessed(final int linesProcessed) {
        this.linesProcessed += linesProcessed;
    }

    public BiFunctionThrowsException<DataArea, String, AtpRemarksOutput, Exception> populateAtpRemarksDetails(
        final List<AtpRemark> systemRemarks) {
        return new BiFunctionThrowsException<DataArea, String, AtpRemarksOutput, Exception>() {

            @Override
            public AtpRemarksOutput apply(final DataArea outputDataArea, final String rawOutput) throws Exception {
                final String transactionName = TransactionOutputUtil.getString(outputDataArea, TPO_PROGRAM);
                if (WipsTransactionConstant.G56X_TRANSACTION_NAME.contains(transactionName)) {
                    final AtpRecapHelper atpRecapHelper = new AtpRecapHelper();
                    systemRemarks.addAll(atpRecapHelper.getAtpRemarks(outputDataArea));
                    setMoreRemarks(atpRecapHelper.getMorePages(outputDataArea));
                    setPartNumber(atpRecapHelper.getPartNumber(outputDataArea));
                    setBuyerCode(TransactionOutputUtil.getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-BUYER"));
                    setEngineeringLevel(
                            TransactionOutputUtil.getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-ENGLVL"));
                    setG56xInput(atpRecapHelper.populateG56xInput(outputDataArea));
                } else {
                    throw new WipsImsTransactionException(WipsTransactionConstant.G56X_TRANSACTION_NAME,
                            rawOutput.substring(94, 170));
                }
                return AtpRemarksOutput.this;
            }

        };
    }

    public BiFunctionThrowsException<DataArea, String, AtpRemarksOutput, Exception> populateMoreSystemRemarks(
            final List<AtpRemark> systemRemarks) {
        return new BiFunctionThrowsException<DataArea, String, AtpRemarksOutput, Exception>() {

            @Override
            public AtpRemarksOutput apply(final DataArea outputDataArea, final String rawOutput) throws Exception {
                final String transactionName = TransactionOutputUtil.getString(outputDataArea, TPO_PROGRAM);
                final AtpRemarksOutput atpRemarksOutput = new AtpRemarksOutput();
                if (WipsTransactionConstant.G56X_TRANSACTION_NAME.contains(transactionName)) {
                    final AtpRecapHelper atpRecapHelper = new AtpRecapHelper();
                    systemRemarks.get(0)
                        .getRemarks()
                        .addAll(atpRecapHelper.getAtpRemarks(outputDataArea).get(0).getRemarks());
                    atpRemarksOutput.setMoreRemarks(atpRecapHelper.getMorePages(outputDataArea));
                    atpRemarksOutput.setPartNumber(atpRecapHelper.getPartNumber(outputDataArea));
                    atpRemarksOutput.setBuyerCode(
                            TransactionOutputUtil.getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-BUYER"));
                    atpRemarksOutput.setEngineeringLevel(
                            TransactionOutputUtil.getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-ENGLVL"));
                    atpRemarksOutput.setG56xInput(atpRecapHelper.populateG56xInput(outputDataArea));
                } else {
                    throw new WipsImsTransactionException(WipsTransactionConstant.G56X_TRANSACTION_NAME,
                            rawOutput.substring(94, 170));
                }
                return atpRemarksOutput;
            }

        };
    }

    public BiFunctionThrowsException<DataArea, String, AtpRemarksOutput, Exception> populateUserRemarks(
            final List<AtpRemark> userRemarks) {

        return new BiFunctionThrowsException<DataArea, String, AtpRemarksOutput, Exception>() {

            @Override
            public AtpRemarksOutput apply(final DataArea outputDataArea, final String rawOutput) throws Exception {
                final String transactionName = TransactionOutputUtil.getString(outputDataArea, TPO_PROGRAM);
                final AtpRemarksOutput atpRemarks = new AtpRemarksOutput();
                if (WipsTransactionConstant.G56X_TRANSACTION_NAME.contains(transactionName)) {
                    final AtpRecapHelper atpRecapHelper = new AtpRecapHelper();
                    atpRemarks.setMoreRemarks(atpRecapHelper.getMorePages(outputDataArea));
                    atpRemarks.setPageNo(
                            TransactionOutputUtil.getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-PAGENO-CHAR"));
                    if (notErrorCondition(outputDataArea, atpRecapHelper.getJobCode(outputDataArea))) {
                        final List<AtpRemark> remarks = atpRecapHelper.getAtpRemarks(outputDataArea);
                        if (userRemarks.size() > 0) {
                            userRemarks.get(0).getRemarks().addAll(remarks.get(0).getRemarks());
                        } else {
                            userRemarks.addAll(remarks);
                        }
                        atpRemarks.setRemark(remarks);
                    }
                    atpRemarks.setG56xInput(atpRecapHelper.populateG56xInput(outputDataArea));
                } else {
                    throw new WipsImsTransactionException(WipsTransactionConstant.G56X_TRANSACTION,
                            rawOutput.substring(108, 184));
                }
                return atpRemarks;
            }
        };
    }

    private boolean notErrorCondition(final DataArea outputDataArea, final String jobCode) throws ConnectorException {
        final String errorMessage = TransactionOutputUtil.getString(outputDataArea,
            "TP-OUTPUT-BUFFER-FIELDS.TPO-ERRMSG1");
        return !errorMessage.contains("1095") && !errorMessage.contains("3315") && !jobCode.contains("***");
    }

    public BiFunctionThrowsException<DataArea, String, AtpRemarksOutput, Exception> populateAtpRemarksDetailsForSave() {
        return new BiFunctionThrowsException<DataArea, String, AtpRemarksOutput, Exception>() {

            @Override
            public AtpRemarksOutput apply(final DataArea outputDataArea, final String rawOutput) throws Exception {
                final String transactionName = TransactionOutputUtil.getString(outputDataArea, TPO_PROGRAM);
                final AtpRemarksOutput atpRemarks = new AtpRemarksOutput();
                if (WipsTransactionConstant.G56X_TRANSACTION_NAME.contains(transactionName)) {
                    final AtpRecapHelper atpRecapHelper = new AtpRecapHelper();
                    atpRemarks.setG56xInput(atpRecapHelper.populateG56xInput(outputDataArea));
                    atpRemarks.setRemark(atpRecapHelper.getAtpRemarks(outputDataArea));
                    atpRemarks.setMoreRemarks(atpRecapHelper.getMorePages(outputDataArea));
                } else {
                    throw new WipsImsTransactionException(WipsTransactionConstant.G53X_G56X_TRANSACTION,
                            rawOutput.substring(94, 170));
                }
                return atpRemarks;
            }

        };
    }

    public BiFunctionThrowsException<DataArea, String, AtpRemarksOutput, Exception> populateUserRemarksForSave() {

        return new BiFunctionThrowsException<DataArea, String, AtpRemarksOutput, Exception>() {

            @Override
            public AtpRemarksOutput apply(final DataArea outputDataArea, final String rawOutput) throws Exception {
                final String transactionName = TransactionOutputUtil.getString(outputDataArea, TPO_PROGRAM);
                final AtpRemarksOutput atpRemarks = new AtpRemarksOutput();
                if (WipsTransactionConstant.G56X_TRANSACTION_NAME.contains(transactionName)) {
                    final AtpRecapHelper atpRecapHelper = new AtpRecapHelper();
                    atpRemarks.setMoreRemarks(atpRecapHelper.getMorePages(outputDataArea));
                    atpRemarks.setPageNo(TransactionOutputUtil.getString(outputDataArea,
                        "TP-OUTPUT-BUFFER-FIELDS.TPO-PAGENO-CHAR"));
                    if (notErrorCondition(outputDataArea, atpRecapHelper.getJobCode(outputDataArea))) {
                        atpRemarks.setRemark(atpRecapHelper.getAtpRemarks(outputDataArea));
                    }
                    atpRemarks.setG56xInput(atpRecapHelper.populateG56xInput(outputDataArea));
                } else {
                    throw new WipsImsTransactionException(WipsTransactionConstant.G56X_TRANSACTION,
                            rawOutput.substring(108, 184));
                }
                return atpRemarks;
            }
        };
    }

    public BiFunctionThrowsException<DataArea, String, AtpRemarksOutput, Exception> populateSavedRemarks(
            final List<String> savedRemarksFromIms) {
        return new BiFunctionThrowsException<DataArea, String, AtpRemarksOutput, Exception>() {

            @Override
            public AtpRemarksOutput apply(final DataArea outputDataArea, final String rawOutptu) throws Exception {

                final AtpRemarksOutput output = new AtpRemarksOutput();
                final String transactionName = TransactionOutputUtil.getString(outputDataArea, TPO_PROGRAM);
                if (WipsTransactionConstant.G56X_TRANSACTION_NAME.contains(transactionName)) {
                    final AtpRecapHelper atpRecapHelper = new AtpRecapHelper();
                    output.setMoreRemarks(atpRecapHelper.getMorePages(outputDataArea));
                    output.setPageNo(
                            TransactionOutputUtil.getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-PAGENO-CHAR"));
                    if (notErrorCondition(outputDataArea, atpRecapHelper.getJobCode(outputDataArea))) {
                        final List<AtpRemark> atpRemarks = atpRecapHelper.getAtpRemarks(outputDataArea);
                        output.setRemark(atpRemarks);
                        savedRemarksFromIms.addAll(output.getRemark().get(0).getRemarks());
                    }

                    output.increaseLinesProcessed(savedRemarksFromIms.size());
                    output.setG56xInput(atpRecapHelper.populateG56xInput(outputDataArea));
                } else {
                    throw new WipsImsTransactionException(WipsTransactionConstant.G56X_TRANSACTION_NAME,
                            rawOutptu.substring(108, 184));
                }
                output.setRemarksSuccessfullySaved(true);
                return output;
            }
        };
    }

    public BiFunctionThrowsException<DataArea, String, AtpRemarksOutput, Exception> populate(
            final List<ApproveWarningHandler> warningMessages) {
        return new BiFunctionThrowsException<DataArea, String, AtpRemarksOutput, Exception>() {

            @Override
            public AtpRemarksOutput apply(final DataArea outputDataArea, final String rawOutput) throws Exception {
                final AtpRemarksOutput transactionOutput = new AtpRemarksOutput();
                if (rawOutput.length() != 121) {
                    final List<String> message = new ArrayList<String>();
                    final String errorMessage = rawOutput.substring(38, 114);
                    message.add(errorMessage);
                    transactionOutput.setConfirmMessage(message);
                    transactionOutput.setErrorMessage(errorMessage);
                    if (containsConfirmationErrorMessages(errorMessage)) {
                        transactionOutput.setActionTaken(WipsConstant.APPROVE);
                    } else if (containsRejectionErrorMessage(errorMessage)) {
                        transactionOutput.setActionTaken(WipsConstant.REJECT_ATP);
                    } else {
                        throw new WipsImsTransactionException(WipsTransactionConstant.AAIMG56X_S01I0,
                            rawOutput.substring(63, 139));
                    }
                    if (transactionOutput.getErrorMessage() != null) {
                        warningMessages.add(WipsUtil.populateWarningMessages(transactionOutput.getErrorMessage(),
                            transactionOutput.getActionTaken()));
                    }
                }

                return transactionOutput;
            }

        };

    }

    private boolean containsRejectionErrorMessage(final String errorMessage) {
        return errorMessage.contains("MSG-4499") || errorMessage.contains("MSG-4628");
    }

    private boolean containsConfirmationErrorMessages(final String errorMessage) {
        return errorMessage.contains("MSG-4276") || errorMessage.contains("MSG-4236")
                || errorMessage.contains("MSG-4524");
    }

}
