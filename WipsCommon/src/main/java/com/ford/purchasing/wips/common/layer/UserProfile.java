//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.layer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ford.purchasing.wips.common.layer.util.StringUtil;

@SuppressWarnings("javadoc")
public class UserProfile {

    private String userRacfId;
    private String userJobCode;
    private String switchJobCode = StringUtil.createBlankSpaces(4);
    private Map<String, JobTitleDetail> jobTitleDetails;

    public List<JobDetail> getDelegatedJobs(final String userJobCode) {
        final List<JobDetail> list = new ArrayList<JobDetail>();
        String jobCode;
        for (final JobTitleDetail jobTitleDetail : this.jobTitleDetails.values()) {
            jobCode = jobTitleDetail.getJobCodeDetail().getJobCode();
            if (!userJobCode.equals(jobCode)) {
                list.add(this.jobTitleDetails.get(jobCode).getJobCodeDetail());
            }
        }
        return list;
    }

    public Map<String, JobTitleDetail> getJobTitleDetails() {
        return this.jobTitleDetails;
    }

    public void setJobTitleDetails(final Map<String, JobTitleDetail> jobDetails) {
        this.jobTitleDetails = jobDetails;
    }

    public String getUserRacfId() {
        return this.userRacfId;
    }

    public void setUserRacfId(final String userRacfId) {
        this.userRacfId = userRacfId;
    }

    public String getUserJobCode() {
        return this.userJobCode;
    }

    public void setUserJobCode(final String userJobCode) {
        this.userJobCode = userJobCode;
    }

    public void setSwitchJobCode(final String switchJobCode) {
        this.switchJobCode = switchJobCode;
    }

    public String getCurrentJobCode() {
        if (!this.switchJobCode.equals(StringUtil.createBlankSpaces(4))) {
            return this.switchJobCode;
        }
        return this.userJobCode;
    }

    public String getImsJobCode() {
        String imsJobCode;
        if (this.userJobCode.equals(getCurrentJobCode())) {
            imsJobCode = StringUtil.createBlankSpaces(4);
        } else {
            imsJobCode = this.switchJobCode;
        }
        return imsJobCode;
    }

}
