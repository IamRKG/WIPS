package com.ford.purchasing.wips.domain.lumpsum.connector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import com.ford.purchasing.wips.common.lumpsum.WipsImsCR5xInput;
import com.ford.purchasing.wips.domain.connector.WipsTransactionConnector;
import com.ford.purchasing.wips.domain.lumpsum.beans.C33uTransactionOutput;
import com.ford.purchasing.wips.domain.lumpsum.service.LumpSumDetailAS;

@SuppressWarnings("javadoc")
public class CR5xC33uTransaction extends WipsTransactionConnector {
    private static final String CLASS_NAME = LumpSumDetailAS.class.getName();
    private static ILogger log = LogFactory.getInstance().getLogger(CLASS_NAME);
    private static final String CR5X_C33U_TRANSACTION = "AAIMCR5X-C33U";

    @Override
    protected void populateImsTransactionInput(final WipsImsInput wipsImsInput,
            final DataArea inputDataArea) throws ConnectorException {
        final WipsImsCR5xInput wipsImsCR5xInput = (WipsImsCR5xInput)wipsImsInput;
        inputDataArea.put("TPI-IOINDIC", wipsImsCR5xInput.getTpiIoindic());
        inputDataArea.put("TPI-PFKEY", wipsImsCR5xInput.getTpiPfKey());
        final List<DataArea> inputDocList = wipsImsCR5xInput.getBufferedSegLoop();
        final List<String> outputFromDataAreaList = new ArrayList<String>();
        for (final Iterator<DataArea> i = inputDocList.iterator(); i.hasNext();) {
            final DataArea inputDataAreaTemp = i.next();
            outputFromDataAreaList
                    .add(TransactionOutputUtil.getString(inputDataAreaTemp, "TPO-APPRMKS"));
        }
        final List<DataArea> inputList =
                TransactionOutputUtil.getArray(inputDataArea, "TPI-BUFFER-SEGLOOP");
        int index = 0;
        for (final Iterator<DataArea> i = inputList.iterator(); i.hasNext();) {
            final DataArea inputDataAreaTemp = i.next();
            inputDataAreaTemp.put("TPI-APPRMKS", outputFromDataAreaList.get(index));
            index++;
        }
    }

    @Override
    protected String getImsTransactionName() {
        return CR5X_C33U_TRANSACTION;
    }

    @Override
    protected WipsImsOutput populateImsTransactionOutput(final ImsOperation imsOperation)
            throws ResourceException, ConnectorException, WipsImsTransactionException {
        log.info("imsOperation--------------" + imsOperation.getRawOutput());
        if (imsOperation.getRawOutput().length() != 1252) {
            throw new WipsImsTransactionException(CR5X_C33U_TRANSACTION,
                    imsOperation.getRawOutput().substring(63, 139));
        }
        return new C33uTransactionOutput();
    }
}
