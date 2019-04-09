//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.priceclaims.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.ford.purchasing.wips.common.atp.EppsTransactionInput;
import com.ford.purchasing.wips.common.connector.FunctionThrowsException;
import com.ford.purchasing.wips.common.connector.Predicate;
import com.ford.purchasing.wips.common.layer.ApprovalDetail;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.priceclaims.PCSBusinessUnit;
import com.ford.purchasing.wips.common.priceclaims.PriceClaimsRequest;
import com.ford.purchasing.wips.common.priceclaims.PurchasingManagerDetail;
import com.ford.purchasing.wips.domain.atp.beans.M00mTransactionInput;
import com.ford.purchasing.wips.domain.atp.beans.M00mTransactionOutput;
import com.ford.purchasing.wips.domain.connector.Function;
import com.ford.purchasing.wips.domain.connector.ImsConversationCtx;
import com.ford.purchasing.wips.domain.connector.ImsTransaction;
import com.ford.purchasing.wips.domain.dao.db2.PcCausalDescriptionDAO;
import com.ford.purchasing.wips.domain.layer.WipsTransactionConstant;
import com.ford.purchasing.wips.domain.layer.exception.WipsDb2DatabaseException;
import com.ford.purchasing.wips.domain.pcs.beans.A02lTransactionInput;
import com.ford.purchasing.wips.domain.pcs.beans.A02lTransactionOutput;
import com.ford.purchasing.wips.domain.pcs.beans.C36uTransactionInput;
import com.ford.purchasing.wips.domain.pcs.beans.C38vTransactionInput;
import com.ford.purchasing.wips.domain.pcs.beans.C38vTransactionOutput;
import com.ford.purchasing.wips.domain.pcs.beans.Cr1xTransactionInput;
import com.ford.purchasing.wips.domain.pcs.beans.Cr1xTransactionOutput;
import com.ford.purchasing.wips.domain.pcs.beans.Cr3vTransactionInput;
import com.ford.purchasing.wips.domain.pcs.beans.Cr3vTransactionOutput;
import com.ford.purchasing.wips.domain.pcs.beans.FinancialImpactInformation;
import com.ford.purchasing.wips.domain.pcs.beans.PriceClaimRemarks;
import com.ford.purchasing.wips.domain.pcs.beans.PriceClaimSummaryInformation;
import com.ford.purchasing.wips.domain.pcs.beans.Q43lTransactionInput;
import com.ford.purchasing.wips.domain.pcs.beans.Q43lTransactionOutput;
import com.ford.purchasing.wips.domain.priceclaims.beans.PriceClaimsResponse;

public class RetrievePriceClaim implements Function<ImsConversationCtx, ImsTransaction<PriceClaimsResponse>> {

    private PriceClaimsRequest priceClaimsRequest;

    @Inject
    private PcCausalDescriptionDAO pcCausalDescriptionDAO;

    @Inject
    public RetrievePriceClaim() {
    }

    public RetrievePriceClaim(final PriceClaimsRequest priceClaimsRequest) {
        this.priceClaimsRequest = priceClaimsRequest;
    }

    @Override
    public ImsTransaction<PriceClaimsResponse> apply(final ImsConversationCtx ctx) {
        final Map<String, ApprovalDetail> remarks = new LinkedHashMap<String, ApprovalDetail>();
        final List<String> purchaseManagers = new ArrayList<String>();
        final PriceClaimsResponse priceClaimsResponse = new PriceClaimsResponse();
        final List<PurchasingManagerDetail> purchasingManagerDetails = new ArrayList<PurchasingManagerDetail>();
        final List<PCSBusinessUnit> pcsBusinessUnits = new ArrayList<PCSBusinessUnit>();
        final A02lTransactionOutput a02lOutput = new A02lTransactionOutput();
        purchaseManagers.add(WipsConstant.ALL);
        return ctx
            .transact(WipsTransactionConstant.AAIMEPPS_TRANSACTION_NAME, this.priceClaimsRequest,
                new EppsTransactionInput().loadEppsInput(),
                new M00mTransactionOutput().getEppsOutput())
            .thenTransactIf(new M00mTransactionOutput().notUserJobCode(this.priceClaimsRequest),
                WipsTransactionConstant.AAIMM00M_TRANSACTION_NAME,
                new M00mTransactionInput().loadM00mSwitchJobInput(this.priceClaimsRequest),
                new M00mTransactionOutput().getM00mSwitchJobOutput())
            .thenTransact(WipsTransactionConstant.AAIMM00M_Q43L_TRANSACTION_NAME,
                new M00mTransactionInput().loadM00mInput(this.priceClaimsRequest),
                new Q43lTransactionOutput().getQ43lOutput())
            .thenTransact(WipsTransactionConstant.AAIMQ43L_C36U_TRANSACTION_NAME,
                new Q43lTransactionInput().loadQ43lInput(this.priceClaimsRequest,
                    WipsConstant.PFKEY15),
                new PriceClaimSummaryInformation().get(this.priceClaimsRequest, priceClaimsResponse))
            .map(toIntermediateResponse())
            .thenTransact(WipsTransactionConstant.AAIMC36U_A02L_TRANSACTION_NAME,
                C36uTransactionInput.loadC36uInput(WipsConstant.PFKEY22),
                A02lTransactionOutput.getA02lOutput(a02lOutput, remarks))
            .thenTransactWhile(moreApproversExist(),
                WipsTransactionConstant.AAIMA02L_A02L_TRANSACTION_NAME,
                A02lTransactionInput.loadA02lInput(WipsConstant.PFKEY8),
                A02lTransactionOutput
                    .getA02lOutputWithRemarks(a02lOutput, remarks))
            .thenTransact(WipsTransactionConstant.AAIMA02L_C36U_TRANSACTION_NAME,
                A02lTransactionInput.loadA02lInput(WipsConstant.PFKEY3),
               new PriceClaimSummaryInformation().retrieveC36uOutput())
            .thenTransact(WipsTransactionConstant.AAIMC36U_C38V_TRANSACTION_NAME,
                C36uTransactionInput.loadC36uInput(WipsConstant.PFKEY23),
                C38vTransactionOutput.getC38vOutput(WipsConstant.STAR_ALL, purchaseManagers, pcsBusinessUnits,
                    purchasingManagerDetails))
            .thenTransactWhile(C38vTransactionOutput.isMoreDataPredicate(),
                WipsTransactionConstant.AAIMC38V_TRANSACTION_NAME,
                C38vTransactionInput.loadC38vInput(WipsConstant.PFKEY8, null, null),
                C38vTransactionOutput.getC38vOutput(WipsConstant.STAR_ALL, purchaseManagers, pcsBusinessUnits,
                    purchasingManagerDetails))
            .map(financialInfoToResponse(priceClaimsResponse))
            .thenTransact(WipsTransactionConstant.AAIMC38V_CR1X_TRANSACTION_NAME,
                C38vTransactionInput.loadC38vInput(WipsConstant.PFKEY9, this.priceClaimsRequest.getSelectedPM(),
                    this.priceClaimsRequest.getSelectedYear()),
                Cr1xTransactionOutput.getCr1xOutput(remarks))
            .thenTransactWhile(moreRemarksExist(),
                WipsTransactionConstant.AAIMCR1X_TRANSACTION_NAME,
                Cr1xTransactionInput.loadCr1xInput(WipsConstant.PFKEY8,
                    this.priceClaimsRequest),
                Cr1xTransactionOutput.getCr1xOutput(remarks))
            .thenTransact(WipsTransactionConstant.AAIMCR1X_CR3V_TRANSACTION_NAME,
                Cr1xTransactionInput.loadCr1xInput(WipsConstant.PFKEY15,
                    this.priceClaimsRequest),
                Cr3vTransactionOutput.getCr3vOutput())
            .map(documentRemarksToResponse(priceClaimsResponse))
            .thenTransact(WipsTransactionConstant.AAIMCR3V_M00M_TRANSACTION_NAME,
                Cr3vTransactionInput.loadCr3vInput(),
                new M00mTransactionOutput().getEppsOutput())
            .map(remarksToApprovers(priceClaimsResponse, remarks));
    }

    private FunctionThrowsException<FinancialImpactInformation, FinancialImpactInformation, Exception> financialInfoToResponse(
        final PriceClaimsResponse priceClaimsResponse) {
        return new FunctionThrowsException<FinancialImpactInformation, FinancialImpactInformation, Exception>() {
            @Override
            public FinancialImpactInformation apply(
                final FinancialImpactInformation financialImpactInformation)
                throws Exception {
                priceClaimsResponse.setFinancialImpactInformation(financialImpactInformation);
                return financialImpactInformation;
            }
        };
    }

    private FunctionThrowsException<PriceClaimSummaryInformation, PriceClaimSummaryInformation, WipsDb2DatabaseException> toIntermediateResponse() {
        return new FunctionThrowsException<PriceClaimSummaryInformation, PriceClaimSummaryInformation, WipsDb2DatabaseException>() {

            @Override
            public PriceClaimSummaryInformation apply(final PriceClaimSummaryInformation summary)
                throws WipsDb2DatabaseException {
                summary.setCausalFactor(summary.getCausalFactor() + " - " + getCausalFactorDescription(summary));
                return summary;
            }

        };
    }

    private String getCausalFactorDescription(final PriceClaimSummaryInformation summary)
        throws WipsDb2DatabaseException {
        return pcCausalDescDAO()
            .retrieveCausalDesription(summary.getCausalFactor());
    }

    protected PcCausalDescriptionDAO pcCausalDescDAO() {
        return this.pcCausalDescriptionDAO;
    }

    private FunctionThrowsException<M00mTransactionOutput, PriceClaimsResponse, Exception> remarksToApprovers(
        final PriceClaimsResponse priceClaimsResponse, final Map<String, ApprovalDetail> remarksMap) {
        return new FunctionThrowsException<M00mTransactionOutput, PriceClaimsResponse, Exception>() {

            @Override
            public PriceClaimsResponse apply(final M00mTransactionOutput summary) throws Exception {
                final List<ApprovalDetail> remarks = new ArrayList<ApprovalDetail>();
                for (final String key : remarksMap.keySet()) {
                    remarks.add(remarksMap.get(key));
                }
                priceClaimsResponse.setPriceClaimRemarks(remarks);
                return priceClaimsResponse;
            }
        };
    }

    private Predicate<A02lTransactionOutput> moreApproversExist() {
        return new Predicate<A02lTransactionOutput>() {
            @Override
            public boolean test(final A02lTransactionOutput a02lTransactionOutput) {
                return a02lTransactionOutput.getHasMorePages().contains(WipsConstant.MORE);
            }
        };
    }

    private Predicate<PriceClaimRemarks> moreRemarksExist() {
        return new Predicate<PriceClaimRemarks>() {
            @Override
            public boolean test(final PriceClaimRemarks pcsRemarks) {
                return pcsRemarks.getApprovalDetails().getMoreRemarksPage().contains(WipsConstant.MORE);
            }
        };
    }

    private FunctionThrowsException<Cr3vTransactionOutput, Cr3vTransactionOutput, Exception> documentRemarksToResponse(
        final PriceClaimsResponse priceClaimsResponse) {
        return new FunctionThrowsException<Cr3vTransactionOutput, Cr3vTransactionOutput, Exception>() {

            @Override
            public Cr3vTransactionOutput apply(final Cr3vTransactionOutput summary) throws Exception {
                priceClaimsResponse.setDocumentRemarks(summary.getStrRemarks());
                return summary;
            }
        };
    }

    public void setPriceClaimsRequest(final PriceClaimsRequest priceClaimsRequest) {
        this.priceClaimsRequest = priceClaimsRequest;
    }

}
