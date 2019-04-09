package com.ford.purchasing.wips.common.lumpsum;

import com.ford.purchasing.wips.common.layer.WipsBaseRequest;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.WipsImsInput;

@SuppressWarnings("javadoc")
public class WipsImsC32uInput extends WipsImsInput {

    private String linkSrv;
    private String payTitle;
    private String lsType;
    private String manAppr;
    private String payPlant;
    private String payNatCo;
    private String supplier;
    private String fund;
    private String partPfx;
    private String partBse;
    private String partSfx;
    private String commodityCode;
    private String causFact;
    private String negJob;
    private String payType;
    private String payTerm;
    private String issueDate;
    private String costDate;
    private String gsaReq;
    private String gsaAudit;
    private String cost;
    private String costSign;
    private String gsaCost;
    private String gsaCosts;
    private String paidAmount;
    private String paidSign;
    private String paidDate;
    private String keepLS;
    private String autoISS;
    private String key24;
    private String transactionName;

    public String getPayTitle() {
        return this.payTitle;
    }

    public void setPayTitle(final String payTitle) {
        this.payTitle = payTitle;
    }

    public String getLsType() {
        return this.lsType;
    }

    public void setLsType(final String lsType) {
        this.lsType = lsType;
    }

    public String getManAppr() {
        return this.manAppr;
    }

    public void setManAppr(final String manAppr) {
        this.manAppr = manAppr;
    }

    public String getPayPlant() {
        return this.payPlant;
    }

    public void setPayPlant(final String payPlant) {
        this.payPlant = payPlant;
    }

    public String getPayNatCo() {
        return this.payNatCo;
    }

    public void setPayNatCo(final String payNatCo) {
        this.payNatCo = payNatCo;
    }

    public String getSupplier() {
        return this.supplier;
    }

    public void setSupplier(final String supplier) {
        this.supplier = supplier;
    }

    public String getFund() {
        return this.fund;
    }

    public void setFund(final String fund) {
        this.fund = fund;
    }

    public String getPartPfx() {
        return this.partPfx;
    }

    public void setPartPfx(final String partPfx) {
        this.partPfx = partPfx;
    }

    public String getPartBse() {
        return this.partBse;
    }

    public void setPartBse(final String partBse) {
        this.partBse = partBse;
    }

    public String getPartSfx() {
        return this.partSfx;
    }

    public void setPartSfx(final String partSfx) {
        this.partSfx = partSfx;
    }

    public String getCommodityCode() {
        return this.commodityCode;
    }

    public void setCommodityCode(final String commodityCode) {
        this.commodityCode = commodityCode;
    }

    public String getCausFact() {
        return this.causFact;
    }

    public void setCausFact(final String causFact) {
        this.causFact = causFact;
    }

    public String getNegJob() {
        return this.negJob;
    }

    public void setNegJob(final String negJob) {
        this.negJob = negJob;
    }

    public String getPayType() {
        return this.payType;
    }

    public void setPayType(final String payType) {
        this.payType = payType;
    }

    public String getIssueDate() {
        return this.issueDate;
    }

    public void setIssueDate(final String issueDate) {
        this.issueDate = issueDate;
    }

    public String getCostDate() {
        return this.costDate;
    }

    public void setCostDate(final String costDate) {
        this.costDate = costDate;
    }

    public String getGsaReq() {
        return this.gsaReq;
    }

    public void setGsaReq(final String gsaReq) {
        this.gsaReq = gsaReq;
    }

    public String getCost() {
        return this.cost;
    }

    public void setCost(final String cost) {
        this.cost = cost;
    }

    public String getCostSign() {
        return this.costSign;
    }

    public void setCostSign(final String costSign) {
        this.costSign = costSign;
    }

    public String getGsaCost() {
        return this.gsaCost;
    }

    public void setGsaCost(final String gsaCost) {
        this.gsaCost = gsaCost;
    }

    public String getGsaCosts() {
        return this.gsaCosts;
    }

    public void setGsaCosts(final String gsaCosts) {
        this.gsaCosts = gsaCosts;
    }

    public String getPaidAmount() {
        return this.paidAmount;
    }

    public void setPaidAmount(final String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getPaidSign() {
        return this.paidSign;
    }

    public void setPaidSign(final String paidSign) {
        this.paidSign = paidSign;
    }

    public String getKeepLS() {
        return this.keepLS;
    }

    public String getPaidDate() {
        return this.paidDate;
    }

    public void setPaidDate(final String paidDate) {
        this.paidDate = paidDate;
    }

    public void setKeepLS(final String keepLS) {
        this.keepLS = keepLS;
    }

    public String getAutoISS() {
        return this.autoISS;
    }

    public void setAutoISS(final String autoISS) {
        this.autoISS = autoISS;
    }

    public String getKey24() {
        return this.key24;
    }

    public void setKey24(final String key24) {
        this.key24 = key24;
    }

    public String getLinkSrv() {
        return this.linkSrv;
    }

    public void setLinkSrv(final String linkSrv) {
        this.linkSrv = linkSrv;
    }

    public String getGsaAudit() {
        return this.gsaAudit;
    }

    public void setGsaAudit(final String gsaAudit) {
        this.gsaAudit = gsaAudit;
    }

    public WipsImsInput populateWipsC32uInputForC33u(
            final WipsBaseRequest wipsBaseRequest) {
        final WipsImsC32uInput wipsImsc32uInput = new WipsImsC32uInput();

        final LumpSumRequest lumpSumRequest = (LumpSumRequest)wipsBaseRequest;

        wipsImsc32uInput.setTpiIoindic(WipsConstant.InputI);
        wipsImsc32uInput.setTpiPfKey(WipsConstant.PFKEY23);
        wipsImsc32uInput.setLinkSrv(WipsConstant.BLANK_SPACE_6);
        wipsImsc32uInput.setLterm(lumpSumRequest.getLterm());

        wipsImsc32uInput.setPayTitle(WipsConstant.BLANK_SPACE_56);
        wipsImsc32uInput.setLsType(WipsConstant.BLANK_SPACE_1);
        wipsImsc32uInput.setManAppr(WipsConstant.BLANK_SPACE_1);
        wipsImsc32uInput.setPayPlant(WipsConstant.BLANK_SPACE_2);
        wipsImsc32uInput.setPayNatCo(WipsConstant.BLANK_SPACE_3);
        wipsImsc32uInput.setSupplier(WipsConstant.BLANK_SPACE_5);
        wipsImsc32uInput.setFund(WipsConstant.BLANK_SPACE_1);
        wipsImsc32uInput.setPartPfx(WipsConstant.BLANK_SPACE_6);
        wipsImsc32uInput.setPartBse(WipsConstant.BLANK_SPACE_8);
        wipsImsc32uInput.setPartSfx(WipsConstant.BLANK_SPACE_8);
        wipsImsc32uInput.setCommodityCode(WipsConstant.BLANK_SPACE_4);
        wipsImsc32uInput.setCausFact(WipsConstant.BLANK_SPACE_1);
        wipsImsc32uInput.setNegJob(WipsConstant.BLANK_SPACE_4);
        wipsImsc32uInput.setPayType(WipsConstant.BLANK_SPACE_1);
        wipsImsc32uInput.setPayTerm(WipsConstant.BLANK_SPACE_1);
        wipsImsc32uInput.setIssueDate(WipsConstant.BLANK_SPACE_6);
        wipsImsc32uInput.setCostDate(WipsConstant.BLANK_SPACE_6);
        wipsImsc32uInput.setGsaReq(WipsConstant.BLANK_SPACE_1);
        wipsImsc32uInput.setGsaAudit(WipsConstant.BLANK_SPACE_1);
        wipsImsc32uInput.setCost(WipsConstant.BLANK_SPACE_14);
        wipsImsc32uInput.setCostSign(WipsConstant.BLANK_SPACE_1);
        wipsImsc32uInput.setGsaCost(WipsConstant.BLANK_SPACE_14);
        wipsImsc32uInput.setGsaCosts(WipsConstant.BLANK_SPACE_1);
        wipsImsc32uInput.setPaidAmount(WipsConstant.BLANK_SPACE_14);
        wipsImsc32uInput.setPaidSign(WipsConstant.BLANK_SPACE_1);
        wipsImsc32uInput.setPaidDate(WipsConstant.BLANK_SPACE_6);
        wipsImsc32uInput.setKeepLS(WipsConstant.BLANK_SPACE_1);
        wipsImsc32uInput.setAutoISS(WipsConstant.BLANK_SPACE_1);
        wipsImsc32uInput.setKey24(WipsConstant.BLANK_SPACE_8);

        return wipsImsc32uInput;
    }

    public String getPayTerm() {
        return this.payTerm;
    }

    public void setPayTerm(final String payTerm) {
        this.payTerm = payTerm;
    }

    public String getTransactionName() {
        return this.transactionName;
    }

    public void setTransactionName(final String transactionName) {
        this.transactionName = transactionName;
    }

}
