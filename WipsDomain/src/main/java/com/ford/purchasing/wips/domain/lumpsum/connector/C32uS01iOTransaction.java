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
import com.ford.purchasing.wips.common.atp.S01i0TransactionOutput;
import com.ford.purchasing.wips.common.layer.WipsImsInput;
import com.ford.purchasing.wips.common.layer.WipsImsOutput;
import com.ford.purchasing.wips.common.layer.exception.WipsImsTransactionException;
import com.ford.purchasing.wips.domain.connector.WipsTransactionConnector;

public class C32uS01iOTransaction extends WipsTransactionConnector {

    private static final String C32U_S01IO_TRANSACTION = "AAIMC32U-S01O";
    private static final String CLASS_NAME = C32uS01iOTransaction.class.getName();
    private static ILogger log = LogFactory.getInstance().getLogger(CLASS_NAME);

    @Inject
    private C32uTransaction c32uTransaction;

    @Override
    protected void populateImsTransactionInput(final WipsImsInput wipsImsInput,
            final DataArea inputDataArea) throws ConnectorException {
        this.c32uTransaction.populateImsTransactionInput(wipsImsInput, inputDataArea);
    }

    @Override
    protected WipsImsOutput populateImsTransactionOutput(final ImsOperation imsOperation)
            throws ResourceException, ConnectorException, WipsImsTransactionException {
        final String rawOutput = imsOperation.getRawOutput();
        log.info("s01io rawoutput" + rawOutput);
        final S01i0TransactionOutput s01ioTransactionOutput = new S01i0TransactionOutput();
        if (imsOperation.getRawOutput().length() != 121) {
            s01ioTransactionOutput.setErrorFlag(true);
            final String errorMessage = rawOutput.substring(38, 116);
            s01ioTransactionOutput.setErrorMessage(errorMessage);
        }
        return s01ioTransactionOutput;
    }

    @Override
    protected String getImsTransactionName() {
        return C32U_S01IO_TRANSACTION;
    }
}
