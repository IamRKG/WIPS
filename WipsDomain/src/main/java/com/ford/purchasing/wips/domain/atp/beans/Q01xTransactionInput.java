package com.ford.purchasing.wips.domain.atp.beans;

import java.util.Iterator;
import java.util.List;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.connector.BiConsumerThrowsException;
import com.ford.purchasing.wips.common.layer.Category;
import com.ford.purchasing.wips.common.layer.PendingApprovalRequest;
import com.ford.purchasing.wips.common.layer.WipsBaseRequest;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.WipsImsInput;
import com.ford.purchasing.wips.common.layer.util.StringUtil;
import com.ford.purchasing.wips.common.layer.util.TransactionOutputUtil;
import com.ford.purchasing.wips.common.lumpsum.LumpSumRequest;

@SuppressWarnings("javadoc")
public class Q01xTransactionInput extends WipsImsInput {

    private String tpiSelNo;

    private String tpiSelPre;

    private String tpiSelBas;

    private String tpiSelsUp;

    private String tpiSelCaus;

    private String tpiSelAge;

    private String tpiSelProg;

    private String tpiSelNum;

    private String tpiRead;

    private String tpiCat;

    private String tpiEng;

    private String tpiSelSuf;

    private String tpiSelResp;

    public String getTpiSelNo() {
        return this.tpiSelNo;
    }

    public void setTpiSelNo(final String tpiSelNo) {
        this.tpiSelNo = tpiSelNo;
    }

    public String getTpiSelPre() {
        return this.tpiSelPre;
    }

    public void setTpiSelPre(final String tpiSelPre) {
        this.tpiSelPre = tpiSelPre;
    }

    public String getTpiSelBas() {
        return this.tpiSelBas;
    }

    public void setTpiSelBas(final String tpiSelBas) {
        this.tpiSelBas = tpiSelBas;
    }

    public String getTpiSelUp() {
        return this.tpiSelsUp;
    }

    public void setTpiSelUp(final String tpiSelUp) {
        this.tpiSelsUp = tpiSelUp;
    }

    public String getTpiSelCaus() {
        return this.tpiSelCaus;
    }

    public void setTpiSelCaus(final String tpiSelCaus) {
        this.tpiSelCaus = tpiSelCaus;
    }

    public String getTpiSelAge() {
        return this.tpiSelAge;
    }

    public void setTpiSelAge(final String tpiSelAge) {
        this.tpiSelAge = tpiSelAge;
    }

    public String getTpiSelProg() {
        return this.tpiSelProg;
    }

    public void setTpiSelProg(final String tpiSelProg) {
        this.tpiSelProg = tpiSelProg;
    }

    public String getTpiSelNum() {
        return this.tpiSelNum;
    }

    public void setTpiSelNum(final String tpiSelNum) {
        this.tpiSelNum = tpiSelNum;
    }

    public String getTpiRead() {
        return this.tpiRead;
    }

    public void setTpiRead(final String tpiRead) {
        this.tpiRead = tpiRead;
    }

    public String getTpiCat() {
        return this.tpiCat;
    }

    public void setTpiCat(final String tpiCat) {
        this.tpiCat = tpiCat;
    }

    public String getTpiEng() {
        return this.tpiEng;
    }

    public void setTpiEng(final String tpiEng) {
        this.tpiEng = tpiEng;
    }

    public String getTpiSelSuf() {
        return this.tpiSelSuf;
    }

    public void setTpiSelSuf(final String tpiSelSuf) {
        this.tpiSelSuf = tpiSelSuf;
    }

    public String getTpiSelResp() {
        return this.tpiSelResp;
    }

    public void setTpiSelResp(final String tpiSelResp) {
        this.tpiSelResp = tpiSelResp;
    }

    public WipsImsInput populateWipsQ01XToC32UInput(final WipsBaseRequest wipsBaseRequest) {
        final Q01xTransactionInput wipsImsQ01xInput =
                populateCommonQ01xFields(wipsBaseRequest);
        wipsImsQ01xInput.setTpiPfKey(WipsConstant.PFKEY13);
        return wipsImsQ01xInput;
    }

    public WipsImsInput populateWipsQ01XInput(final WipsBaseRequest wipsBaseRequest) {
        final Q01xTransactionInput wipsImsQ01xInput =
                populateCommonQ01xFields(wipsBaseRequest);
        wipsImsQ01xInput.setTpiPfKey(WipsConstant.PFKEY0);
        return wipsImsQ01xInput;
    }

    public Q01xTransactionInput populateCommonQ01xFields(
            final WipsBaseRequest wipsBaseRequest) {
        final Q01xTransactionInput wipsImsQ01xInput = new Q01xTransactionInput();
        final LumpSumRequest lumpSumRequest = (LumpSumRequest)wipsBaseRequest;
        wipsImsQ01xInput.setTpiIoindic(WipsConstant.InputI);
        wipsImsQ01xInput.setTpiPageNo(WipsConstant.PAGENUMBERONE);
        wipsImsQ01xInput.setTpiSelNo(WipsConstant.LUMPSUM_ENTITY_CODE);
        wipsImsQ01xInput.setTpiSelPre(StringUtil.createBlankSpaces(6));
        wipsImsQ01xInput.setTpiSelBas(StringUtil.createBlankSpaces(8));
        wipsImsQ01xInput.setTpiSelSuf(StringUtil.createBlankSpaces(8));
        wipsImsQ01xInput.setTpiSelUp(StringUtil.createBlankSpaces(5));
        wipsImsQ01xInput.setTpiSelCaus(StringUtil.createBlankSpaces(6));
        wipsImsQ01xInput.setTpiSelAge(StringUtil.createBlankSpaces(5));
        wipsImsQ01xInput.setTpiSelResp(StringUtil.createBlankSpaces(4));
        wipsImsQ01xInput.setTpiSelProg(StringUtil.createBlankSpaces(8));
        wipsImsQ01xInput.setTpiSelNum(lumpSumRequest.getLumpSumNumber());
        wipsImsQ01xInput.setTpiCat(WipsConstant.SelectLumpSum);
        wipsImsQ01xInput.setTpiEng(StringUtil.createBlankSpaces(18));
        wipsImsQ01xInput.setTpiRead(StringUtil.createBlankSpaces(1));
        wipsImsQ01xInput.setTpiSelect(StringUtil.createBlankSpaces(1));
        wipsImsQ01xInput.setLterm(lumpSumRequest.getLterm());
        return wipsImsQ01xInput;
    }

    public void populate(final DataArea inputDataArea, final PendingApprovalRequest request,
            final short pfKey, final String pageNo, final String selcat)
            throws ConnectorException {
        inputDataArea.put("TPI-IOINDIC", WipsConstant.InputI);
        inputDataArea.put("TPI-PAGENO", pageNo);
        inputDataArea.put("TPI-PFKEY", pfKey);
        inputDataArea.put("TPI-SELNO", request.getCategory().getCategoryCode());
        inputDataArea.put("TPI-SELPRE", StringUtil.createBlankSpaces(6));
        inputDataArea.put("TPI-SELBAS", StringUtil.createBlankSpaces(8));
        inputDataArea.put("TPI-SELSUF", StringUtil.createBlankSpaces(8));
        inputDataArea.put("TPI-SELSUP", StringUtil.createBlankSpaces(5));
        inputDataArea.put("TPI-SELCAUS", StringUtil.createBlankSpaces(6));
        inputDataArea.put("TPI-SELAGE", StringUtil.createBlankSpaces(5));
        inputDataArea.put("TPI-SELRESP", StringUtil.createBlankSpaces(4));
        inputDataArea.put("TPI-SELPROG", StringUtil.createBlankSpaces(8));
        inputDataArea.put("TPI-SELNUM", request.getEntityNumber());
        inputDataArea.put("TPI-SELREAD", StringUtil.createBlankSpaces(1));
        inputDataArea.put("TPI-SELCAT", selcat);
        inputDataArea.put("TPI-SELENG", StringUtil.createBlankSpaces(18));
        final List<DataArea> ar730InputDocIndList =
                TransactionOutputUtil.getArray(inputDataArea, "TPI-BUFFER-SEGLOOP");
        DataArea parameterList;
        for (final Iterator<DataArea> i = ar730InputDocIndList.iterator(); i.hasNext();) {
            parameterList = i.next();
            parameterList.put("TPI-SELECT", StringUtil.createBlankSpaces(1));
        }
    }

    public BiConsumerThrowsException<Q01xTransactionOutput, DataArea, ConnectorException> loadQ01xInput(
            final PendingApprovalRequest priceClaimsRequest, final short pfKey,
            final String selcat) {
        return new BiConsumerThrowsException<Q01xTransactionOutput, DataArea, ConnectorException>() {

            @Override
            public void accept(
                    final Q01xTransactionOutput q01xTransactionOutput,
                    final DataArea inputDataArea)
                    throws ConnectorException {
                populate(inputDataArea, priceClaimsRequest, pfKey,
                        q01xTransactionOutput.getPageNo(), selcat);
            }
        };
    }

    public BiConsumerThrowsException<Q01xTransactionOutput, DataArea, ConnectorException> loadQ01xInput(
        final PendingApprovalRequest pendingApprovalRequest, final short pfkey8) {
        return loadQ01xInput(pendingApprovalRequest, pfkey8, selCat(pendingApprovalRequest));

    }

    private String selCat(final PendingApprovalRequest pendingApprovalRequest) {
        String selcat = null;
        final Category category = pendingApprovalRequest.getCategory();
        switch (category) {
        case ATP:
            selcat = WipsConstant.SELCAT;
            break;
        case LUMPSUM:
            selcat = WipsConstant.SelectLumpSum;
            break;
        }
        return selcat;
    }
}
