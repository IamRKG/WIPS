//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.layer;

@SuppressWarnings("javadoc")
public class SupplierDetail {
    private String siteCode;
    private String siteName;

    public String getSiteCode() {
        return this.siteCode;
    }

    public void setSiteCode(final String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteName() {
        return this.siteName;
    }

    public void setSiteName(final String siteName) {
        this.siteName = siteName;
    }

}
