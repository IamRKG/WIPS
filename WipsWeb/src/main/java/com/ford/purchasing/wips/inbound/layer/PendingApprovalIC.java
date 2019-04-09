package com.ford.purchasing.wips.inbound.layer;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ford.it.logging.ILogger;
import com.ford.it.logging.LogFactory;
import com.ford.purchasing.wips.business.layer.PendingApprovalBF;
import com.ford.purchasing.wips.common.layer.Category;
import com.ford.purchasing.wips.common.layer.PendingApprovalItemsResponse;
import com.ford.purchasing.wips.common.layer.PendingApprovalRequest;
import com.ford.purchasing.wips.common.layer.UserProfile;

@RequestScoped
@Path("/PendingApprovals/{ltermToken}/{categoryCode}")
@SuppressWarnings("javadoc")
public class PendingApprovalIC extends WipsRestBaseIC {

    private static final String CLASS_NAME = PendingApprovalIC.class.getName();
    private static ILogger log = LogFactory.getInstance().getLogger(CLASS_NAME);

    @Inject
    private PendingApprovalBF pendingApprovalBF;

    /**
     * Called using HTTP POST method to get details of a list of atps.
     *
     * @return response for atp details.
     * @throws Exception
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPendingApprovalList(
            @PathParam("ltermToken") final String ltermToken,
            @PathParam("categoryCode") final String categoryCode) throws Exception {
        final String methodName = "getPendingApprovalList";
        log.entering(CLASS_NAME, methodName);
        PendingApprovalItemsResponse pendingApprovalItemsResponse =
                new PendingApprovalItemsResponse();
        final Status status = Response.Status.OK;
        final PendingApprovalRequest pendingApprovalRequest =
                populatePendingApprovalRequest(categoryCode, ltermToken);
        pendingApprovalRequest.setJobTitle(getJobTitle(pendingApprovalRequest.getLterm()));
        pendingApprovalItemsResponse =
                this.pendingApprovalBF.getPendingApprovals(pendingApprovalRequest);
        log.exiting(CLASS_NAME, methodName);
        return buildResponse(pendingApprovalItemsResponse, status);

    }

    private PendingApprovalRequest populatePendingApprovalRequest(final String categoryCode,
            final String ltermToken) {
        final PendingApprovalRequest pendingApprovalRequest = new PendingApprovalRequest();
        pendingApprovalRequest.setLtermToken(ltermToken);
        // Populate current Job code & User RacfId from User Profile.
        populateUserDetailsIntoRequest(pendingApprovalRequest,
                Category.getCategory(categoryCode));
        return pendingApprovalRequest;
    }

    protected String getJobTitle(final String lterm) {
        final UserProfile userProfile = getUserProfile(lterm);
        return userProfile.getJobTitleDetails()
                .get(userProfile.getCurrentJobCode())
                .getJobTitle();
    }
}
