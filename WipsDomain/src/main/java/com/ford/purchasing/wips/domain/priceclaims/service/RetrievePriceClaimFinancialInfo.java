//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.priceclaims.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.ford.purchasing.wips.common.atp.EppsTransactionInput;
import com.ford.purchasing.wips.common.connector.FunctionThrowsException;
import com.ford.purchasing.wips.common.connector.Predicate;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.priceclaims.PCSBusinessUnit;
import com.ford.purchasing.wips.common.priceclaims.PriceClaimsRequest;
import com.ford.purchasing.wips.common.priceclaims.PurchasingManagerDetail;
import com.ford.purchasing.wips.domain.atp.beans.M00mTransactionInput;
import com.ford.purchasing.wips.domain.atp.beans.M00mTransactionOutput;
import com.ford.purchasing.wips.domain.connector.Function;
import com.ford.purchasing.wips.domain.connector.ImsConversationCtx;
import com.ford.purchasing.wips.domain.connector.ImsTransaction;
import com.ford.purchasing.wips.domain.layer.WipsTransactionConstant;
import com.ford.purchasing.wips.domain.pcs.beans.C36uTransactionInput;
import com.ford.purchasing.wips.domain.pcs.beans.C38vTransactionInput;
import com.ford.purchasing.wips.domain.pcs.beans.C38vTransactionOutput;
import com.ford.purchasing.wips.domain.pcs.beans.FinancialImpactInformation;
import com.ford.purchasing.wips.domain.pcs.beans.PriceClaimSummaryInformation;
import com.ford.purchasing.wips.domain.pcs.beans.Q43lTransactionInput;
import com.ford.purchasing.wips.domain.pcs.beans.Q43lTransactionOutput;
import com.ford.purchasing.wips.domain.priceclaims.beans.PriceClaimsResponse;

public class RetrievePriceClaimFinancialInfo implements Function<ImsConversationCtx, ImsTransaction<PriceClaimsResponse>> {

    private PriceClaimsRequest priceClaimsRequest;

    @Inject
    public RetrievePriceClaimFinancialInfo() {
    }

    public RetrievePriceClaimFinancialInfo(final PriceClaimsRequest priceClaimsRequest) {
        this.priceClaimsRequest = priceClaimsRequest;
    }

    @Override
    public ImsTransaction<PriceClaimsResponse> apply(final ImsConversationCtx ctx) {
        final List<String> purchaseManagers = new ArrayList<String>();
        final PriceClaimsResponse priceClaimsResponse = new PriceClaimsResponse();
        final List<PurchasingManagerDetail> purchasingManagerDetails = new ArrayList<PurchasingManagerDetail>();
        final List<PCSBusinessUnit> pcsBusinessUnits = new ArrayList<PCSBusinessUnit>();
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
            .thenTransact(WipsTransactionConstant.AAIMC36U_C38V_TRANSACTION_NAME,
                C36uTransactionInput.loadC36uInput(WipsConstant.PFKEY23),
                C38vTransactionOutput.getC38vOutput(WipsConstant.STAR_ALL, purchaseManagers, pcsBusinessUnits,
                    purchasingManagerDetails))
            .thenTransactWhile(C38vTransactionOutput.isMoreDataPredicate(),
                WipsTransactionConstant.AAIMC38V_TRANSACTION_NAME,
                C38vTransactionInput.loadC38vInput(WipsConstant.PFKEY8, null, null),
                C38vTransactionOutput.getC38vOutput(WipsConstant.STAR_ALL, purchaseManagers, pcsBusinessUnits,
                    purchasingManagerDetails))
            .thenNestTransactionsIf(anyPMSelected(this.priceClaimsRequest),
                new Function<ImsTransaction<FinancialImpactInformation>, ImsTransaction<FinancialImpactInformation>>() {
                    @Override
                    public ImsTransaction<FinancialImpactInformation> apply(
                        final ImsTransaction<FinancialImpactInformation> txn) {
                        return loadPmOrYearSelectedOutput(purchaseManagers, purchasingManagerDetails, pcsBusinessUnits, txn,
                            false);
                    }
                })
            .thenNestTransactionsIf(anyYearSelected(this.priceClaimsRequest),
                new Function<ImsTransaction<FinancialImpactInformation>, ImsTransaction<FinancialImpactInformation>>() {
                    @Override
                    public ImsTransaction<FinancialImpactInformation> apply(
                        final ImsTransaction<FinancialImpactInformation> txn) {
                        return loadPmOrYearSelectedOutput(purchaseManagers, purchasingManagerDetails, pcsBusinessUnits, txn,
                            true);
                    }
                })
            .thenTransactIf(anyPMSelected(this.priceClaimsRequest),
                WipsTransactionConstant.AAIMC38V_TRANSACTION_NAME,
                C38vTransactionInput.loadC38vInput(WipsConstant.PFKEY18, this.priceClaimsRequest.getSelectedPM(),
                    this.priceClaimsRequest.getSelectedYear()),
                C38vTransactionOutput.getC38vOutput(this.priceClaimsRequest.getSelectedPM(),
                    purchaseManagers, pcsBusinessUnits,
                    purchasingManagerDetails))
            .thenNestTransactionsIf(anyPMSelected(this.priceClaimsRequest),
                new Function<ImsTransaction<FinancialImpactInformation>, ImsTransaction<FinancialImpactInformation>>() {

                    @Override
                    public ImsTransaction<FinancialImpactInformation> apply(
                        final ImsTransaction<FinancialImpactInformation> txn) {
                        return txn.thenDoTransactWhile(isMoreBUPredicate(priceClaimsRequest.getSelectedPM()),
                            WipsTransactionConstant.AAIMC38V_TRANSACTION_NAME,
                            C38vTransactionInput.loadC38vInput(priceClaimsRequest.getSelectedPM(),
                                priceClaimsRequest.getSelectedYear()),
                            C38vTransactionOutput.getC38vOutput(priceClaimsRequest.getSelectedPM(),
                                purchaseManagers, pcsBusinessUnits,
                                purchasingManagerDetails));
                    }
                })
            .map(financialInfoToResponse(priceClaimsResponse));
    }

    private ImsTransaction<FinancialImpactInformation> loadPmOrYearSelectedOutput(
        final List<String> purchaseManagers, final List<PurchasingManagerDetail> purchasingManagerDetails,
        final List<PCSBusinessUnit> pcsBusinessUnits, final ImsTransaction<FinancialImpactInformation> txn,
        final boolean isYearSelected) {
        return txn
            .thenTransact(WipsTransactionConstant.AAIMC38V_TRANSACTION_NAME,
                C38vTransactionInput.loadC38vInput(WipsConstant.PFKEY0,
                    priceClaimsRequest.getSelectedPM(), isYearSelected ? priceClaimsRequest.getSelectedYear() : null),
                isYearSelected ? C38vTransactionOutput.getC38vOutputAfterClear(priceClaimsRequest.getSelectedPM(),
                    purchaseManagers, pcsBusinessUnits, purchasingManagerDetails)
                    : C38vTransactionOutput.getC38vOutput(priceClaimsRequest.getSelectedPM(),
                        purchaseManagers, pcsBusinessUnits, purchasingManagerDetails))
            .thenTransactWhile(C38vTransactionOutput.isMoreDataPredicate(),
                WipsTransactionConstant.AAIMC38V_TRANSACTION_NAME,
                C38vTransactionInput.loadC38vInput(WipsConstant.PFKEY8,
                    priceClaimsRequest.getSelectedPM(), isYearSelected ? priceClaimsRequest.getSelectedYear() : null),
                C38vTransactionOutput.getC38vOutput(priceClaimsRequest.getSelectedPM(),
                    purchaseManagers, pcsBusinessUnits, purchasingManagerDetails));
    }

    private FunctionThrowsException<FinancialImpactInformation, PriceClaimsResponse, Exception> financialInfoToResponse(
        final PriceClaimsResponse priceClaimsResponse) {
        return new FunctionThrowsException<FinancialImpactInformation, PriceClaimsResponse, Exception>() {
            @Override
            public PriceClaimsResponse apply(
                final FinancialImpactInformation financialImpactInformation)
                throws Exception {
                priceClaimsResponse.setFinancialImpactInformation(financialImpactInformation);
                return priceClaimsResponse;
            }
        };
    }

    public void setPriceClaimsRequest(final PriceClaimsRequest priceClaimsRequest) {
        this.priceClaimsRequest = priceClaimsRequest;
    }

    public Predicate<FinancialImpactInformation> anyPMSelected(
        final PriceClaimsRequest priceClaimsRequest) {
        return new Predicate<FinancialImpactInformation>() {
            @Override
            public boolean test(final FinancialImpactInformation financialImpactInformation) {
                return StringUtils.isNotEmpty(priceClaimsRequest.getSelectedPM())
                    && !checkEqualsStarAll(priceClaimsRequest.getSelectedPM());
            }
        };
    }

    public Predicate<FinancialImpactInformation> anyYearSelected(
        final PriceClaimsRequest priceClaimsRequest) {
        return new Predicate<FinancialImpactInformation>() {
            @Override
            public boolean test(final FinancialImpactInformation financialImpactInformation) {
                return StringUtils.isNotEmpty(priceClaimsRequest.getSelectedYear())
                    && !checkEqualsStarAll(priceClaimsRequest.getSelectedYear());
            }
        };
    }

    private Predicate<FinancialImpactInformation> isMoreBUPredicate(final String selectedPM) {
        return new Predicate<FinancialImpactInformation>() {
            @Override
            public boolean test(final FinancialImpactInformation financialImpactInformation) {
                if (StringUtils.isEmpty(selectedPM) || checkEqualsStarAll(selectedPM))
                    return false;
                return !financialImpactInformation.getSelectedBU().contains(WipsConstant.ALL.toUpperCase());
            }
        };
    }

    private boolean checkEqualsStarAll(final String value) {
        return WipsConstant.STAR_ALL.equalsIgnoreCase(value);
    }
}
