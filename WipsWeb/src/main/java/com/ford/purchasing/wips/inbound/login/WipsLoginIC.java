//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.inbound.login;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ford.it.logging.ILogger;
import com.ford.it.logging.LogFactory;
import com.ford.purchasing.wips.business.login.WipsLoginBF;
import com.ford.purchasing.wips.common.layer.LoginRequest;
import com.ford.purchasing.wips.common.layer.PendingApprovalResponse;
import com.ford.purchasing.wips.common.layer.UserProfile;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.util.WipsUtil;
import com.ford.purchasing.wips.inbound.layer.WipsRestBaseIC;
import com.ford.purchasing.wips.inbound.layer.common.UserSession;
import com.ford.purchasing.wips.inbound.layer.common.UserSessionCacheManager;

/**
 * This Inbound Controller provides an end point for all login request.
 */
@RequestScoped
@Path("/WipsLogin")
public class WipsLoginIC extends WipsRestBaseIC {

    private static final String CLASS_NAME = WipsLoginIC.class.getName();
    private static ILogger log = LogFactory.getInstance().getLogger(CLASS_NAME);

    @Inject
    @Default
    private UserSessionCacheManager userSessionCache;

    @Inject
    private WipsLoginBF wipsLoginBF;

    /**
     * Called using HTTP POST method to get the pending approvals & user delegated jobs.
     */
    @POST
    @Path("/UserAuth")
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticateUser(final LoginRequest loginRequest) {
        final String methodName = "authenticateUser";
        log.entering(CLASS_NAME, methodName);
        Status status = Response.Status.OK;
        loginRequest.setImsAction(WipsConstant.WIPS_LOGIN_ACTION);
        final PendingApprovalResponse pendingApprovalResponse =
                this.wipsLoginBF.authenticateUser(loginRequest);
        if (pendingApprovalResponse.isValidUser()) {
            final String lterm =
                    createUserSessionDetailsIntoCache(loginRequest.getRacfId(),
                            pendingApprovalResponse);
            pendingApprovalResponse.setLtermToken(WipsUtil.encrpyt(lterm));
        } else {
            status = Response.Status.BAD_REQUEST;
        }
        log.exiting(CLASS_NAME, methodName);
        return buildResponse(pendingApprovalResponse, status);
    }

    protected String createUserSessionDetailsIntoCache(final String userRacfId,
            final PendingApprovalResponse pendingApprovalResponse) {
        final UserSession userSession =
                this.userSessionCache.createNewUserSession(userRacfId);
        userSession.setUserProfile(createUserProfile(pendingApprovalResponse));
        return userSession.getLterm();
    }

    protected UserProfile createUserProfile(
            final PendingApprovalResponse pendingApprovalResponse) {
        final UserProfile userProfile = pendingApprovalResponse.retrieveUserProfile();
        userProfile.setUserRacfId(userProfile.getUserRacfId());
        userProfile.setUserJobCode(pendingApprovalResponse.getJobDetail().getJobCode());
        return userProfile;
    }
}
