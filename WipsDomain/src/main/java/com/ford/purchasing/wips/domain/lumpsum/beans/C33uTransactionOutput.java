package com.ford.purchasing.wips.domain.lumpsum.beans;

import java.util.List;

import com.ford.purchasing.wips.common.layer.LsClassification;
import com.ford.purchasing.wips.common.layer.WipsImsOutput;
import com.ford.purchasing.wips.common.lumpsum.WipsImsC33uInput;

@SuppressWarnings("javadoc")
public class C33uTransactionOutput extends WipsImsOutput {

    private List<String> paymentDescription;
    private List<LsClassification> classification;
    private String prePayment;
    private String workTaskNumber;
    private String accountNumber;
    private String subDivisionNumber;
    private String deptNumber;
    private String longTermCost;
    private String shortTermCurrency;
    private String longTermCurrency;
    private String shortTermCost;
    private String selectedClassification;
    private boolean hasPaymentConfirmed = false;
    private String modelYear;
    private List<String> models;
    private WipsImsC33uInput wipsImsC33uInput;

    public String getShortTermCurrency() {
        return this.shortTermCurrency;
    }

    public void setShortTermCurrency(final String shortTermCurrency) {

        this.shortTermCurrency = shortTermCurrency;
    }

    public String getLongTermCurrency() {
        return this.longTermCurrency;
    }

    public void setLongTermCurrency(final String longTermCurrency) {

        this.longTermCurrency = longTermCurrency;
    }

    public String getModelYear() {
        return this.modelYear;
    }

    public void setModelYear(final String modelYear) {
        this.modelYear = modelYear;
    }

    public List<String> getModels() {
        return this.models;
    }

    public void setModels(final List<String> models) {
        this.models = models;
    }

    public boolean isHasPaymentConfirmed() {
        return this.hasPaymentConfirmed;
    }

    public void setHasPaymentConfirmed(final boolean hasPaymentConfirmed) {

        this.hasPaymentConfirmed = hasPaymentConfirmed;
    }

    public List<String> getPaymentDescription() {
        return this.paymentDescription;
    }

    public void setPaymentDescription(final List<String> paymentDescription) {
        this.paymentDescription = paymentDescription;
    }

    public String getPrePayment() {
        return this.prePayment;
    }

    public void setPrePayment(final String prePayment) {
        this.prePayment = prePayment;
    }

    public String getWorkTaskNumber() {
        return this.workTaskNumber;
    }

    public void setWorkTaskNumber(final String workTaskNumber) {
        this.workTaskNumber = workTaskNumber;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public void setAccountNumber(final String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getSubDivisionNumber() {
        return this.subDivisionNumber;
    }

    public void setSubDivisionNumber(final String subDivisionNumber) {
        this.subDivisionNumber = subDivisionNumber;
    }

    public String getDeptNumber() {
        return this.deptNumber;
    }

    public void setDeptNumber(final String deptNumber) {
        this.deptNumber = deptNumber;
    }

    public String getLongTermCost() {
        return this.longTermCost;
    }

    public void setLongTermCost(final String longTermCost) {
        this.longTermCost = longTermCost;
    }

    public String getShortTermCost() {
        return this.shortTermCost;
    }

    public void setShortTermCost(final String shortTermCost) {
        this.shortTermCost = shortTermCost;
    }

    public String getSelectedClassification() {
        return this.selectedClassification;
    }

    public void setSelectedClassification(final String selectedClassification) {
        this.selectedClassification = selectedClassification;
    }

    public List<LsClassification> getClassification() {
        return this.classification;
    }

    public void setClassification(final List<LsClassification> classification) {
        this.classification = classification;
    }

    public WipsImsC33uInput getWipsImsC33uInput() {
        return this.wipsImsC33uInput;
    }

    public void setWipsImsC33uInput(final WipsImsC33uInput wipsImsC33uInput) {
        this.wipsImsC33uInput = wipsImsC33uInput;
    }

}
