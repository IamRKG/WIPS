//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.atp.connector;

import javax.inject.Inject;
import javax.resource.ResourceException;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.ims.ImsOperation;
import com.ford.it.connector.record.DataArea;
import com.ford.it.logging.ILogger;
import com.ford.it.logging.LogFactory;
import com.ford.purchasing.wips.common.layer.Category;
import com.ford.purchasing.wips.common.layer.WipsImsInput;
import com.ford.purchasing.wips.common.layer.WipsImsOutput;
import com.ford.purchasing.wips.common.layer.exception.WipsImsTransactionException;
import com.ford.purchasing.wips.common.layer.util.TransactionOutputUtil;
import com.ford.purchasing.wips.domain.atp.beans.Q01xTransactionOutput;
import com.ford.purchasing.wips.domain.connector.WipsTransactionConnector;
import com.ford.purchasing.wips.domain.layer.PendingApprItemsHelper;
import com.ford.purchasing.wips.domain.layer.WipsTransactionConstant;

public class M00mQ01xTransaction extends WipsTransactionConnector {

    private static final String CLASS_NAME = M00mQ01xTransaction.class.getName();
    private static ILogger log = LogFactory.getInstance().getLogger(CLASS_NAME);
    private static final String M00M_Q01X_TRANSACTION = "AAIMM00M-Q01X";
    @Inject
    private M00mTransaction m00mTransaction;
    private Category expectedCategory;

    @Override
    protected void populateImsTransactionInput(final WipsImsInput wipsImsInput,
            final DataArea inputDataArea) {
        this.m00mTransaction.populateImsTransactionInput(wipsImsInput, inputDataArea);
        this.expectedCategory = Category.getCategory(wipsImsInput.getTpiOption());
    }

    @Override
    public WipsImsOutput populateImsTransactionOutput(final ImsOperation imsOperation)
            throws ResourceException, ConnectorException, WipsImsTransactionException {
        Q01xTransactionOutput q01xTransactionOutputBean = null;
        final String rawOutputM00M = imsOperation.getRawOutput();
        log.info("M00M Q01x output" + rawOutputM00M);
        final DataArea outputDataArea = (DataArea)imsOperation.getOutputRecord();
        final String transactionName = TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-PGMNAME");
        if (WipsTransactionConstant.Q01X_TRANSACTION_NAME.equals(transactionName)) {
            q01xTransactionOutputBean = new Q01xTransactionOutput();
            q01xTransactionOutputBean
                    .setUserJobCode(TransactionOutputUtil.getString(outputDataArea,
                            "TP-OUTPUT-BUFFER-FIELDS.TPO-JOBCODE"));
            q01xTransactionOutputBean
                    .setUserJobName(TransactionOutputUtil.getString(outputDataArea,
                            "TP-OUTPUT-BUFFER-FIELDS.TPO-EMPNAME"));
            q01xTransactionOutputBean.setContainMorePages(TransactionOutputUtil.getString(
                    outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-MORE"));
            q01xTransactionOutputBean
                    .setPageNo(TransactionOutputUtil.getString(outputDataArea,
                            "TP-OUTPUT-BUFFER-FIELDS.TPO-PAGENO"));
            q01xTransactionOutputBean.setTpiCat(TransactionOutputUtil.getString(
                    outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-SELCAT"));
            q01xTransactionOutputBean.setPendingApprItemsList(
                    new PendingApprItemsHelper().populatePendingApprItems(outputDataArea,
                            this.expectedCategory));
            q01xTransactionOutputBean.setErrorFlag(false);
        } else {
            throw new WipsImsTransactionException(getImsTransactionName(),
                    rawOutputM00M.substring(108, 184));
        }
        return q01xTransactionOutputBean;
    }

    @Override
    protected String getImsTransactionName() {
        return M00M_Q01X_TRANSACTION;
    }

}
