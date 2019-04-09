package com.ford.purchasing.wips.common.lumpsum;

import java.util.List;

import com.ford.purchasing.wips.common.layer.AttachmentDetail;
import com.ford.purchasing.wips.common.layer.Cost;
import com.ford.purchasing.wips.common.layer.GsaAudit;
import com.ford.purchasing.wips.common.layer.LumpSumType;
import com.ford.purchasing.wips.common.layer.WipsImsOutput;

@SuppressWarnings("javadoc")
public class C32uTransactionOutput extends WipsImsOutput {

    private String userJobCode;
    private String userName;
    private String lumpsumNumber;
    private String lumpsumStatus;
    private String linkedLSNo;
    private String paymentTitle;
    private String lumpsumType;
    private String plantToPay;
    private String fundingOrganization;
    private String partNumber;
    private WipsImsC32uInput wipsImsC32uInput;
    private String commodityCode;
    private String negotiatingJob;
    private String paymentTerm;
    private String paymentType;
    private Cost currency;
    private Cost gsaAmount;
    private String doAAmount;
    private Cost totalAmount;
    private Cost previousRSVBalance;
    private Cost newRSVBalance;
    private Cost previousNTEBalance;
    private Cost newNTEBalance;
    private String paidDate;
    private List<GsaAudit> gsaAudit;
    private String selectedGsaAudit;
    private boolean hasApprove = false;
    private boolean hasReject = false;
    private List<AttachmentDetail> attachmentDetails;
    private List<SupplierInformation> supplierInformation;
    private String currentAmendment;
    private String latestAmendment;
    private String hasAttachments;
    private boolean lumpSumReadOnlyFlag;
    private LumpSumType lsTypeCode;
    private String confirmationMessage;
    private boolean hasMoreSuppliers;
    private boolean toggleCurrencyEnabled;
    private String supplierCurrency;
    private String exchangeRate;

    public boolean hasMoreSuppliers() {
        return this.hasMoreSuppliers;
    }

    public void setMoreSuppliers(final boolean hasMoreSuppliers) {

        this.hasMoreSuppliers = hasMoreSuppliers;
    }

    public boolean isHasApprove() {
        return this.hasApprove;
    }

    public void setHasApprove(final boolean hasApprove) {

        this.hasApprove = hasApprove;
    }

    public boolean isHasReject() {
        return this.hasReject;
    }

    public void setHasReject(final boolean hasReject) {
        this.hasReject = hasReject;
    }

    public String getPartNumber() {
        return this.partNumber;
    }

    public void setPartNumber(final String partNumber) {
        this.partNumber = partNumber;
    }

    public Cost getTotalAmount() {
        return this.totalAmount;
    }

    public void setTotalAmount(final Cost totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSelectedGsaAudit() {
        return this.selectedGsaAudit;
    }

    public void setSelectedGsaAudit(final String selectedGsaAudit) {
        this.selectedGsaAudit = selectedGsaAudit;
    }

    public List<AttachmentDetail> getAttachmentDetails() {
        return this.attachmentDetails;
    }

    public void setAttachmentDetails(final List<AttachmentDetail> attachment) {
        this.attachmentDetails = attachment;
    }

    public List<SupplierInformation> getSupplierInformation() {
        return this.supplierInformation;
    }

    public void setSupplierInformation(final List<SupplierInformation> supplierInformation) {
        this.supplierInformation = supplierInformation;
    }

    public String getUserJobCode() {
        return this.userJobCode;
    }

    public void setUserJobCode(final String userJobCode) {
        this.userJobCode = userJobCode;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public String getLumpsumNumber() {
        return this.lumpsumNumber;
    }

    public void setLumpsumNumber(final String lumpsumNumber) {
        this.lumpsumNumber = lumpsumNumber;
    }

    public String getLumpsumStatus() {
        return this.lumpsumStatus;
    }

    public void setLumpsumStatus(final String lumpsumStatus) {
        this.lumpsumStatus = lumpsumStatus;
    }

    public String getLinkedLSNo() {
        return this.linkedLSNo;
    }

    public void setLinkedLSNo(final String linkedLSNo) {
        this.linkedLSNo = linkedLSNo;
    }

    public String getPaymentTitle() {
        return this.paymentTitle;
    }

    public void setPaymentTitle(final String paymentTitle) {
        this.paymentTitle = paymentTitle;
    }

    public String getLumpsumType() {
        return this.lumpsumType;
    }

    public void setLumpsumType(final String lumpsumType) {
        this.lumpsumType = lumpsumType;
    }

    public String getPlantToPay() {
        return this.plantToPay;
    }

    public void setPlantToPay(final String plantToPay) {
        this.plantToPay = plantToPay;
    }

    public String getFundingOrganization() {
        return this.fundingOrganization;
    }

    public void setFundingOrganization(final String fundingOrganization) {
        this.fundingOrganization = fundingOrganization;
    }

    public String getNegotiatingJob() {
        return this.negotiatingJob;
    }

    public void setNegotiatingJob(final String negotiatingJob) {
        this.negotiatingJob = negotiatingJob;
    }

    public String getPaymentTerm() {
        return this.paymentTerm;
    }

    public void setPaymentTerm(final String paymentTerm) {
        this.paymentTerm = paymentTerm;
    }

    public String getPaymentType() {
        return this.paymentType;
    }

    public void setPaymentType(final String paymentType) {
        this.paymentType = paymentType;
    }

    public Cost getCurrency() {
        return this.currency;
    }

    public void setCurrency(final Cost currency) {
        this.currency = currency;
    }

    public Cost getGsaAmount() {
        return this.gsaAmount;
    }

    public void setGsaAmount(final Cost gsaAmount) {
        this.gsaAmount = gsaAmount;
    }

    public String getDoAAmount() {
        return this.doAAmount;
    }

    public void setDoAAmount(final String doAAmount) {
        this.doAAmount = doAAmount;
    }

    public Cost getPreviousRSVBalance() {
        return this.previousRSVBalance;
    }

    public void setPreviousRSVBalance(final Cost previousRSVBalance) {
        this.previousRSVBalance = previousRSVBalance;
    }

    public Cost getNewRSVBalance() {
        return this.newRSVBalance;
    }

    public void setNewRSVBalance(final Cost newRSVBalance) {
        this.newRSVBalance = newRSVBalance;
    }

    public Cost getPreviousNTEBalance() {
        return this.previousNTEBalance;
    }

    public void setPreviousNTEBalance(final Cost previousNTEBalance) {
        this.previousNTEBalance = previousNTEBalance;
    }

    public Cost getNewNTEBalance() {
        return this.newNTEBalance;
    }

    public void setNewNTEBalance(final Cost newNTEBalance) {
        this.newNTEBalance = newNTEBalance;
    }

    public String getPaidDate() {
        return this.paidDate;
    }

    public void setPaidDate(final String paidDate) {
        this.paidDate = paidDate;
    }

    public List<GsaAudit> getGsaAudit() {
        return this.gsaAudit;
    }

    public void setGsaAudit(final List<GsaAudit> gsaAudit) {
        this.gsaAudit = gsaAudit;
    }

    public String getCurrentAmendment() {
        return this.currentAmendment;
    }

    public void setCurrentAmendment(final String currentAmendment) {
        this.currentAmendment = currentAmendment;
    }

    public String getLatestAmendment() {
        return this.latestAmendment;
    }

    public void setLatestAmendment(final String latestAmendment) {
        this.latestAmendment = latestAmendment;

    }

    public String getCommodityCode() {
        return this.commodityCode;
    }

    public void setCommodityCode(final String commodityCode) {
        this.commodityCode = commodityCode;
    }

    public WipsImsC32uInput getWipsImsC32uInput() {
        return this.wipsImsC32uInput;
    }

    public void setWipsImsC32uInput(final WipsImsC32uInput wipsImsC32uInput) {

        this.wipsImsC32uInput = wipsImsC32uInput;
    }

    public String getHasAttachments() {
        return this.hasAttachments;
    }

    public void setHasAttachments(final String hasAttachments) {
        this.hasAttachments = hasAttachments;
    }

    public boolean isLumpSumReadOnlyFlag() {
        return this.lumpSumReadOnlyFlag;
    }

    public void setLumpSumReadOnlyFlag(final boolean lumpSumReadOnlyFlag) {
        this.lumpSumReadOnlyFlag = lumpSumReadOnlyFlag;
    }

    public String getLsTypeCode() {
        if (this.lsTypeCode != null) {
            return this.lsTypeCode.getLumpSumTypeCode();
        }
        return null;
    }

    public void setLsTypeCode(final LumpSumType lsType) {
        this.lsTypeCode = lsType;
    }

    public String getConfirmationMessage() {
        return this.confirmationMessage;
    }

    public void setConfirmationMessage(final String confirmationMessage) {
        this.confirmationMessage = confirmationMessage;
    }

    public boolean isToggleCurrencyEnabled() {
        return this.toggleCurrencyEnabled;
    }

    public void setToggleCurrencyEnabled(final boolean toggleCurrencyEnabled) {
        this.toggleCurrencyEnabled = toggleCurrencyEnabled;
    }

    public void setSupplierCurrency(final String supplierCurrency) {
        this.supplierCurrency = supplierCurrency;
    }

    public String getSupplierCurrency() {
        return this.supplierCurrency;
    }

    public void setExchangeRate(final String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getExchangeRate() {
        return this.exchangeRate;
    }

}
