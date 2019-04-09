package com.ford.purchasing.wips.common.lumpsum;

import com.ford.purchasing.wips.common.layer.PendingApprovalRequest;

@SuppressWarnings("javadoc")
public class LumpSumRequest extends PendingApprovalRequest {

    private String lumpSumNumber;
    private String requestedAmendmentVersion;
    private String selectedClassification;
    private String selectedGsaAudit;
    private String workTaskNumber;
    private String shortTermCost;
    private String longTermCost;
    private String gsaAmount;
    private boolean group1Save;
    private boolean group2Save;
    private boolean group3Save;
    private String totalAmount;
    private String[] userRemarks;

    public String[] getUserRemarks() {
        return this.userRemarks;
    }

    public String getTotalAmount() {
        return this.totalAmount;
    }

    public void setTotalAmount(final String totalAmount) {

        this.totalAmount = totalAmount;
    }

    public void setUserRemarks(final String[] userRemarks) {

        this.userRemarks = userRemarks;

    }

    public boolean isGroup1Save() {
        return this.group1Save;
    }

    public void setGroup1Save(final boolean group1Save) {

        this.group1Save = group1Save;
    }

    public boolean isGroup2Save() {
        return this.group2Save;
    }

    public void setGroup2Save(final boolean group2Save) {

        this.group2Save = group2Save;
    }

    public boolean isGroup3Save() {
        return this.group3Save;
    }

    public void setGroup3Save(final boolean group3Save) {

        this.group3Save = group3Save;
    }

    public String getLumpSumNumber() {
        return this.lumpSumNumber;
    }

    public void setLumpSumNumber(final String lumpSumNumber) {
        this.lumpSumNumber = lumpSumNumber;
    }

    public String getSelectedClassification() {
        return this.selectedClassification;
    }

    public void setSelectedClassification(final String selectedClassification) {
        this.selectedClassification = selectedClassification;
    }

    public String getSelectedGsaAudit() {
        return this.selectedGsaAudit;
    }

    public void setSelectedGsaAudit(final String selectedGsaAudit) {
        this.selectedGsaAudit = selectedGsaAudit;
    }

    public String getWorkTaskNumber() {
        return this.workTaskNumber;
    }

    public void setWorkTaskNumber(final String workTaskNumber) {
        this.workTaskNumber = workTaskNumber;
    }

    public String getShortTermCost() {
        return this.shortTermCost;
    }

    public void setShortTermCost(final String shortTermCost) {
        this.shortTermCost = shortTermCost;
    }

    public String getLongTermCost() {
        return this.longTermCost;
    }

    public void setLongTermCost(final String longTermCost) {
        this.longTermCost = longTermCost;
    }

    public String getRequestedAmendmentVersion() {
        return this.requestedAmendmentVersion;
    }

    public void setRequestedAmendmentVersion(final String requestedAmendmentVersion) {
        this.requestedAmendmentVersion = requestedAmendmentVersion;
    }

    public String getGsaAmount() {
        return this.gsaAmount;
    }

    public void setGsaAmount(final String gsaAmount) {

        this.gsaAmount = gsaAmount;
    }

}
