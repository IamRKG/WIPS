//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.inbound.atp;

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
import com.ford.purchasing.wips.business.atp.AtpApprovalBF;
import com.ford.purchasing.wips.common.atp.AtpApproveOrRejectResponse;
import com.ford.purchasing.wips.common.atp.AtpRemarksRequest;
import com.ford.purchasing.wips.common.atp.AtpRequest;
import com.ford.purchasing.wips.common.layer.Category;
import com.ford.purchasing.wips.common.layer.WipsBaseResponse;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.domain.atp.AtpRemarksResponse;
import com.ford.purchasing.wips.inbound.layer.WipsRestBaseIC;

/**
 * This Inbound Controller provides an end point for all ATP Approval request.
 */
@RequestScoped
@Path("/AtpApproval")
public class AtpApprovalIC extends WipsRestBaseIC {

    private static final String CLASS_NAME = AtpApprovalIC.class.getName();
    private static ILogger log = LogFactory.getInstance().getLogger(CLASS_NAME);

    @Inject
    private AtpApprovalBF atpApprovalBF;

    /**
     * Handles ATP RECAP request to fetch the recap details from Mainframe source system.
     */
    @GET
    @Path("/Recap/{ltermToken}/{atpNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAtpRecap(
            @PathParam("atpNumber") final String atpNumber,
            @PathParam("ltermToken") final String ltermToken) {
        final String methodName = "getAtpRecap";
        log.entering(CLASS_NAME, methodName);
        final AtpRequest atpRecapRequest =
                createAtpRequest(atpNumber, ltermToken);
        final WipsBaseResponse atpRecapResponse =
                this.atpApprovalBF.getRecapDetails(atpRecapRequest);
        log.exiting(CLASS_NAME, methodName);
        return buildResponse(atpRecapResponse, atpRecapResponse.getStatus());
    }

    /**
     * Handles ATP Strategy request to fetch the Strategy details from Mainframe source
     * system.
     */
    @GET
    @Path("/Strategy/{ltermToken}/{atpNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStrategyDetails(
            @PathParam("ltermToken") final String ltermToken,
            @PathParam("atpNumber") final String atpNumber) {
        final String methodName = "getStrategyDetails";
        log.entering(CLASS_NAME, methodName);
        final AtpRequest strategyRequest =
                createAtpRequest(atpNumber, ltermToken);
        final WipsBaseResponse strategyResponse =
                this.atpApprovalBF.getStrategyDetails(strategyRequest);
        log.exiting(CLASS_NAME, methodName);
        return buildResponse(strategyResponse, strategyResponse.getStatus());
    }

    /**
     * Handles ATP ApproveOrReject request to process it from Mainframe source system.
     */
    @PUT
    @Path("/ApproveOrReject")
    @Produces(MediaType.APPLICATION_JSON)
    public Response approveOrRejectAtp(final AtpRemarksRequest atpRequest) {
        final String methodName = "approveOrRejectAtp";
        log.entering(CLASS_NAME, methodName);
        atpRequest.setEntityNumber(atpRequest.getAtpNumber());
        populateUserDetailsIntoRequest(atpRequest, Category.ATP);
        final WipsBaseResponse atpApproveOrRejectResponse =
                this.atpApprovalBF.approveOrRejectAtp(atpRequest);
        log.exiting(CLASS_NAME, methodName);
        return buildResponse(atpApproveOrRejectResponse,
                atpApproveOrRejectResponse.getStatus());
    }

    @GET
    @Path("/ATPRemarks/{ltermToken}/{atpNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getATPRemarks(
            @PathParam("atpNumber") final String atpNumber,
            @PathParam("ltermToken") final String ltermToken) {
        final String methodName = "getATPRemarks";
        log.entering(CLASS_NAME, methodName);
        final AtpRequest request = createAtpRequest(atpNumber, ltermToken);
        final WipsBaseResponse atpRemarksResponse = this.atpApprovalBF.getRemarks(request);
        log.exiting(CLASS_NAME, methodName);
        return buildResponse(atpRemarksResponse, atpRemarksResponse.getStatus());
    }

    @PUT
    @Path("/SaveATPRemarks")
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveATPRemarks(final AtpRemarksRequest atpRemarksRequest) {
        final String methodName = "saveATPRemarks";
        log.entering(CLASS_NAME, methodName);
        atpRemarksRequest.setCategory(Category.getCategory(WipsConstant.ATP_ENTITY_CODE));
        atpRemarksRequest.setEntityNumber(atpRemarksRequest.getAtpNumber());
        populateUserDetailsIntoRequest(atpRemarksRequest, Category.ATP);
        final AtpRemarksResponse atpRemarksResponse =
                this.atpApprovalBF.saveRemarks(atpRemarksRequest);
        log.exiting(CLASS_NAME, methodName);
        return buildResponse(atpRemarksResponse, atpRemarksResponse.getStatus());
    }

    /**
     * Handles ATP ApproveOrReject request to process it from Mainframe source system.
     */
    @PUT
    @Path("/SaveRemarksAndApproveOrRejectAtp")
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveRemarksAndApproveOrRejectAtp(
            final AtpRemarksRequest atpRemarksRequest) {
        final String methodName = "saveRemarksAndApproveOrRejectAtp";
        log.entering(CLASS_NAME, methodName);
        atpRemarksRequest.setCategory(Category.getCategory(WipsConstant.ATP_ENTITY_CODE));
        atpRemarksRequest.setEntityNumber(atpRemarksRequest.getAtpNumber());
        populateUserDetailsIntoRequest(atpRemarksRequest, Category.ATP);
        final AtpApproveOrRejectResponse response =
                this.atpApprovalBF.saveRemarksAndApproveOrRejectAtp(atpRemarksRequest);
        log.exiting(CLASS_NAME, methodName);
        return buildResponse(response, response.getStatus());
    }

    private AtpRequest createAtpRequest(
            final String atpNumber,
            final String ltermToken) {
        final AtpRequest atpRequest = new AtpRequest();
        atpRequest.setAtpNumber(atpNumber);
        atpRequest.setEntityNumber(atpNumber);
        atpRequest.setLtermToken(ltermToken);
        // Populate current Job code & User RacfId from User Profile.
        populateUserDetailsIntoRequest(atpRequest, Category.ATP);
        return atpRequest;
    }

}
