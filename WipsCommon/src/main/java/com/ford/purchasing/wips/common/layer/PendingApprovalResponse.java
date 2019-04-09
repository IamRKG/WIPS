//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.layer;

import java.util.List;

@SuppressWarnings("javadoc")
public class PendingApprovalResponse extends WipsBaseResponse {

    private boolean isValidUser = false;
    private List<PendingApproval> pendingApprovals;
    private UserProfile userProfile;
    private JobDetail jobDetail;

    public boolean isValidUser() {
        return this.isValidUser;
    }

    public void setValidUser(final boolean isValidUser) {
        this.isValidUser = isValidUser;
    }

    public List<PendingApproval> getPendingApprovals() {
        return this.pendingApprovals;
    }

    public void setPendingApprovals(final List<PendingApproval> pendingApprovals) {
        this.pendingApprovals = pendingApprovals;
    }

    public List<JobDetail> getDelegatedJobs() {
        if (this.userProfile == null) {
            return null;
        }
        return this.userProfile.getDelegatedJobs(this.getJobDetail().getJobCode());
    }

    public void setUserProfile(final UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public UserProfile retrieveUserProfile() {
        return this.userProfile;
    }

    public JobDetail getJobDetail() {
        return this.jobDetail;
    }

    public void setJobDetail(final JobDetail jobDetail) {
        this.jobDetail = jobDetail;
    }

}
