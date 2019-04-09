//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.layer;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("javadoc")
public class ApprovalDetail {

    private String jobCode;
    private String approverName;
    private String date;
    private String status;
    private List<String> remarks = new ArrayList<String>();
    private String moreRemarksPage;
    private String enterUser;

    public List<String> getRemarks() {
        return this.remarks;
    }

    public void setRemarks(final List<String> list) {
        this.remarks = list;
    }

    public String getJobCode() {
        return this.jobCode;
    }

    public void setJobCode(final String jobCode) {
        this.jobCode = jobCode;
    }

    public String getApproverName() {
        return this.approverName;
    }

    public void setApproverName(final String approverName) {
        this.approverName = approverName;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    /**
     * @return Returns the moreRemarksPage.
     */
    public String getMoreRemarksPage() {
        return this.moreRemarksPage;
    }

    /**
     * @param moreRemarksPage The moreRemarksPage to set.
     */
    public void setMoreRemarksPage(final String moreRemarksPage) {
        this.moreRemarksPage = moreRemarksPage;
    }

    /**
     * @return Returns the enterUser.
     */
    public String getEnterUser() {
        return this.enterUser;
    }

    /**
     * @param enterUser The enterUser to set.
     */
    public void setEnterUser(final String enterUser) {
        this.enterUser = enterUser;
    }
}
