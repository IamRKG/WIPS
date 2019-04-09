//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.inbound.priceclaims;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ford.purchasing.wips.business.priceclaims.PriceClaimsBF;
import com.ford.purchasing.wips.common.layer.Category;
import com.ford.purchasing.wips.common.layer.UserProfile;
import com.ford.purchasing.wips.common.layer.WipsBaseResponse;
import com.ford.purchasing.wips.common.priceclaims.PriceClaimsApproveOrRejectResponse;
import com.ford.purchasing.wips.common.priceclaims.PriceClaimsRequest;
import com.ford.purchasing.wips.domain.priceclaims.service.PriceClaimsAS;
import com.ford.purchasing.wips.inbound.layer.WipsRestBaseIC;

@RequestScoped
@Path("/PriceClaims")
public class PriceClaimsIC extends WipsRestBaseIC {

    @Inject
    private PriceClaimsAS priceClaimsAS;

    @Inject
    private PriceClaimsBF priceClaimsBF;

    @GET
    @Path("/retrievePriceClaimsDetails/{ltermToken}/{priceClaimNumber}/{supplier}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrievePriceClaimDetails(
        @PathParam("ltermToken") final String ltermToken,
        @PathParam("priceClaimNumber") final String priceClaimNumber,
        @PathParam("supplier") final String supplier) {

        final PriceClaimsRequest priceClaimsRequest =
            priceClaimRequest(ltermToken, priceClaimNumber, supplier);
        final WipsBaseResponse priceClaimsResponse =
            this.priceClaimsAS.retrievePriceClaim(priceClaimsRequest);
        return buildResponse(priceClaimsResponse, priceClaimsResponse.getStatus());
    }

    @PUT
    @Path("/saveClaimDetails")
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveClaimDetails(
        final PriceClaimsRequest priceClaimsRequest) {
        WipsBaseResponse response = new WipsBaseResponse();
        priceClaimRequest(priceClaimsRequest);
        response = this.priceClaimsAS.savePriceClaimRemarks(priceClaimsRequest);
        return buildResponse(response, response.getStatus());
    }

    @PUT
    @Path("/approveClaimDetails")
    @Produces(MediaType.APPLICATION_JSON)
    public Response approveOrRejectClaimDetails(
        final PriceClaimsRequest priceClaimsRequest) {
        WipsBaseResponse response = new PriceClaimsApproveOrRejectResponse();
        priceClaimRequest(priceClaimsRequest);
        response = this.priceClaimsBF.saveRemarksAndApproveOrRejectPriceClaims(priceClaimsRequest);
        return buildResponse(response, response.getStatus());
    }

    private PriceClaimsRequest priceClaimRequest(final String ltermToken,
        final String priceClaimNumber, final String supplier) {
        final PriceClaimsRequest priceClaimsRequest = new PriceClaimsRequest();
        priceClaimsRequest.setLtermToken(ltermToken);
        priceClaimsRequest.setPriceClaimNumber(priceClaimNumber);
        final UserProfile userProfile = getUserProfile(priceClaimsRequest.getLterm());
        priceClaimsRequest.setCurrentJobCode(userProfile.getCurrentJobCode());
        priceClaimsRequest.setJobCode(userProfile.getImsJobCode());
        priceClaimsRequest.setUserRacfId(userProfile.getUserRacfId());
        priceClaimsRequest.setSupplier(supplier);
        priceClaimsRequest.setCategory(Category.PRICE_CLAIMS);
        priceClaimsRequest.setEntityNumber(priceClaimNumber);
        return priceClaimsRequest;
    }

    private PriceClaimsRequest priceClaimRequest(final String ltermToken,
        final String priceClaimNumber, final String supplier, final String selectedPM, final String selectedYear) {
        final PriceClaimsRequest priceClaimsRequest = priceClaimRequest(ltermToken, priceClaimNumber, supplier);
        priceClaimsRequest.setSelectedPM(selectedPM);
        priceClaimsRequest.setSelectedYear(selectedYear);
        return priceClaimsRequest;
    }

    private void priceClaimRequest(final PriceClaimsRequest priceClaimsRequest) {
        final UserProfile userProfile = getUserProfile(priceClaimsRequest.getLterm());
        priceClaimsRequest.setCurrentJobCode(userProfile.getCurrentJobCode());
        priceClaimsRequest.setJobCode(userProfile.getImsJobCode());
        priceClaimsRequest.setUserRacfId(userProfile.getUserRacfId());
        priceClaimsRequest.setUserRacfId(userProfile.getUserRacfId());
        priceClaimsRequest.setEntityNumber(priceClaimsRequest.getPriceClaimNumber());
        priceClaimsRequest.setCategory(Category.PRICE_CLAIMS);
    }

    @GET
    @Path("/retrieveFinancialImpactDetails/{ltermToken}/{priceClaimNumber}/{supplier}/{selectedPM}/{selectedYear}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveFinancialImpactDetails(
        @PathParam("ltermToken") final String ltermToken,
        @PathParam("priceClaimNumber") final String priceClaimNumber,
        @PathParam("supplier") final String supplier, @PathParam("selectedPM") final String selectedPM,
        @PathParam("selectedYear") final String selectedYear) {

        final PriceClaimsRequest priceClaimsRequest =
            priceClaimRequest(ltermToken, priceClaimNumber, supplier, selectedPM, selectedYear);
        final WipsBaseResponse priceClaimsResponse =
            this.priceClaimsAS.retrievePriceClaimFinancialInfo(priceClaimsRequest);
        return buildResponse(priceClaimsResponse, priceClaimsResponse.getStatus());
    }

}
