package com.ford.purchasing.wips.common.layer;

@SuppressWarnings("javadoc")
public class LoginRequest {

    private String racfId;

    private String racfPassword;

    private String imsAction;

    public String getRacfId() {
        return this.racfId;
    }

    public void setRacfId(final String racfId) {
        this.racfId = racfId;
    }

    public String getRacfPassword() {
        return this.racfPassword;
    }

    public void setRacfPassword(final String racfPassword) {
        this.racfPassword = racfPassword.toUpperCase();
    }

    public String getImsAction() {
        return this.imsAction;
    }

    public void setImsAction(final String imsAction) {
        this.imsAction = imsAction;
    }

}
