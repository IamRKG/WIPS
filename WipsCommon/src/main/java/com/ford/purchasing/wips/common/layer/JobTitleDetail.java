//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.layer;

@SuppressWarnings("javadoc")
public class JobTitleDetail {

    private JobDetail jobCodeDetail;
    private String jobTitle;

    public JobTitleDetail() {
    }

    public JobTitleDetail(final JobDetail jobCodeDetail, final String jobTitle) {
        this.jobCodeDetail = jobCodeDetail;
        this.jobTitle = jobTitle;
    }

    public String getJobTitle() {
        return this.jobTitle;
    }

    public JobDetail getJobCodeDetail() {
        return this.jobCodeDetail;
    }

}
