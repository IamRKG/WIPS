package com.ford.purchasing.wips.domain.lumpsum.connector;

import java.util.Arrays;

import javax.resource.ResourceException;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.ims.ImsOperation;
import com.ford.it.connector.record.DataArea;
import com.ford.it.logging.ILogger;
import com.ford.it.logging.LogFactory;
import com.ford.purchasing.wips.common.layer.LumpSumType;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.WipsImsInput;
import com.ford.purchasing.wips.common.layer.WipsImsOutput;
import com.ford.purchasing.wips.common.layer.exception.WipsImsTransactionException;
import com.ford.purchasing.wips.common.layer.util.TransactionOutputUtil;
import com.ford.purchasing.wips.common.layer.util.WipsUtil;
import com.ford.purchasing.wips.common.lumpsum.C32uTransactionOutput;
import com.ford.purchasing.wips.common.lumpsum.WipsImsC32uInput;
import com.ford.purchasing.wips.domain.connector.WipsTransactionConnector;
import com.ford.purchasing.wips.domain.layer.WipsLumsumHelper;
import com.ford.purchasing.wips.domain.layer.WipsTransactionConstant;

@SuppressWarnings("javadoc")
public class C32uTransaction extends WipsTransactionConnector {

    private static final String CLASS_NAME = C32uTransaction.class
            .getName();
    private static ILogger log = LogFactory.getInstance().getLogger(CLASS_NAME);
    private static final String C32U_TRANSACTION = "AAIMC32U";

    @Override
    protected void populateImsTransactionInput(final WipsImsInput wipsImsInput,
            final DataArea inputDataArea) throws ConnectorException {
        final WipsImsC32uInput wipsImsC32uInput = (WipsImsC32uInput)wipsImsInput;
        inputDataArea.put("TP-INPUT-FIELDS.TPI-IOINDIC", wipsImsC32uInput.getTpiIoindic());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-PFKEY", wipsImsC32uInput.getTpiPfKey());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-LINKRSV", wipsImsC32uInput.getLinkSrv());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-PAYTITLE", wipsImsC32uInput.getPayTitle());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-LSPTYPE", wipsImsC32uInput.getLsType());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-MANAPPR", wipsImsC32uInput.getManAppr());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-PAYPLANT", wipsImsC32uInput.getPayPlant());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-PAYNATCO", wipsImsC32uInput.getPayNatCo());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-SUPPLIER", wipsImsC32uInput.getSupplier());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-FUND", wipsImsC32uInput.getFund());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-PARTPFX", wipsImsC32uInput.getPartPfx());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-PARTBSE", wipsImsC32uInput.getPartBse());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-PARTSFX", wipsImsC32uInput.getPartSfx());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-COMMCODE",
                wipsImsC32uInput.getCommodityCode());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-CAUSFACT", wipsImsC32uInput.getCausFact());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-NEGJOB", wipsImsC32uInput.getNegJob());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-PAYTYPE", wipsImsC32uInput.getPayType());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-PAYTERM", wipsImsC32uInput.getPayTerm());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-ISSUEDTE", wipsImsC32uInput.getIssueDate());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-COSTDATE", wipsImsC32uInput.getCostDate());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-GSAREQ", wipsImsC32uInput.getGsaReq());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-GSAAUDIT", wipsImsC32uInput.getGsaAudit());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-COST", wipsImsC32uInput.getCost());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-COSTSIGN", wipsImsC32uInput.getCostSign());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-GSACOST", wipsImsC32uInput.getGsaCost());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-GSACOSTS", wipsImsC32uInput.getGsaCosts());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-PAIDAMT", wipsImsC32uInput.getPaidAmount());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-PAIDSGN", wipsImsC32uInput.getPaidSign());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-PAIDDT", wipsImsC32uInput.getPaidDate());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-KEEPLS", wipsImsC32uInput.getKeepLS());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-AUTOISS", wipsImsC32uInput.getAutoISS());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-PFK24OPT", wipsImsC32uInput.getKey24());
    }

    @Override
    protected String getImsTransactionName() {
        return C32U_TRANSACTION;
    }

    @Override
    protected WipsImsOutput populateImsTransactionOutput(
            final ImsOperation imsOperation) throws ResourceException,
            ConnectorException, WipsImsTransactionException {
        final String rawOutput = imsOperation.getRawOutput();
        log.info("C32u Raw Output" + rawOutput);
        C32uTransactionOutput c32uTransactionOutput = null;
        String transactionName = null;
        final DataArea outputDataArea = (DataArea)imsOperation.getOutputRecord();
        transactionName = TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-PROGRAM");
        if (WipsTransactionConstant.C32U_TRANSACTION_NAME.contains(transactionName)) {
            final WipsLumsumHelper wipsLumsumHelper = new WipsLumsumHelper();
            c32uTransactionOutput =
                    wipsLumsumHelper.populateLumpSumInformation(outputDataArea);
            c32uTransactionOutput.setLsTypeCode(this.resolveLumpSumType(outputDataArea));
            c32uTransactionOutput
                    .setWipsImsC32uInput(wipsLumsumHelper.generateC32Input(outputDataArea));
            final String errorMessage = imsOperation.getRawOutput()
                    .substring(38, 114);
            if (WipsUtil.isExceptionContainingMessageCode(errorMessage,
                    Arrays.asList(WipsConstant.getC32uErrorMessages())))
                throw new WipsImsTransactionException(getImsTransactionName(),
                        errorMessage);
        } else {
            throw new WipsImsTransactionException(getImsTransactionName(),
                    "Unable to process your request currently. Please contact system administrator");
        }
        return c32uTransactionOutput;
    }

    LumpSumType resolveLumpSumType(final DataArea outputDataArea) throws ConnectorException {
        final String lsType =
                TransactionOutputUtil.getStringWithUnderScoreTrimmed(outputDataArea,
                        "TP-OUTPUT-BUFFER-FIELDS.TPO-LSPTYPE");
        final String linkedType = resolvedLinkedType(outputDataArea);
        return LumpSumType.getLumpSumType(lsType, linkedType);
    }

    private String resolvedLinkedType(final DataArea outputDataArea)
            throws ConnectorException {
        final boolean rsvLinked = isLinkedToReserve(outputDataArea);
        final boolean nteLinked = isLinkedToNte(outputDataArea);
        return linkedType(rsvLinked, nteLinked);
    }

    private String linkedType(final boolean rsvLinked, final boolean nteLinked) {
        String linkedType = null;
        if (rsvLinked && nteLinked) {
            linkedType = "B";
        } else if (rsvLinked) {
            linkedType = "R";
        } else if (nteLinked) {
            linkedType = "N";
        }
        return linkedType;
    }

    private boolean isLinkedToNte(final DataArea outputDataArea) throws ConnectorException {
        final String prevNte =
                TransactionOutputUtil.getString(outputDataArea,
                        "TP-OUTPUT-BUFFER-FIELDS.TPO-CURNTELI");
        final String newNte =
                TransactionOutputUtil.getString(outputDataArea,
                        "TP-OUTPUT-BUFFER-FIELDS.TPO-NEWNTELI");

        boolean nteLinked = false;

        if (WipsConstant.PREV_NTE_BAL.equals(prevNte)
            && WipsConstant.NEW_NTE_BAL.equals(newNte)) {
            nteLinked = true;
        }
        return nteLinked;
    }

    private boolean isLinkedToReserve(final DataArea outputDataArea)
            throws ConnectorException {
        boolean rsvLinked = false;
        final String prevRsv =
                TransactionOutputUtil.getString(outputDataArea,
                        "TP-OUTPUT-BUFFER-FIELDS.TPO-CURRSVLI");
        final String newRsv =
                TransactionOutputUtil.getString(outputDataArea,
                        "TP-OUTPUT-BUFFER-FIELDS.TPO-NEWRSVLI");
        if (WipsConstant.PREV_RSV_BAL.equals(prevRsv)
            && WipsConstant.NEW_RSV_BAL.equals(newRsv)) {
            rsvLinked = true;
        }
        return rsvLinked;
    }

}
