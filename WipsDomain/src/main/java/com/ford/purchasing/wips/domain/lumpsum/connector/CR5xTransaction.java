package com.ford.purchasing.wips.domain.lumpsum.connector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.resource.ResourceException;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.ims.ImsConversation;
import com.ford.it.connector.ims.ImsOperation;
import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.layer.ApprovalDetail;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.WipsImsInput;
import com.ford.purchasing.wips.common.layer.WipsImsOutput;
import com.ford.purchasing.wips.common.layer.exception.WipsImsTransactionException;
import com.ford.purchasing.wips.common.layer.util.StringUtil;
import com.ford.purchasing.wips.common.layer.util.TransactionOutputUtil;
import com.ford.purchasing.wips.common.lumpsum.WipsImsCR5xInput;
import com.ford.purchasing.wips.domain.connector.WipsTransactionConnector;
import com.ford.purchasing.wips.domain.layer.WipsLumsumHelper;
import com.ford.purchasing.wips.domain.layer.exception.WipsImsInterfaceException;
import com.ford.purchasing.wips.domain.lumpsum.beans.CR5xTransactionOutput;

@SuppressWarnings("javadoc")
public class CR5xTransaction extends WipsTransactionConnector {

    private static final String CR5X_TRANSACTION = "AAIMCR5X";
    private short pfKey;
    private String actionTaken;
    private List<DataArea> bufferedList = new ArrayList<DataArea>();
    private List<String> allCR5xUserRemarks = new ArrayList<String>();

    @Override
    protected void populateImsTransactionInput(final WipsImsInput wipsImsInput, DataArea inputDataArea)
            throws ConnectorException {
        final WipsImsCR5xInput wipsImsCR5xInput = (WipsImsCR5xInput) wipsImsInput;
        inputDataArea.put("TPI-IOINDIC", wipsImsCR5xInput.getTpiIoindic());
        inputDataArea.put("TPI-PFKEY", this.pfKey);
        final List<DataArea> inputDocList = this.bufferedList;
        final List<String> outputFromDataAreaList = new ArrayList<String>();
        for (final Iterator<DataArea> i = inputDocList.iterator(); i.hasNext();) {
            final DataArea inputDataAreaTemp = i.next();
            outputFromDataAreaList.add(TransactionOutputUtil.getString(inputDataAreaTemp, "TPO-APPRMKS"));
        }
        final List<DataArea> inputList = TransactionOutputUtil.getArray(inputDataArea, "TPI-BUFFER-SEGLOOP");
        int index = 0;
        for (final Iterator<DataArea> i = inputList.iterator(); i.hasNext();) {
            final DataArea inputDataAreaTemp = i.next();
            inputDataAreaTemp.put("TPI-APPRMKS", outputFromDataAreaList.get(index));
            index++;
        }
        if (WipsConstant.CLEAR_REMARKS.equals(this.actionTaken)) {
            final List<DataArea> inputDocIndList = TransactionOutputUtil.getArray(inputDataArea, "TPI-BUFFER-SEGLOOP");
            for (final Iterator<DataArea> i = inputDocIndList.iterator(); i.hasNext();) {
                final DataArea inpDataArea = i.next();
                inpDataArea.put("TPI-APPRMKS", StringUtil.createBlankSpaces(72));
            }
        }
        this.bufferedList = wipsImsCR5xInput.getBufferedSegLoop();
    }

    @Override
    public WipsImsOutput executeWipsImsTransaction(final WipsImsInput wipsImsInput,
            final ImsConversation imsConversation) throws WipsImsInterfaceException, WipsImsTransactionException {
        CR5xTransactionOutput cR5xTransactionOutput;
        final WipsImsCR5xInput wipsImsCR5xInput = (WipsImsCR5xInput) wipsImsInput;
        this.actionTaken = wipsImsCR5xInput.getActionTaken();
        this.bufferedList = wipsImsCR5xInput.getBufferedSegLoop();
        this.pfKey = wipsImsCR5xInput.getTpiPfKey();
        do {
            cR5xTransactionOutput = (CR5xTransactionOutput) super.executeWipsImsTransaction(wipsImsInput,
                    imsConversation);
            this.bufferedList = cR5xTransactionOutput.getWipsImsCR5xInput().getBufferedSegLoop();
            this.pfKey = cR5xTransactionOutput.getWipsImsCR5xInput().getTpiPfKey();
            if (WipsConstant.CLEAR_REMARKS.equals(this.actionTaken)) {
                this.pfKey = 0;
            }
        } while (cR5xTransactionOutput.getHasMorePages().contains(WipsConstant.MORE_PAGES)
                && !(WipsConstant.SAVE_REMARKS.equals(this.actionTaken)));
        return cR5xTransactionOutput;
    }

    @Override
    protected WipsImsOutput populateImsTransactionOutput(final ImsOperation imsOperation)
            throws ResourceException, ConnectorException, WipsImsTransactionException {
        final CR5xTransactionOutput cR5xTransactionOutput = new CR5xTransactionOutput();
        final DataArea outputDataArea = (DataArea) imsOperation.getOutputRecord();
        String errorMessage = null;
        boolean nextApprover = false;
        final List<ApprovalDetail> remainingRemarks = new ArrayList<ApprovalDetail>();
        if (outputDataArea != null) {
            errorMessage = TransactionOutputUtil.getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-ERRMSG1");
            cR5xTransactionOutput.setHasMorePages("");
        }
        // check output length for remarks and process
        final WipsLumsumHelper helper = new WipsLumsumHelper();
        if (imsOperation.getRawOutput().length() == WipsConstant.CR5X_RAWOUTPUT_LENGTH
                && !errorMessage.contains("MSG-1283")) {
            final ApprovalDetail approvalDetail = new ApprovalDetail();
            approvalDetail.setMoreRemarksPage(helper.morePages(outputDataArea));
            cR5xTransactionOutput.setHasMorePages(approvalDetail.getMoreRemarksPage());
            if (!(WipsConstant.CLEAR_REMARKS.equals(this.actionTaken)
                    || WipsConstant.SAVE_REMARKS_NEXT_PAGE.equals(this.actionTaken)
                    || WipsConstant.SAVE_LS_REMARKS.equals(this.actionTaken))) {
                this.allCR5xUserRemarks.addAll(TransactionOutputUtil.getListOfValues(outputDataArea, "TPO-APPRMKS"));
                approvalDetail.setRemarks(this.allCR5xUserRemarks);

                if (cR5xTransactionOutput.getHasMorePages().isEmpty()
                        && !WipsConstant.CLEAR_REMARKS.equals(this.actionTaken)) {
                    cR5xTransactionOutput.setWipsImsCR5xInput(helper.generateCR5xInputPF14(outputDataArea));
                    nextApprover = true;
                } else {
                    nextApprover = false;
                    cR5xTransactionOutput.setWipsImsCR5xInput(helper.generateCR5xInputPF8(outputDataArea));
                }
                if (nextApprover) {
                    cR5xTransactionOutput.setCheckMorePagesFlag(true);
                    this.allCR5xUserRemarks = new ArrayList<String>();
                }
            }
            if (WipsConstant.SAVE_LS_REMARKS.equals(this.actionTaken)) {
                cR5xTransactionOutput.setHasMorePages("");
                cR5xTransactionOutput.setWipsImsCR5xInput(helper.generateCR5xInputPF14(outputDataArea));
            }
            if (WipsConstant.SAVE_REMARKS_NEXT_PAGE.equals(this.actionTaken)
                    || WipsConstant.CLEAR_REMARKS.equals(this.actionTaken)) {
                cR5xTransactionOutput.setWipsImsCR5xInput(helper.generateCR5xInputPF8(outputDataArea));
            }
            approvalDetail.setEnterUser(helper.getEnteredUser(outputDataArea));
            approvalDetail.setJobCode(helper.getJobCode(outputDataArea));
            remainingRemarks.add(approvalDetail);
            cR5xTransactionOutput.setApprovalDetails(remainingRemarks);
            cR5xTransactionOutput.setErrorMessage(errorMessage);
        } else {
            cR5xTransactionOutput.setErrorMessage(errorMessage);
            if (outputDataArea != null) {
                cR5xTransactionOutput.setWipsImsCR5xInput(helper.generateCR5xInputPF14(outputDataArea));
            } else {
                throw new WipsImsTransactionException(getImsTransactionName(), "Unable to process it.");
            }
        }
        return cR5xTransactionOutput;
    }

    @Override
    protected String getImsTransactionName() {
        return CR5X_TRANSACTION;
    }
}
