package com.ford.purchasing.wips.inbound.lumpsum;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ford.purchasing.wips.common.layer.Category;
import com.ford.purchasing.wips.common.layer.UserProfile;
import com.ford.purchasing.wips.common.layer.WipsBaseResponse;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.util.StringUtil;
import com.ford.purchasing.wips.common.lumpsum.LumpSumApproveOrRejectResponse;
import com.ford.purchasing.wips.common.lumpsum.LumpSumRequest;
import com.ford.purchasing.wips.domain.lumpsum.beans.LumpSumResponse;
import com.ford.purchasing.wips.inbound.layer.WipsRestBaseIC;

@RequestScoped
@Path("/ViewLumpSumDetails")
public class LumpSumDetailIC extends WipsRestBaseIC {

    @Inject
    private LumpSumDetailBF lumpSumDetailBF;

    @GET
    @Path("/retrieveLumpSumDetails/{ltermToken}/{lumpSumNumber}/{amendmentVersion}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveLumpSumDetails(
            @PathParam("ltermToken") final String ltermToken,
            @PathParam("lumpSumNumber") final String lumpSumNumber,
            @PathParam("amendmentVersion") final String amendmentVersion) {
        final LumpSumRequest lumpSumRequest =
                createLumsumRequest(ltermToken, lumpSumNumber, amendmentVersion);
        final LumpSumResponse lumpSumResponse =
                this.lumpSumDetailBF.getLumpSumDetail(lumpSumRequest);
        return buildResponse(lumpSumResponse, lumpSumResponse.getStatus());
    }

    @PUT
    @Path("/ConfirmPaymentDesc")
    @Produces(MediaType.APPLICATION_JSON)
    public Response confirmPaymentDesc(final LumpSumRequest lumpsumRequest) {
        populateUserDetailsIntoRequest(lumpsumRequest, Category.LUMPSUM);
        final WipsBaseResponse wipsBaseResponse =
                this.lumpSumDetailBF.confirmPaymentDescription(lumpsumRequest);
        return buildResponse(wipsBaseResponse, wipsBaseResponse.getStatus());
    }

    @PUT
    @Path("/saveLumpSumDetails")
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveLumpSumDetails(
            final LumpSumRequest lumpsumRequest) {
        populateUserDetailsIntoRequest(lumpsumRequest, Category.LUMPSUM);
        LumpSumResponse lumpSumResponse = new LumpSumResponse();
        if (isCostConditionSatisfied(lumpsumRequest)) {
            lumpsumRequest.setJobTitle(getJobTitle(lumpsumRequest.getLterm()));
            lumpSumResponse = this.lumpSumDetailBF.saveLumpSumDetail(lumpsumRequest);
        } else {
            lumpSumResponse.setStatus(Response.Status.ACCEPTED);
            lumpSumResponse.setErrorFlag(true);
            lumpSumResponse.setErrorMessage(
                    "Sum of Short term and long term cost should be equal to Total Cost");
        }
        lumpSumResponse.getLumpSumInformation().setHasAttachments(WipsConstant.YES);
        return buildResponse(lumpSumResponse, lumpSumResponse.getStatus());
    }

    @PUT
    @Path("/SaveAndApproveOrRejectLumpSumDetails")
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveAndApproveOrRejectLumpSumDetails(
            final LumpSumRequest lumpsumRequest) {
        lumpsumRequest.setRequestedAmendmentVersion(null);
        populateUserDetailsIntoRequest(lumpsumRequest, Category.LUMPSUM);
        LumpSumApproveOrRejectResponse response = new LumpSumApproveOrRejectResponse();
        if (isCostConditionSatisfied(lumpsumRequest)) {
            lumpsumRequest.setJobTitle(getJobTitle(lumpsumRequest.getLterm()));
            response =
                    this.lumpSumDetailBF.saveAndApproveOrRejectLumpSumDetails(lumpsumRequest);
        } else {
            response.setErrorFlag(true);
            response.setErrorMessage(
                    "Sum of Short term and long term cost should be equal to Total Cost");
        }
        return buildResponse(response, response.getStatus());
    }

    private boolean isCostConditionSatisfied(final LumpSumRequest request) {
        String shortTerm = request.getShortTermCost(), longTerm =
                request.getLongTermCost();
        double shortTermCost = 0, longTermCost = 0;
        if (shortTerm != null || longTerm != null) {
            if (shortTerm != null) {
                shortTerm = StringUtil.retrieveCost(shortTerm);
                shortTermCost = Double.valueOf(shortTerm);
            }
            if (longTerm != null) {
                longTerm = StringUtil.retrieveCost(longTerm);
                longTermCost = Double.valueOf(longTerm);
            }
            if (Double.compare(shortTermCost + longTermCost,
                    Double.valueOf(request.getTotalAmount())) == 0)
                return true;
            else
                return false;
        }
        return true;
    }

    private LumpSumRequest createLumsumRequest(
            final String ltermToken, final String lumsumNumber,
            final String amendmentVersion) {
        final LumpSumRequest lumpSumRequest = new LumpSumRequest();
        lumpSumRequest.setLumpSumNumber(lumsumNumber);
        lumpSumRequest.setLtermToken(ltermToken);
        lumpSumRequest.setRequestedAmendmentVersion(amendmentVersion);
        lumpSumRequest.setJobTitle(getJobTitle(lumpSumRequest.getLterm()));
        // Populate current Job code & User RacfId from User Profile.
        populateUserDetailsIntoRequest(lumpSumRequest, Category.LUMPSUM);
        return lumpSumRequest;
    }

    protected String getJobTitle(final String lterm) {
        final UserProfile userProfile = getUserProfile(lterm);
        return userProfile.getJobTitleDetails()
                .get(userProfile.getCurrentJobCode())
                .getJobTitle();
    }
}
