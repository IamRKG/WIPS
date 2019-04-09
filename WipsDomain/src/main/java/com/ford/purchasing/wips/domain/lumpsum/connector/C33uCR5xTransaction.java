package com.ford.purchasing.wips.domain.lumpsum.connector;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.resource.ResourceException;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.ims.ImsOperation;
import com.ford.it.connector.record.DataArea;
import com.ford.it.logging.ILogger;
import com.ford.it.logging.LogFactory;
import com.ford.purchasing.wips.common.layer.ApprovalDetail;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.WipsImsInput;
import com.ford.purchasing.wips.common.layer.WipsImsOutput;
import com.ford.purchasing.wips.common.layer.exception.WipsImsTransactionException;
import com.ford.purchasing.wips.common.layer.util.TransactionOutputUtil;
import com.ford.purchasing.wips.common.lumpsum.WipsImsC33uInput;
import com.ford.purchasing.wips.domain.connector.WipsTransactionConnector;
import com.ford.purchasing.wips.domain.layer.WipsLumsumHelper;
import com.ford.purchasing.wips.domain.layer.WipsTransactionConstant;
import com.ford.purchasing.wips.domain.lumpsum.beans.CR5xTransactionOutput;

@SuppressWarnings("javadoc")
public class C33uCR5xTransaction extends WipsTransactionConnector {

    private static final String CLASS_NAME = C33uCR5xTransaction.class.getName();
    private static ILogger log = LogFactory.getInstance().getLogger(CLASS_NAME);
    private static final String C33U_CR5X_TRANSACTION = "AAIMC33U_CR5X";
    @Inject
    private C33uTransaction c33uTransaction;
    private String actionTaken;

    @Override
    protected void populateImsTransactionInput(final WipsImsInput wipsImsInput,
            final DataArea inputDataArea) throws ConnectorException {
        final WipsImsC33uInput wipsImsC33uInput = (WipsImsC33uInput)wipsImsInput;
        this.actionTaken = wipsImsC33uInput.getActionTaken();
        this.c33uTransaction.populateImsTransactionInput(wipsImsInput, inputDataArea);
    }

    @Override
    protected String getImsTransactionName() {
        return C33U_CR5X_TRANSACTION;
    }

    @Override
    protected WipsImsOutput populateImsTransactionOutput(
            final ImsOperation imsOperation) throws ResourceException,
            ConnectorException, WipsImsTransactionException {
        final String rawOutput = imsOperation.getRawOutput();
        log.info("cR5x rawoutput " + rawOutput);
        final CR5xTransactionOutput cR5xTransactionOutput;
        final DataArea outputDataArea = (DataArea)imsOperation.getOutputRecord();
        String transactionName = null;
        transactionName = TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-PROGRAM");
        final WipsLumsumHelper helper = new WipsLumsumHelper();
        if (WipsTransactionConstant.CR5X_TRANSACTION_NAME.equals(transactionName)) {
            cR5xTransactionOutput = new CR5xTransactionOutput();
            final ApprovalDetail approvalDetail = new ApprovalDetail();
            approvalDetail.setEnterUser(helper.getEnteredUser(outputDataArea));
            approvalDetail.setJobCode(helper.getJobCode(outputDataArea));
            approvalDetail.setMoreRemarksPage(helper.morePages(outputDataArea));
            cR5xTransactionOutput.setHasMorePages(approvalDetail.getMoreRemarksPage());
            if (!WipsConstant.SAVE_REMARKS.equals(this.actionTaken)) {
                approvalDetail.setRemarks(
                        TransactionOutputUtil.getListOfValues(outputDataArea, "TPO-APPRMKS"));
            }
            final List<ApprovalDetail> firstScreenRemarks = new ArrayList<ApprovalDetail>();
            firstScreenRemarks.add(approvalDetail);
            cR5xTransactionOutput.setApprovalDetails(firstScreenRemarks);

            if (cR5xTransactionOutput.getHasMorePages().isEmpty()) {
                cR5xTransactionOutput.setCheckMorePagesFlag(true);
                cR5xTransactionOutput
                        .setWipsImsCR5xInput(helper.generateCR5xInputPF14(outputDataArea));
            } else {
                cR5xTransactionOutput
                        .setWipsImsCR5xInput(helper.generateCR5xInputPF8(outputDataArea));
            }
        } else {
            throw new WipsImsTransactionException(getImsTransactionName(),
                    "Unable to process it.");
        }
        return cR5xTransactionOutput;
    }

}
