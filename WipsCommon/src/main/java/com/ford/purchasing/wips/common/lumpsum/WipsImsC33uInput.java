package com.ford.purchasing.wips.common.lumpsum;

import com.ford.purchasing.wips.common.layer.WipsImsInput;

@SuppressWarnings("javadoc")
public class WipsImsC33uInput extends WipsImsInput {

    private String classification;
    private String workTaskNumber;
    private String longTermCost;
    private String longTermSign;
    private String shortTermCost;
    private String shortTermSign;
    private String cTeam;
    private String actionTaken;

    public String getcTeam() {
        return this.cTeam;
    }

    public void setcTeam(final String cTeam) {

        this.cTeam = cTeam;
    }

    public String getClassification() {
        return this.classification;
    }

    public void setClassification(final String classification) {
        this.classification = classification;
    }

    public String getWorkTaskNumber() {
        return this.workTaskNumber;
    }

    public void setWorkTaskNumber(final String workTaskNumber) {
        this.workTaskNumber = workTaskNumber;
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

    public String getActionTaken() {
        return this.actionTaken;
    }

    public void setActionTaken(final String actionTaken) {
        this.actionTaken = actionTaken;
    }

    public String getLongTermSign() {
        return this.longTermSign;
    }

    public void setLongTermSign(final String longTermSign) {
        this.longTermSign = longTermSign;
    }

    public String getShortTermSign() {
        return this.shortTermSign;
    }

    public void setShortTermSign(final String shortTermSign) {
        this.shortTermSign = shortTermSign;
    }

}
