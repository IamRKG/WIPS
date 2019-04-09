package com.ford.purchasing.wips.domain.lumpsum.connector;

import javax.inject.Inject;
import javax.resource.ResourceException;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.ims.ImsOperation;
import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.layer.WipsImsInput;
import com.ford.purchasing.wips.common.layer.WipsImsOutput;
import com.ford.purchasing.wips.common.layer.exception.WipsImsTransactionException;
import com.ford.purchasing.wips.domain.atp.connector.Q01xTransaction;
import com.ford.purchasing.wips.domain.connector.WipsTransactionConnector;

@SuppressWarnings("javadoc")
public class Q01xC32uTransaction extends WipsTransactionConnector {
    private static final String Q01X_C32U_TRANSACTION = "AAIMQ01X-C32U";

    @Inject
    private Q01xTransaction q01xTransaction;

    @Inject
    private C32uTransaction c32uTransaction;

    @Override
    protected void populateImsTransactionInput(final WipsImsInput wipsImsInput,
            final DataArea inputDataArea) throws ConnectorException {
        this.q01xTransaction.populateImsTransactionInput(wipsImsInput, inputDataArea);
    }

    @Override
    protected String getImsTransactionName() {
        return Q01X_C32U_TRANSACTION;
    }

    @Override
    protected WipsImsOutput populateImsTransactionOutput(final ImsOperation imsoperation)
            throws ResourceException, ConnectorException, WipsImsTransactionException {
        return this.c32uTransaction.populateImsTransactionOutput(imsoperation);
    }

}
