package com.ford.purchasing.wips.domain.layer;

//****************************************************************

//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************

import static com.ford.purchasing.wips.common.layer.util.TransactionOutputUtil.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.layer.Cost;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.lumpsum.C32uTransactionOutput;
import com.ford.purchasing.wips.common.lumpsum.WipsImsC32uInput;
import com.ford.purchasing.wips.common.lumpsum.WipsImsC33uInput;
import com.ford.purchasing.wips.common.lumpsum.WipsImsCR5xInput;
import com.ford.purchasing.wips.domain.lumpsum.beans.C33uTransactionOutput;

public class WipsLumsumHelper {

    public C32uTransactionOutput populateLumpSumInformation(final DataArea outputDataArea) throws ConnectorException {
        final C32uTransactionOutput c32uTransactionOutput = new C32uTransactionOutput();
        c32uTransactionOutput
                .setUserJobCode(getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-JOBCODE"));
        c32uTransactionOutput
                .setUserName(getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-EMPNAME"));
        c32uTransactionOutput
                .setLumpsumNumber(getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-LSPNO"));
        c32uTransactionOutput.setCurrentAmendment(
                getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-LSPVERSION"));
        c32uTransactionOutput.setLumpsumStatus(
                getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-LSPSTATUS"));
        c32uTransactionOutput
                .setLinkedLSNo(getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-LINKRSV"));
        c32uTransactionOutput.setPaymentTitle(
                getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-PAYTITLE"));
        c32uTransactionOutput.setLumpsumType(
                getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-LSPTYPE") + " - "
                        + getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-LSPTPDES"));
        c32uTransactionOutput
                .setPlantToPay(getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-PAYPLANT")
                        + " " + getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-PAYNATCO"));
        c32uTransactionOutput.setFundingOrganization(
                getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-FUND") + " - "
                        + getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-FUNDDESC"));
        c32uTransactionOutput.setPartNumber(getPartNumber(outputDataArea));
        c32uTransactionOutput.setCommodityCode(getCommodityCode(outputDataArea));
        c32uTransactionOutput.setNegotiatingJob(getNegotiatingJob(outputDataArea));
        c32uTransactionOutput.setPaymentTerm(getPaymentTerm(outputDataArea));
        c32uTransactionOutput.setPaymentType(getPaymentType(outputDataArea));
        c32uTransactionOutput.setTotalAmount(getTotalLumpsumAmount(outputDataArea));
        c32uTransactionOutput.setCurrency(getCurrency(outputDataArea));
        c32uTransactionOutput.setSupplierCurrency(
                getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-COSTCURR"));
        c32uTransactionOutput.setGsaAmount(getGsaAmount(outputDataArea));
        c32uTransactionOutput.setDoAAmount(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-DOAAMT"));
        c32uTransactionOutput.setExchangeRate(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-EXCHRATE-CHAR"));
        c32uTransactionOutput.setPreviousNTEBalance(getPrevNteBalance(outputDataArea));
        c32uTransactionOutput.setPreviousRSVBalance(getPrevRsvBalance(outputDataArea));
        c32uTransactionOutput.setNewNTEBalance(getNewNteBalance(outputDataArea));
        c32uTransactionOutput.setNewRSVBalance(getNewRsvBalance(outputDataArea));
        c32uTransactionOutput
                .setPaidDate(getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-PAIDDT"));
        c32uTransactionOutput.setSelectedGsaAudit(
                getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-GSAAUDIT"));
        c32uTransactionOutput.setHasApprove(
                "F13=Approve".equalsIgnoreCase(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-PFK13")));
        c32uTransactionOutput.setHasReject(
                "F17=Reject".equalsIgnoreCase(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-PFK17")));
        c32uTransactionOutput.setHasAttachments(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-ATTACH"));
        c32uTransactionOutput
                .setToggleCurrencyEnabled(isToggleCurrencyEnabled(c32uTransactionOutput.getSupplierCurrency()));
        return c32uTransactionOutput;
    }

    private boolean isToggleCurrencyEnabled(final String supplierCurrency) {
        return supplierCurrency != null && !"USD".equalsIgnoreCase(supplierCurrency);
    }

    private Cost getTotalLumpsumAmount(final DataArea outputDataArea) throws ConnectorException {
        final String amount = getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-TOTAL-CHAR");
        final String currency = getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-TOTALCUR");
        final String sign = getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-TOTALSGN");
        return new Cost(amount, currency, sign);
    }

    private Cost getNewNteBalance(final DataArea outputDataArea) throws ConnectorException {
        final String value = getStringWithUnderScoreTrimmed(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-NEWNTEBL-CHAR");
        final String sign = getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-NEWNTESG");
        final String currency = getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-NEWNTECU");
        return new Cost(value, currency, sign);
    }

    private Cost getNewRsvBalance(final DataArea outputDataArea) throws ConnectorException {
        final String value = getStringWithUnderScoreTrimmed(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-NEWRSVBL-CHAR");
        final String sign = getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-NEWRSVSG");
        final String currency = getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-NEWRSVCU");
        return new Cost(value, currency, sign);
    }

    private Cost getPrevRsvBalance(final DataArea outputDataArea) throws ConnectorException {
        final String value = getStringWithUnderScoreTrimmed(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-CURSVBAL-CHAR");
        final String sign = getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-CURRSVSG");
        final String currency = getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-CURRSVCU");
        return new Cost(value, currency, sign);
    }

    private Cost getPrevNteBalance(final DataArea outputDataArea) throws ConnectorException {
        final String value = getStringWithUnderScoreTrimmed(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-CURNTEBL-CHAR");
        final String sign = getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-CURNTESG");
        final String currency = getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-CURNTECU");
        return new Cost(value, currency, sign);
    }

    private Cost getGsaAmount(final DataArea outputDataArea) throws ConnectorException {
        final String amount = getStringWithUnderScoreTrimmed(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-GSACOST-CHAR");
        final String currency = getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-GSACOSTC");
        final String sign = getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-GSACOSTS");
        return new Cost(amount, currency, sign);
    }

    private Cost getCurrency(final DataArea outputDataArea) throws ConnectorException {
        final String currency = getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-COSTCURR");
        final String sign = getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-COSTSIGN");
        final String amount = getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-COST-CHAR");
        return new Cost(amount, currency, sign);
    }

    private String getPaymentType(final DataArea outputDataArea) throws ConnectorException {
        return getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-PAYTYPE") + " - "
                + getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-PAYTPDES");
    }

    private String getPaymentTerm(final DataArea outputDataArea) throws ConnectorException {
        return getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-PAYTERM")
                + getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-PAYTMDES");
    }

    private String getNegotiatingJob(final DataArea outputDataArea) throws ConnectorException {
        return getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-NEGJOB") + " - "
                + getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-NEGJOBNM");
    }

    private String getCommodityCode(final DataArea outputDataArea) throws ConnectorException {
        return getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-COMMCODE") + " - "
                + getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-COMMDES");
    }

    private String getPartNumber(final DataArea outputDataArea) throws ConnectorException {
        return getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-PARTPFX") + "-"
                + getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-PARTBSE") + "-"
                + getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-PARTSFX") + " "
                + getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-PARTNAME");
    }

    public C33uTransactionOutput generateAdditionalLumpsumInformation(final DataArea outputDataArea)
            throws ConnectorException {
        final C33uTransactionOutput c33uTransactionOutput = new C33uTransactionOutput();
        c33uTransactionOutput.setSelectedClassification(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-CLASS"));
        c33uTransactionOutput
                .setPrePayment(getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-PREPAY"));
        c33uTransactionOutput.setWorkTaskNumber(
                getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-WORKTASK"));
        c33uTransactionOutput
                .setAccountNumber(getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-ACCNO1"));
        c33uTransactionOutput.setSubDivisionNumber(
                getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-SUBDIV1"));
        c33uTransactionOutput
                .setDeptNumber(getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-DEPT1"));
        c33uTransactionOutput.setLongTermCost(
                getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-ACC2AMT-CHAR")
                        + getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-ACC2SGN"));
        c33uTransactionOutput.setLongTermCurrency(
                getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-ACC2CUR"));
        c33uTransactionOutput.setShortTermCost(
                getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-ACC1AMT-CHAR")
                        + getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-ACC1SGN"));
        c33uTransactionOutput.setShortTermCurrency(
                getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-ACC1CUR"));
        final boolean pFKey21 = getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-PF21TXT").contains("F21=");
        // set payment description as confirmed and hide confirm on UI
        if (!pFKey21) {
            c33uTransactionOutput.setHasPaymentConfirmed(true);
        }
        c33uTransactionOutput
                .setModelYear(getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-MODELYR"));
        final List<String> models = new ArrayList<String>();
        for (int count = 1; count <= 12; count++) {
            final String model = getStringWithUnderScoreTrimmed(outputDataArea,
                    "TP-OUTPUT-BUFFER-FIELDS.TPO-MOD" + count);
            if (model != null && model.length() > 0) {
                models.add(model);
            }
        }
        c33uTransactionOutput.setModels(models);
        c33uTransactionOutput.setPaymentDescription(getListOfValues(outputDataArea, "TPO-PAYDESC"));
        return c33uTransactionOutput;
    }

    public WipsImsCR5xInput generateCR5xInputPF14(final DataArea outputDataArea) throws ConnectorException {
        final WipsImsCR5xInput wipsImsCR5xInput = new WipsImsCR5xInput();
        wipsImsCR5xInput.setTpiIoindic(WipsConstant.InputI);
        wipsImsCR5xInput.setTpiPfKey(WipsConstant.PFKEY14);
        wipsImsCR5xInput.setBufferedSegLoop(getSegmentLoop(outputDataArea));
        final List<String> outputFromDataAreaList = new ArrayList<String>();
        for (final Iterator<DataArea> i = wipsImsCR5xInput.getBufferedSegLoop().iterator(); i.hasNext();) {
            final DataArea inputDataArea = i.next();
            outputFromDataAreaList.add(getString(inputDataArea, "TPO-APPRMKS"));
        }
        wipsImsCR5xInput.setRemarksText(outputFromDataAreaList);
        return wipsImsCR5xInput;
    }

    public WipsImsCR5xInput generateCR5xInputPF8(final DataArea outputDataArea) throws ConnectorException {
        final WipsImsCR5xInput wipsImsCR5xInput = new WipsImsCR5xInput();
        wipsImsCR5xInput.setTpiIoindic(WipsConstant.InputI);
        wipsImsCR5xInput.setTpiPfKey(WipsConstant.PFKEY8);
        wipsImsCR5xInput.setBufferedSegLoop(getSegmentLoop(outputDataArea));
        final List<String> outputFromDataAreaList = new ArrayList<String>();
        for (final Iterator<DataArea> i = wipsImsCR5xInput.getBufferedSegLoop().iterator(); i.hasNext();) {
            final DataArea inputDataArea = i.next();
            outputFromDataAreaList.add(getString(inputDataArea, "TPO-APPRMKS"));
        }
        wipsImsCR5xInput.setRemarksText(outputFromDataAreaList);
        return wipsImsCR5xInput;
    }

    public WipsImsC33uInput generateC33Input(final DataArea outputDataArea) throws ConnectorException {
        final WipsImsC33uInput input = new WipsImsC33uInput();
        input.setTpiIoindic(WipsConstant.InputI);
        input.setTpiPfKey(WipsConstant.PFKEY9);
        input.setClassification(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-CLASS"));
        input.setWorkTaskNumber(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-WORKTASK"));
        input.setLongTermCost(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-ACC1AMT-CHAR"));
        input.setLongTermSign(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-ACC1SGN"));
        input.setShortTermCost(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-ACC2AMT-CHAR"));
        input.setcTeam(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-CTEAM"));
        input.setShortTermSign(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-ACC2SGN"));
        return input;
    }

    public WipsImsC32uInput generateC32Input(final DataArea outputDataArea) throws ConnectorException {
        final WipsImsC32uInput input = new WipsImsC32uInput();
        input.setTpiIoindic(WipsConstant.InputI);
        input.setLinkSrv(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-LINKRSV"));
        input.setPayTitle(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-PAYTITLE"));
        input.setLsType(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-LSPTYPE"));
        input.setManAppr(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-MANAPPR"));
        input.setPayPlant(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-PAYPLANT"));
        input.setPayNatCo(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-PAYNATCO"));
        input.setSupplier(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-SUPPLIER"));
        input.setFund(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-FUND"));
        input.setPartPfx(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-PARTPFX"));
        input.setPartBse(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-PARTBSE"));
        input.setPartSfx(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-PARTSFX"));
        input.setCommodityCode(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-COMMCODE"));
        input.setCausFact(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-CAUSFACT"));
        input.setNegJob(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-NEGJOB"));
        input.setPayType(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-PAYTYPE"));
        input.setPayTerm(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-PAYTERM"));
        input.setIssueDate(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-ISSUEDTE"));
        input.setCostDate(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-COSTDATE"));
        input.setGsaReq(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-GSAREQ"));
        input.setGsaAudit(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-GSAAUDIT"));
        input.setCost(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-COST-CHAR"));
        input.setCostSign(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-COSTSIGN"));
        input.setGsaCost(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-GSACOST-CHAR"));
        input.setGsaCosts(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-GSACOSTS"));
        input.setPaidAmount(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-PAIDAMT-CHAR"));
        input.setPaidDate(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-PAIDDT"));
        input.setPaidSign(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-PAIDSGN"));
        input.setKeepLS(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-KEEPLS"));
        input.setAutoISS(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-AUTOISS"));
        input.setKey24(getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-PFK24OPT"));
        return input;
    }

    public String getEnteredUser(final DataArea outputDataArea) throws ConnectorException {
        return getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-ENTRUSER");
    }

    public String getJobCode(final DataArea outputDataArea) throws ConnectorException {
        return getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-USERCODE");
    }

    public String morePages(final DataArea outputDataArea) throws ConnectorException {
        return getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-MORE");
    }
}
