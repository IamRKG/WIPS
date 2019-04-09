//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.lumpsum.connector;

import javax.resource.ResourceException;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.ims.ImsOperation;
import com.ford.it.connector.record.DataArea;
import com.ford.it.logging.ILogger;
import com.ford.it.logging.LogFactory;
import com.ford.purchasing.wips.common.layer.WipsImsInput;
import com.ford.purchasing.wips.common.layer.WipsImsOutput;
import com.ford.purchasing.wips.common.layer.exception.WipsImsTransactionException;
import com.ford.purchasing.wips.common.lumpsum.C32uTransactionOutput;
import com.ford.purchasing.wips.domain.connector.WipsTransactionConnector;

public class S01OC32UTransaction extends WipsTransactionConnector {

    private static final String S01O_C32U_TRASACTION_CODE = "AAIMS01O-C32U";
    private static final String CLASS_NAME = S01OC32UTransaction.class.getName();
    private static ILogger log = LogFactory.getInstance().getLogger(CLASS_NAME);

    @Override
    protected void populateImsTransactionInput(final WipsImsInput wipsImsInput,
            final DataArea inputDataArea) throws ConnectorException {
        inputDataArea.put("TPI-PFKEY", wipsImsInput.getTpiPfKey());
    }

    @Override
    protected WipsImsOutput populateImsTransactionOutput(final ImsOperation imsOperation)
            throws ResourceException, ConnectorException, WipsImsTransactionException {
        final String rawOutput = imsOperation.getRawOutput();
        log.info("c32u output" + rawOutput);
        final C32uTransactionOutput transactionOutput = new C32uTransactionOutput();
        if (rawOutput.length() == 1326) {
            transactionOutput.setConfirmationMessage(rawOutput.substring(38, 116));
        } else if (rawOutput.length() == 1616) {
            transactionOutput.setConfirmationMessage(rawOutput.substring(62, 144));
        } else {
            throw new WipsImsTransactionException(
                    getImsTransactionName(), rawOutput.substring(38, 113));
        }
        return transactionOutput;
    }

    @Override
    public String getImsTransactionName() {
        return S01O_C32U_TRASACTION_CODE;
    }
}
