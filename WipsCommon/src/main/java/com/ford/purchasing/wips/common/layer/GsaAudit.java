//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.layer;

@SuppressWarnings({"javadoc"})
public class GsaAudit {

    private String code;
    private String description;

    public GsaAudit(final String code, final String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }

}
