//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.inbound.layer;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

import com.ford.it.rest.ic.FRestBaseIC;
import com.ford.purchasing.wips.common.layer.Category;
import com.ford.purchasing.wips.common.layer.PendingApprovalRequest;
import com.ford.purchasing.wips.common.layer.UserProfile;
import com.ford.purchasing.wips.inbound.layer.common.UserSessionCacheManager;

@SuppressWarnings("javadoc")
public class WipsRestBaseIC extends FRestBaseIC {

    @Inject
    @Default
    private UserSessionCacheManager userSessionCache;

    public UserProfile getUserProfile(final String lterm) {
        return this.userSessionCache.retrieveUserProfile(lterm);
    }

    public String getUserRacfIdFromCache(final String lterm) {
        final UserProfile userProfile = getUserProfile(lterm);
        return userProfile.getUserRacfId();
    }

    public void populateUserDetailsIntoRequest(
            final PendingApprovalRequest pendingApprovalRequest,
            final Category category) {
        pendingApprovalRequest.setCategory(category);
        final UserProfile userProfile = getUserProfile(pendingApprovalRequest.getLterm());
        pendingApprovalRequest.setCurrentJobCode(userProfile.getCurrentJobCode());
        pendingApprovalRequest.setJobCode(userProfile.getImsJobCode());
        pendingApprovalRequest.setUserRacfId(userProfile.getUserRacfId());
    }

}
