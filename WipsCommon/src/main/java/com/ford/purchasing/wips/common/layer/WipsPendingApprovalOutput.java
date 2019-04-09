package com.ford.purchasing.wips.common.layer;

@SuppressWarnings("javadoc")
public class WipsPendingApprovalOutput {

    private String entityNumber;

    private String buyerCode;

    private String readOrUnreadFlag;

    private String subsequentProgram;

    public String getEntityNumber() {
        return this.entityNumber;
    }

    public void setEntityNumber(final String entityNumber) {
        this.entityNumber = entityNumber;
    }

    public String getBuyerCode() {
        return this.buyerCode;
    }

    public void setBuyerCode(final String buyerCode) {
        this.buyerCode = buyerCode;
    }

    public String getReadOrUnreadFlag() {
        return this.readOrUnreadFlag;
    }

    public void setReadOrUnreadFlag(final String readOrUnreadFlag) {
        this.readOrUnreadFlag = readOrUnreadFlag;
    }

    public String getSubsequentProgram() {
        return this.subsequentProgram;
    }

    public void setSubsequentProgram(final String subsequentProgram) {
        this.subsequentProgram = subsequentProgram;
    }

}
