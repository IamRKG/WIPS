//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.inbound.login;

import java.io.UnsupportedEncodingException;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ford.purchasing.wips.common.layer.util.WipsUtil;
import com.ford.purchasing.wips.inbound.layer.WipsRestBaseIC;
import com.ford.purchasing.wips.inbound.layer.common.UserSessionCacheManager;

@RequestScoped
@Path("/UserSession/{lterm}")
public class WipsUserSessionIC extends WipsRestBaseIC {

    @Inject
    @Default
    private UserSessionCacheManager userSessionCache;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateUserSessionTimeOut(
            @PathParam("lterm") final String lterm) throws UnsupportedEncodingException {
        this.userSessionCache.retrieveUserSession(WipsUtil.decrpyt(lterm)).updateLastUpdateTime();
        return buildResponseWithStatus(Response.Status.OK);
    }

}
