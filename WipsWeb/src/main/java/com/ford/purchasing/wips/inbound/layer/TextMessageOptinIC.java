package com.ford.purchasing.wips.inbound.layer;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ford.it.logging.ILogger;
import com.ford.it.logging.LogFactory;
import com.ford.purchasing.wips.common.layer.TextMessageOptinRequest;
import com.ford.purchasing.wips.common.layer.TextMessageOptinResponse;
import com.ford.purchasing.wips.common.layer.UserProfile;
import com.ford.purchasing.wips.common.layer.WipsBaseResponse;
import com.ford.purchasing.wips.common.layer.util.WipsUtil;
import com.ford.purchasing.wips.domain.sms.service.TextMessageOptinAS;

@RequestScoped
@Path("/textMessage")
@SuppressWarnings("javadoc")
public class TextMessageOptinIC extends WipsRestBaseIC {

    private static final String CLASS_NAME = TextMessageOptinIC.class.getName();
    private static ILogger log = LogFactory.getInstance().getLogger(CLASS_NAME);
    @Inject
    TextMessageOptinAS textMessageOptinAS;

    @GET
    @Path("/retriveDetails/{ltermToken}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPendingApprovalList(
            @PathParam("ltermToken") final String ltermToken) {
        final String methodName = "getPendingApprovalList";
        log.entering(CLASS_NAME, methodName);
        final String racfId;
        final UserProfile userProfile = getUserProfile(WipsUtil.decrpyt(ltermToken));
        racfId = userProfile.getUserRacfId();
        final TextMessageOptinResponse retrieveCdsIdAndOptinOptout =
                this.textMessageOptinAS.returnAuxiliaryPhoneAndCDS(racfId.toUpperCase());
        log.exiting(CLASS_NAME, methodName);
        return buildResponse(retrieveCdsIdAndOptinOptout,
                retrieveCdsIdAndOptinOptout.getStatus());

    }

    @PUT
    @Path("/saveSelectedOption")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSelectedOption(final TextMessageOptinRequest request) {
        final String racfId = getUserProfile(request.getLterm()).getUserRacfId();
        final WipsBaseResponse response =
                this.textMessageOptinAS.updateOptedValue(request, racfId.toUpperCase());
        return buildResponse(response,
                response.getStatus());
    }
}
