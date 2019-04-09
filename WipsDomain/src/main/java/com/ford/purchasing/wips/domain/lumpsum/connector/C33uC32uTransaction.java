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
import com.ford.purchasing.wips.common.lumpsum.C32uTransactionOutput;
import com.ford.purchasing.wips.domain.connector.WipsTransactionConnector;
import com.ford.purchasing.wips.domain.layer.WipsTransactionConstant;
import com.ford.purchasing.wips.domain.lumpsum.service.LumpSumDetailAS;

@SuppressWarnings("javadoc")
public class C33uC32uTransaction extends WipsTransactionConnector {
    private static final String CLASS_NAME = LumpSumDetailAS.class.getName();
    private static ILogger log = LogFactory.getInstance().getLogger(CLASS_NAME);
    private static final String C33U_C32U_TRANSACTION = "AAIMC33U-C32U";

    @Inject
    private C33uTransaction c33uTransaction;

    @Override
    protected void populateImsTransactionInput(final WipsImsInput wipsImsInput,
            final DataArea inputDataArea) {
        this.c33uTransaction.populateImsTransactionInput(wipsImsInput, inputDataArea);
    }

    @Override
    protected String getImsTransactionName() {
        return C33U_C32U_TRANSACTION;
    }

    @Override
    protected WipsImsOutput populateImsTransactionOutput(final ImsOperation imsOperation)
            throws ResourceException, ConnectorException, WipsImsTransactionException {
        log.info("imsOperation--------------" + imsOperation.getRawOutput());
        final C32uTransactionOutput output = new C32uTransactionOutput();
        if (imsOperation.getRawOutput().substring(38, 114).contains("MSG-5608"))
            output.setMoreSuppliers(true);
        final DataArea outputDataArea = (DataArea)imsOperation.getOutputRecord();
        final String transactionName = TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-PROGRAM");
        if (!WipsTransactionConstant.C32U_TRANSACTION_NAME.equals(transactionName)) {
            throw new WipsImsTransactionException(getImsTransactionName(),
                    imsOperation.getRawOutput().substring(63, 139));
        }
        return output;
    }
}
