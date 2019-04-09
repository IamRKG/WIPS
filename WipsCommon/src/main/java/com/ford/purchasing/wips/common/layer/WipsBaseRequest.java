//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.layer;

import com.ford.purchasing.wips.common.layer.util.StringUtil;
import com.ford.purchasing.wips.common.layer.util.WipsUtil;

@SuppressWarnings("javadoc")
public class WipsBaseRequest {
    private String userRacfId;
    private String actionTaken;
    private String ltermToken;
    private String currentJobCode;
    private String jobCode;
    private String jobName;
    private String lterm;
    private String jobTitle;
    
    public WipsBaseRequest() {
        super();
    }

    public String getJobTitle() {
        return this.jobTitle;
    }

    public void setJobTitle(final String jobTitle) {

        this.jobTitle = jobTitle;
    }

    public String getUserRacfId() {
        return this.userRacfId;
    }

    public void setUserRacfId(final String userRacfId) {
        this.userRacfId = userRacfId;
    }

    public String getActionTaken() {
        return this.actionTaken;
    }

    public void setActionTaken(final String actionTaken) {
        this.actionTaken = actionTaken;
    }

    public String getLterm() {
        if (this.lterm == null) {
            this.lterm = WipsUtil.decrpyt(this.ltermToken);
        }
        return this.lterm;
    }

    public String getJobCode() {
        if (this.jobCode == null) {
            this.jobCode = StringUtil
                    .createBlankSpaces(4);
        }
        return this.jobCode;
    }

    public void setJobCode(final String jobCode) {
        this.jobCode = jobCode;
    }

    public String getJobName() {
        return this.jobName;
    }

    public void setJobName(final String jobName) {
        this.jobName = jobName;
    }

    public void setLtermToken(final String ltermToken) {
        this.ltermToken = ltermToken;
    }

    public String getCurrentJobCode() {
        return this.currentJobCode;
    }

    public void setCurrentJobCode(final String currentJobCode) {
        this.currentJobCode = currentJobCode;
    }

}
