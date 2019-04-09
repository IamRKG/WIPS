//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.layer;

@SuppressWarnings("javadoc")
public class JobDetail {

    private String jobCode = null;
    private String jobName = null;

    public JobDetail(final String jobCode, final String jobName) {
        this.jobCode = jobCode;
        this.jobName = jobName;
    }

    public String getJobCode() {
        return this.jobCode;
    }

    public String getJobName() {
        return this.jobName;
    }

}
