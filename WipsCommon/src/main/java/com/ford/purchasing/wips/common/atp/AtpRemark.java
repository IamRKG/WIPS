//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.atp;

import java.util.ArrayList;
import java.util.List;

public class AtpRemark {
    private String user;
    private String userJobCode;
    private List<String> remarks = new ArrayList<String>();

    public String getUser() {
        return this.user;
    }

    public void setUser(final String user) {
        this.user = user;
    }

    public List<String> getRemarks() {
        return this.remarks;
    }

    public void setRemarks(final List<String> remarks) {
        this.remarks = remarks;
    }

    public String getUserJobCode() {
        return this.userJobCode;
    }

    public void setUserJobCode(final String userJobCode) {
        this.userJobCode = userJobCode;
    }

}
