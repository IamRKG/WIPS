//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.atp.connector;

import javax.resource.ResourceException;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.ims.ImsOperation;
import com.ford.it.connector.record.DataArea;
import com.ford.it.logging.ILogger;
import com.ford.it.logging.LogFactory;
import com.ford.purchasing.wips.common.atp.S01i1TransactionOutput;
import com.ford.purchasing.wips.common.layer.WipsImsInput;
import com.ford.purchasing.wips.common.layer.WipsImsOutput;
import com.ford.purchasing.wips.common.layer.exception.WipsImsTransactionException;
import com.ford.purchasing.wips.domain.connector.WipsTransactionConnector;

@SuppressWarnings("javadoc")
public class S01i0S01i1Transaction extends WipsTransactionConnector {

    private static final String CLASS_NAME = S01i0S01i1Transaction.class.getName();
    private static final String S01I0_TRANSACTION = "AAIMS010-S011";
    private static ILogger log = LogFactory.getInstance().getLogger(CLASS_NAME);

    @Override
    protected void populateImsTransactionInput(final WipsImsInput wipsImsInput,
            final DataArea inputDataArea) throws ConnectorException {
        inputDataArea.put("TPI-PFKEY", wipsImsInput.getTpiPfKey());
    }

    @Override
    protected WipsImsOutput populateImsTransactionOutput(final ImsOperation imsOperation)
            throws ResourceException, ConnectorException, WipsImsTransactionException {
        final String rawOutputS01i0X = imsOperation.getRawOutput();
        log.info("q01x output" + rawOutputS01i0X);
        final S01i1TransactionOutput transactionOutput = new S01i1TransactionOutput();
        if (rawOutputS01i0X.length() == 1616) {
            transactionOutput.setConfirmMessage(rawOutputS01i0X.substring(62, 138));
            transactionOutput.setErrorFlag(false);
        } else {
            throw new WipsImsTransactionException(getImsTransactionName(),
                    rawOutputS01i0X.substring(38, 113));
        }
        return transactionOutput;
    }

    @Override
    protected String getImsTransactionName() {
        return S01I0_TRANSACTION;
    }
}
