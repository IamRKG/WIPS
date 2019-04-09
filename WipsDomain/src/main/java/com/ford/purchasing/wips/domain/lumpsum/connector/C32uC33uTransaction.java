package com.ford.purchasing.wips.domain.lumpsum.connector;

import javax.inject.Inject;
import javax.resource.ResourceException;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.ims.ImsOperation;
import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.layer.WipsImsInput;
import com.ford.purchasing.wips.common.layer.WipsImsOutput;
import com.ford.purchasing.wips.common.layer.exception.WipsImsTransactionException;
import com.ford.purchasing.wips.domain.connector.WipsTransactionConnector;

@SuppressWarnings("javadoc")
public class C32uC33uTransaction extends WipsTransactionConnector {

    private static final String C32U_C33U_TRANSACTION = "AAIMC32U_C33U";
    @Inject
    private C32uTransaction c32uTransaction;
    @Inject
    private C33uTransaction c33uTransaction;

    @Override
    protected void populateImsTransactionInput(final WipsImsInput wipsImsInput,
            final DataArea inputDataArea) throws ConnectorException {
        this.c32uTransaction.populateImsTransactionInput(wipsImsInput, inputDataArea);
    }

    @Override
    protected String getImsTransactionName() {
        return C32U_C33U_TRANSACTION;
    }

    @Override
    protected WipsImsOutput populateImsTransactionOutput(final ImsOperation imsoperation)
            throws ResourceException, ConnectorException, WipsImsTransactionException {
        return this.c33uTransaction.populateImsTransactionOutput(imsoperation);
    }
}
