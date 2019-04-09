//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.lumpsum.connector;

import javax.inject.Inject;
import javax.resource.ResourceException;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.ims.ImsOperation;
import com.ford.it.connector.record.DataArea;
import com.ford.it.logging.ILogger;
import com.ford.it.logging.LogFactory;
import com.ford.purchasing.wips.common.layer.WipsImsInput;
import com.ford.purchasing.wips.common.layer.WipsImsOutput;
import com.ford.purchasing.wips.common.layer.exception.WipsImsTransactionException;
import com.ford.purchasing.wips.common.layer.util.TransactionOutputUtil;
import com.ford.purchasing.wips.domain.connector.WipsTransactionConnector;
import com.ford.purchasing.wips.domain.layer.WipsTransactionConstant;
import com.ford.purchasing.wips.domain.lumpsum.beans.C33uTransactionOutput;

@SuppressWarnings("javadoc")
public class C32uC34uTransaction extends WipsTransactionConnector {
    private static final String CLASS_NAME = C32uC34uTransaction.class.getName();
    private static ILogger log = LogFactory.getInstance().getLogger(CLASS_NAME);
    private static final String C34U_TRANSACTION = "AAIMC32U-C34U";
    @Inject
    private C32uTransaction c32uTransaction;

    @Override
    protected void populateImsTransactionInput(final WipsImsInput wipsImsInput,
            final DataArea inputDataArea) throws ConnectorException {
        this.c32uTransaction.populateImsTransactionInput(wipsImsInput, inputDataArea);
    }

    @Override
    protected String getImsTransactionName() {
        return C34U_TRANSACTION;
    }

    @Override
    protected WipsImsOutput populateImsTransactionOutput(final ImsOperation imsOperation)
            throws ResourceException, ConnectorException, WipsImsTransactionException {

        final String rawOutput = imsOperation.getRawOutput();
        log.info("c33u rawoutput" + rawOutput);
        final C33uTransactionOutput c33uTransactionOutput = null;
        String transactionName = null;
        final DataArea outputDataArea = (DataArea)imsOperation.getOutputRecord();
        transactionName = TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-PGMNAME");
        if (!WipsTransactionConstant.C34U_TRANSACTION_NAME.equals(transactionName)) {
            throw new WipsImsTransactionException(getImsTransactionName(),
                    imsOperation.getRawOutput().substring(38, 114));
        }

        return c33uTransactionOutput;

    }

}
