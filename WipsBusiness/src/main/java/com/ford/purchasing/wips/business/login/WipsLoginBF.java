//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.business.login;

import javax.inject.Inject;

import com.ford.it.logging.ILogger;
import com.ford.it.logging.LogFactory;
import com.ford.purchasing.wips.business.layer.WipsBaseBF;
import com.ford.purchasing.wips.common.layer.LoginRequest;
import com.ford.purchasing.wips.common.layer.PendingApprovalResponse;
import com.ford.purchasing.wips.domain.login.service.WipsLoginAS;

/**
 * WipsLoginBF used to verify user entered RACF id and password is valid or not.
 * if it is valid, it redirects to next page, else it will return corresponding
 * error to user.
 */
public class WipsLoginBF extends WipsBaseBF {

    private static final String CLASS_NAME = WipsLoginBF.class.getName();
    private static ILogger log = LogFactory.getInstance().getLogger(CLASS_NAME);

    @Inject
    private WipsLoginAS wipsLoginAS;

    /**
     * authenticateUser method is used to verify user RACF id and password
     */
    public PendingApprovalResponse authenticateUser(final LoginRequest loginRequest) {
        final String methodName = "getUserAuthentication";
        log.entering(CLASS_NAME, methodName);
        final PendingApprovalResponse pendingApprovalResponse = this.wipsLoginAS.processUserLoginRequest(loginRequest);
        log.exiting(CLASS_NAME, methodName);
        return pendingApprovalResponse;
    }

}
