package com.ford.purchasing.wips.common.layer;

public class TextMessageOptinResponse extends WipsBaseResponse {
    private String cdsId;
    private String optedForSms;
    private String auxiliaryPhone;
    private String cdsName;

    public String getCdsName() {
        return this.cdsName;
    }

    public void setCdsName(final String cdsName) {

        this.cdsName = cdsName;
    }

    public String getAuxiliaryPhone() {
        return auxiliaryPhone;
    }

    public void setAuxiliaryPhone(final String auxiliaryPhone) {
        this.auxiliaryPhone = auxiliaryPhone;
    }

    public String getOptedForSms() {
        return optedForSms;
    }

    public String getCdsId() {
        return cdsId;
    }

    public void setCdsId(final String cdsId) {
        this.cdsId = cdsId;
    }

    public String isOptedForSms() {
        return optedForSms;
    }

    public void setOptedForSms(final String optedForSms) {
        this.optedForSms = optedForSms;
    }

}
