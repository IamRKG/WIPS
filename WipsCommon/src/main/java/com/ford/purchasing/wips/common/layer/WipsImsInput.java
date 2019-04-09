//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.layer;

import com.ford.it.connector.record.DataArea;

@SuppressWarnings("javadoc")
public class WipsImsInput {

    private String userRacfId;
    private String tpiIoindic;
    private String tpiOption;
    private short tpiPfKey;
    private String lterm;
    private String imsAction;
    private String jobCode;
    private String userLoginPassword;
    private String tpiPageNo;
    private String tpiSelect;

    public String getUserRacfId() {
        return this.userRacfId;
    }

    public void setUserRacfId(final String userRacfId) {
        this.userRacfId = userRacfId;
    }

    public String getTpiIoindic() {
        return this.tpiIoindic;
    }

    public void setTpiIoindic(final String tpiIoindic) {
        this.tpiIoindic = tpiIoindic;
    }

    public short getTpiPfKey() {
        return this.tpiPfKey;
    }

    public void setTpiPfKey(final short tpiPfKey) {
        this.tpiPfKey = tpiPfKey;
    }

    public String getLterm() {
        return this.lterm;
    }

    public void setLterm(final String lterm) {

        this.lterm = lterm;
    }

    public String getImsAction() {
        return this.imsAction;
    }

    public void setImsAction(final String imsAction) {
        this.imsAction = imsAction;
    }

    public String getJobCode() {
        return this.jobCode;
    }

    public void setJobCode(final String jobCode) {
        this.jobCode = jobCode;
    }

    public String getTpiOption() {
        return this.tpiOption;
    }

    public void setTpiOption(final String tpiOption) {
        this.tpiOption = tpiOption;
    }

    public String getUserLoginPassword() {
        return this.userLoginPassword;
    }

    public void setUserLoginPassword(final String userLoginPassword) {
        this.userLoginPassword = userLoginPassword;
    }

    public String getTpiPageNo() {
        return this.tpiPageNo;
    }

    public void setTpiPageNo(final String tpiPageNo) {
        this.tpiPageNo = tpiPageNo;
    }

    public String getTpiSelect() {
        return this.tpiSelect;
    }

    public void setTpiSelect(final String tpiSelect) {
        this.tpiSelect = tpiSelect;
    }

    public void populateM00m(final DataArea inputDataArea) {
        inputDataArea.put("TP-INPUT-FIELDS.TPI-IOINDIC", this.getTpiIoindic());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-PFKEY", this.getTpiPfKey());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-PAGENO", this.getTpiPageNo());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-OPTION", this.getTpiOption());
        inputDataArea.put("TP-INPUT-FIELDS.TPI-ALTCODE", this.getJobCode());
    }

    public WipsImsInput populateSwitchJobCodeInput(final WipsBaseRequest priceClaimsRequest) {
        this.setTpiIoindic(WipsConstant.InputI);
        this.setTpiPfKey(WipsConstant.PFKEY0);
        this.setUserRacfId(priceClaimsRequest.getUserRacfId());
        this.setTpiOption(WipsConstant.BLANK_SPACE_3);
        this.setJobCode(priceClaimsRequest.getJobCode());
        this.setTpiPageNo(WipsConstant.PAGENUMBERONE);
        this.setLterm(priceClaimsRequest.getLterm());
        return this;
    }

    public WipsImsInput populateM00mlInput(final PendingApprovalRequest request) {
        this.setTpiIoindic(WipsConstant.InputI);
        this.setTpiPfKey(WipsConstant.PFKEY0);
        this.setUserRacfId(request.getUserRacfId());
        this.setTpiOption(request
                .getCategory().getCategoryCode());
        this.setJobCode(request.getJobCode());
        this.setTpiPageNo(WipsConstant.PAGENUMBERONE);
        this.setLterm(request.getLterm());
        return this;
    }
}
