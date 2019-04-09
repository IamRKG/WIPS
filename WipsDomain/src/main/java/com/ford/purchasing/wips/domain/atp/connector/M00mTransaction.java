package com.ford.purchasing.wips.domain.atp.connector;

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
import com.ford.purchasing.wips.domain.connector.WipsTransactionHelper;
import com.ford.purchasing.wips.domain.layer.WipsTransactionConstant;

public class M00mTransaction extends WipsTransactionConnector {

    private static final String CLASS_NAME = M00mTransaction.class.getName();
    private static ILogger log = LogFactory.getInstance().getLogger(CLASS_NAME);
    private static final String M00M_TRANSACTION = "AAIMM00M";

    @Override
    public void populateImsTransactionInput(final WipsImsInput wipsImsInput, final DataArea inputDataArea) {
        inputDataArea.put("TP-INPUT-FIELDS.TPI-IOINDIC", wipsImsInput.getTpiIoindic());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-PFKEY", wipsImsInput.getTpiPfKey());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-PAGENO", wipsImsInput.getTpiPageNo());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-OPTION", wipsImsInput.getTpiOption());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-ALTCODE", wipsImsInput.getJobCode());
    }

    @Override
    public WipsImsOutput populateImsTransactionOutput(final ImsOperation imsOperation)
            throws ResourceException, ConnectorException, WipsImsTransactionException {
        final String rawOutputM00M = imsOperation.getRawOutput();
        log.info("M00M output" + rawOutputM00M);
        M00mTransactionOutput m00mTransactionOutput = null;
        final DataArea outputDataArea = (DataArea) imsOperation.getOutputRecord();
        final String transactionName = TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-PROGRAM");
        if (WipsTransactionConstant.M00M_TRANSACTION_NAME.equals(transactionName)) {
            m00mTransactionOutput = new WipsTransactionHelper()
                    .populateM00mOutput((DataArea) imsOperation.getOutputRecord());
        } else {
            throw new WipsImsTransactionException(getImsTransactionName(), rawOutputM00M.substring(108, 184));
        }
        return m00mTransactionOutput;
    }

    @Override
    protected String getImsTransactionName() {
        return M00M_TRANSACTION;
    }
}
