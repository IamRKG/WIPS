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
import com.ford.purchasing.wips.domain.atp.beans.M00mTransactionOutput;
import com.ford.purchasing.wips.domain.connector.WipsTransactionConnector;
import com.ford.purchasing.wips.domain.layer.WipsTransactionConstant;

public class C32uQ01xTransaction extends WipsTransactionConnector {

    private static final String C32U_Q01X_TRANSACTION = "AAIMC32U-Q01X";
    private static final String CLASS_NAME = C32uQ01xTransaction.class.getName();
    private static ILogger log = LogFactory.getInstance().getLogger(CLASS_NAME);

    @Inject
    private C32uTransaction c32uTransaction;

    @Override
    protected void populateImsTransactionInput(final WipsImsInput wipsImsInput,
            final DataArea inputDataArea) throws ConnectorException {
        this.c32uTransaction.populateImsTransactionInput(wipsImsInput, inputDataArea);
    }

    @Override
    protected String getImsTransactionName() {
        return C32U_Q01X_TRANSACTION;
    }

    @Override
    protected WipsImsOutput populateImsTransactionOutput(final ImsOperation imsOperation)
            throws ResourceException, ConnectorException, WipsImsTransactionException {
        log.info("M00M output" + imsOperation.getRawOutput());
        final DataArea outputDataArea = (DataArea)imsOperation.getOutputRecord();
        final String transactionName = TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-PGMNAME");
        if (!WipsTransactionConstant.Q01X_TRANSACTION_NAME.equals(transactionName)) {
            throw new WipsImsTransactionException(getImsTransactionName(),
                    imsOperation.getRawOutput().substring(62, 113));
        }
        return new M00mTransactionOutput();
    }

}
