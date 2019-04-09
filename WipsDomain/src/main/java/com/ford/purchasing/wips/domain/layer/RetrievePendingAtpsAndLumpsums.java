//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.layer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import com.ford.purchasing.wips.common.atp.EppsTransactionInput;
import com.ford.purchasing.wips.common.connector.FunctionThrowsException;
import com.ford.purchasing.wips.common.connector.Predicate;
import com.ford.purchasing.wips.common.layer.PendingApprovalItemsResponse;
import com.ford.purchasing.wips.common.layer.PendingApprovalRequest;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.WipsLumsumOutput;
import com.ford.purchasing.wips.common.layer.WipsPendingApprovalOutput;
import com.ford.purchasing.wips.domain.atp.beans.M00mTransactionInput;
import com.ford.purchasing.wips.domain.atp.beans.M00mTransactionOutput;
import com.ford.purchasing.wips.domain.atp.beans.Q01xTransactionInput;
import com.ford.purchasing.wips.domain.atp.beans.Q01xTransactionOutput;
import com.ford.purchasing.wips.domain.connector.Function;
import com.ford.purchasing.wips.domain.connector.ImsConversationCtx;
import com.ford.purchasing.wips.domain.connector.ImsTransaction;
import com.ford.purchasing.wips.domain.dao.db2.JobDetailDAO;
import com.ford.purchasing.wips.domain.dao.db2.SupplierDetailDAO;
import com.ford.purchasing.wips.domain.layer.exception.WipsDb2DatabaseException;

@SuppressWarnings("javadoc")
public class RetrievePendingAtpsAndLumpsums
        implements
        Function<ImsConversationCtx, ImsTransaction<PendingApprovalItemsResponse>> {

    private PendingApprovalRequest pendingApprovalRequest;

    private static final String FORECAST_LUMPSUM = "FORCST";
    @Inject
    private SupplierDetailDAO supplierDetailDAO;

    @Inject
    private JobDetailDAO jobDetailDAO;

    @Inject
    public RetrievePendingAtpsAndLumpsums() {
    }

    public RetrievePendingAtpsAndLumpsums(
            final PendingApprovalRequest pendingApprovalRequest) {
        this.pendingApprovalRequest = pendingApprovalRequest;

    }

    @Override
    public ImsTransaction<PendingApprovalItemsResponse> apply(
            final ImsConversationCtx ctx) {
        final PendingApprovalItemsResponse pendingApprovalItemsResponse =
                new PendingApprovalItemsResponse();
        final List<WipsPendingApprovalOutput> priceClaims =
                new ArrayList<WipsPendingApprovalOutput>();
        return ctx
                .transact(WipsTransactionConstant.AAIMEPPS_TRANSACTION_NAME,
                        this.pendingApprovalRequest,
                        new EppsTransactionInput().loadEppsInput(),
                        new M00mTransactionOutput().getEppsOutput())
                .thenTransactIf(new M00mTransactionOutput()
                        .notUserJobCode(this.pendingApprovalRequest),
                        WipsTransactionConstant.AAIMM00M_TRANSACTION_NAME,
                        new M00mTransactionInput()
                                .loadM00mSwitchJobInput(
                                        this.pendingApprovalRequest),
                        new M00mTransactionOutput().getM00mSwitchJobOutput())
                .thenTransact(
                        WipsTransactionConstant.AAIMM00M_Q01X_TRANSACTION_NAME,
                        new M00mTransactionInput()
                                .loadM00mInput(
                                        RetrievePendingAtpsAndLumpsums.this.pendingApprovalRequest),
                        new Q01xTransactionOutput()
                                .getQ01xOutput(priceClaims))
                .thenTransactWhile(
                        thereAreMorePagesOfAtpsOrLumpsums(
                                RetrievePendingAtpsAndLumpsums.this.pendingApprovalRequest),
                        WipsTransactionConstant.Q01X_TRANSACTION,
                        new Q01xTransactionInput()
                                .loadQ01xInput(
                                        RetrievePendingAtpsAndLumpsums.this.pendingApprovalRequest,
                        WipsConstant.PFKEY8),
                        new Q01xTransactionOutput()
                                .getQ01xOutput(
                                        priceClaims))
                .map(allPendingItemsToResponse(this.pendingApprovalRequest,
                        pendingApprovalItemsResponse,
                        priceClaims));
    }

    private FunctionThrowsException<Q01xTransactionOutput, PendingApprovalItemsResponse, WipsDb2DatabaseException> allPendingItemsToResponse(
            final PendingApprovalRequest pendingApprovalRequest,
            final PendingApprovalItemsResponse pendingApprovalItemsResponse,
            final List<WipsPendingApprovalOutput> priceClaims) {
        return new FunctionThrowsException<Q01xTransactionOutput, PendingApprovalItemsResponse, WipsDb2DatabaseException>() {

            @Override
            public PendingApprovalItemsResponse apply(final Q01xTransactionOutput summary)
                    throws WipsDb2DatabaseException {
                populateBuyerNames(priceClaims);
                if (pendingApprovalRequest.getCategory()
                        .getCategoryCode()
                        .equals(WipsConstant.LUMPSUM_ENTITY_CODE)) {
                    if (pendingApprovalRequest.getJobTitle()
                            .equalsIgnoreCase(WipsConstant.FINANCE_ANALYST_CODE)) {
                        removeForeCastLumpsums(pendingApprovalRequest, priceClaims);
                    }
                    populateSupplierNames(priceClaims);
                    pendingApprovalItemsResponse.setLumpSum(priceClaims);
                } else
                    pendingApprovalItemsResponse.setAtp(priceClaims);
                pendingApprovalItemsResponse.setCategoryCode(pendingApprovalRequest
                        .getCategory().getCategoryCode());
                pendingApprovalItemsResponse.setUserRacfId(pendingApprovalRequest
                        .getUserRacfId());
                pendingApprovalItemsResponse.setErrorFlag(false);
                return pendingApprovalItemsResponse;
            }
        };
    }

    private void removeForeCastLumpsums(final PendingApprovalRequest pendingApprovalRequest,
            final List<WipsPendingApprovalOutput> pendingList) {
        for (int count = 0; count < pendingList.size(); count++) {
            if ((FORECAST_LUMPSUM).equalsIgnoreCase(
                    ((WipsLumsumOutput)pendingList.get(count)).getCause())) {
                pendingList.remove(count--);
            }
        }
    }

    private Predicate<Q01xTransactionOutput> thereAreMorePagesOfAtpsOrLumpsums(
            final PendingApprovalRequest pendingApprovalRequest) {

        return new Predicate<Q01xTransactionOutput>() {
            @Override
            public boolean test(final Q01xTransactionOutput q43lTransactionOutput) {
                if (!pendingApprovalRequest.getCategory()
                        .getCategoryCode()
                        .equals(WipsConstant.PRICE_CLAIMS_ENTITY_CODE))
                    return q43lTransactionOutput.getContainMorePages()
                            .contains(WipsConstant.MORE);
                return false;
            }
        };

    }

    void populateSupplierNames(
            final List<WipsPendingApprovalOutput> lumpsumPendingApprovalList)
            throws WipsDb2DatabaseException {
        final Set<String> supplierCodes = new HashSet<String>();
        WipsLumsumOutput output = null;
        for (int count = 0; count < lumpsumPendingApprovalList.size(); count++) {
            output = (WipsLumsumOutput)lumpsumPendingApprovalList.get(count);
            supplierCodes.add(output.getSupplierCode());
        }
        final Map<String, String> supplierDetails = this.supplierDetailDAO
                .retrieveSupplierCodesAndNames(supplierCodes);
        for (int count = 0; count < lumpsumPendingApprovalList.size(); count++) {
            output = (WipsLumsumOutput)lumpsumPendingApprovalList.get(count);
            if (!WipsConstant.MANY.equals(output.getSupplierCode())) {
                output.setSupplierCode(supplierDetails.get(output
                        .getSupplierCode()));
            }
        }
    }

    void populateBuyerNames(final List<WipsPendingApprovalOutput> listOfPendingApprovals)
            throws WipsDb2DatabaseException {
        final Set<String> buyerCodes = new HashSet<String>();
        WipsPendingApprovalOutput wipsPendingApprovalOutput = null;
        for (int count = 0; count < listOfPendingApprovals.size(); count++) {
            wipsPendingApprovalOutput =
                    (WipsPendingApprovalOutput)listOfPendingApprovals.get(count);
            buyerCodes.add(wipsPendingApprovalOutput.getBuyerCode());
        }
        final Map<String, String> buyerDetails =
                this.jobDetailDAO.retrieveBuyerCodesAndNames(buyerCodes);
        for (int count = 0; count < listOfPendingApprovals.size(); count++) {
            wipsPendingApprovalOutput = (WipsPendingApprovalOutput)listOfPendingApprovals
                    .get(count);
            wipsPendingApprovalOutput
                    .setBuyerCode(buyerDetails.get(wipsPendingApprovalOutput
                            .getBuyerCode()));
        }
    }

    public void setPendingApprovalRequest(
            final PendingApprovalRequest pendingApprovalRequest2) {
        this.pendingApprovalRequest = pendingApprovalRequest2;
    }

}
