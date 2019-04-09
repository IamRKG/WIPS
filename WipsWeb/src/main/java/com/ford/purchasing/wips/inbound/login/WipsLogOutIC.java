/**
 *
 */
package com.ford.purchasing.wips.inbound.login;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ford.it.rest.ic.FRestBaseIC;
import com.ford.purchasing.wips.common.layer.util.WipsUtil;
import com.ford.purchasing.wips.inbound.layer.common.UserSessionCacheManager;

/**
 * This Inbound Controller provides an end point to process log out request.
 */
@SuppressWarnings("javadoc")
@RequestScoped
@Path("/WipsLogOut/{ltermToken}")
public class WipsLogOutIC extends FRestBaseIC {

    @Inject
    @Default
    private UserSessionCacheManager userSessionCache;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response logOut(@PathParam("ltermToken") final String ltermToken) {
        this.userSessionCache.deleteSessionEntry(WipsUtil.decrpyt(ltermToken));
        return buildResponseWithStatus(Response.Status.OK);
    }
}
