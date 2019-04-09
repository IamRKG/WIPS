//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.atp;

public class Approvers {

    private String jobCode;
    private String approver;
    private String date;
    private String remarks;

    public String getJobCode() {
        return this.jobCode;
    }

    public void setJobCode(final String jobCode) {
        this.jobCode = jobCode;
    }

    public String getApprover() {
        return this.approver;
    }

    public void setApprover(final String approver) {
        this.approver = approver;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

}
