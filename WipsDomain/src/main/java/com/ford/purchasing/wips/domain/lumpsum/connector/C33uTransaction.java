package com.ford.purchasing.wips.domain.lumpsum.connector;

import java.util.Arrays;

import javax.resource.ResourceException;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.ims.ImsOperation;
import com.ford.it.connector.record.DataArea;
import com.ford.it.logging.ILogger;
import com.ford.it.logging.LogFactory;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.WipsImsInput;
import com.ford.purchasing.wips.common.layer.WipsImsOutput;
import com.ford.purchasing.wips.common.layer.exception.WipsImsTransactionException;
import com.ford.purchasing.wips.common.layer.util.TransactionOutputUtil;
import com.ford.purchasing.wips.common.layer.util.WipsUtil;
import com.ford.purchasing.wips.common.lumpsum.WipsImsC33uInput;
import com.ford.purchasing.wips.domain.connector.WipsTransactionConnector;
import com.ford.purchasing.wips.domain.layer.WipsLumsumHelper;
import com.ford.purchasing.wips.domain.layer.WipsTransactionConstant;
import com.ford.purchasing.wips.domain.lumpsum.beans.C33uTransactionOutput;

@SuppressWarnings("javadoc")
public class C33uTransaction extends WipsTransactionConnector {

    private static final String CLASS_NAME = C33uTransaction.class.getName();
    private static ILogger log = LogFactory.getInstance().getLogger(CLASS_NAME);
    private static final String C33U_TRANSACTION = "AAIMC33U";
    private String actionTaken;

    @Override
    protected void populateImsTransactionInput(final WipsImsInput wipsImsInput,
            final DataArea inputDataArea) {
        final WipsImsC33uInput wipsImsC33uInput = (WipsImsC33uInput)wipsImsInput;
        this.actionTaken = wipsImsC33uInput.getActionTaken();
        inputDataArea.put("TP-INPUT-FIELDS.TPI-IOINDIC",
                wipsImsC33uInput.getTpiIoindic());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-PFKEY",
                wipsImsC33uInput.getTpiPfKey());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-CLASS",
                wipsImsC33uInput.getClassification());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-WORKTASK",
                wipsImsC33uInput.getWorkTaskNumber());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-ACC1AMT",
                wipsImsC33uInput.getShortTermCost());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-ACC1SGN",
                wipsImsC33uInput.getShortTermSign());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-ACC2AMT",
                wipsImsC33uInput.getLongTermCost());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-ACC2SGN",
                wipsImsC33uInput.getLongTermSign());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-CTEAM",
                wipsImsC33uInput.getcTeam());
    }

    @Override
    protected String getImsTransactionName() {
        return C33U_TRANSACTION;
    }

    @Override
    protected WipsImsOutput populateImsTransactionOutput(
            final ImsOperation imsOperation) throws ResourceException,
            ConnectorException, WipsImsTransactionException {
        final String rawOutput = imsOperation.getRawOutput();
        log.info("c33u rawoutput" + rawOutput);
        C33uTransactionOutput c33uTransactionOutput = null;
        String transactionName = null;
        final DataArea outputDataArea = (DataArea)imsOperation.getOutputRecord();
        transactionName = TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-PROGRAM");
        if (WipsTransactionConstant.C33U_TRANSACTION_NAME.contains(transactionName)) {
            final WipsLumsumHelper helper = new WipsLumsumHelper();
            if (!WipsConstant.CONFIRMLS_PAYMENT_DESC.equals(this.actionTaken)) {
                c33uTransactionOutput =
                        helper.generateAdditionalLumpsumInformation(outputDataArea);
                c33uTransactionOutput
                        .setWipsImsC33uInput(helper.generateC33Input(outputDataArea));
                if (WipsUtil.isExceptionContainingMessageCode(imsOperation.getRawOutput()
                        .substring(38, 114),
                        Arrays.asList(WipsConstant.getC33uErrorMessages())))
                    throw new WipsImsTransactionException(getImsTransactionName(),
                            imsOperation.getRawOutput().substring(38, 104));
            }
        } else {
            throw new WipsImsTransactionException(getImsTransactionName(),
                    imsOperation.getRawOutput().substring(38, 104));
        }

        return c33uTransactionOutput;
    }
}
