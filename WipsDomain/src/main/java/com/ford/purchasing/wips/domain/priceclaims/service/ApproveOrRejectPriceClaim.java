//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.priceclaims.service;

import java.util.ArrayList;
import java.util.List;

import com.ford.purchasing.wips.common.atp.ApproveWarningHandler;
import com.ford.purchasing.wips.common.atp.EppsTransactionInput;
import com.ford.purchasing.wips.common.atp.S01i0TransactionInput;
import com.ford.purchasing.wips.common.atp.S01i0TransactionOutput;
import com.ford.purchasing.wips.common.connector.FunctionThrowsException;
import com.ford.purchasing.wips.common.connector.Predicate;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.priceclaims.PriceClaimsApproveOrRejectResponse;
import com.ford.purchasing.wips.common.priceclaims.PriceClaimsRequest;
import com.ford.purchasing.wips.domain.atp.beans.M00mTransactionInput;
import com.ford.purchasing.wips.domain.atp.beans.M00mTransactionOutput;
import com.ford.purchasing.wips.domain.connector.Function;
import com.ford.purchasing.wips.domain.connector.ImsConversationCtx;
import com.ford.purchasing.wips.domain.connector.ImsTransaction;
import com.ford.purchasing.wips.domain.layer.WipsTransactionConstant;
import com.ford.purchasing.wips.domain.pcs.beans.C36uTransactionInput;
import com.ford.purchasing.wips.domain.pcs.beans.Cr1xTransactionInput;
import com.ford.purchasing.wips.domain.pcs.beans.Cr1xTransactionOutput;
import com.ford.purchasing.wips.domain.pcs.beans.PriceClaimRemarks;
import com.ford.purchasing.wips.domain.pcs.beans.PriceClaimSummaryInformation;
import com.ford.purchasing.wips.domain.pcs.beans.Q43lTransactionInput;
import com.ford.purchasing.wips.domain.pcs.beans.Q43lTransactionOutput;
import com.ford.purchasing.wips.domain.priceclaims.beans.PriceClaimsResponse;

public class ApproveOrRejectPriceClaim
    implements Function<ImsConversationCtx, ImsTransaction<PriceClaimsApproveOrRejectResponse>> {

    private PriceClaimsRequest priceClaimsRequest;

    public ApproveOrRejectPriceClaim(final PriceClaimsRequest priceClaimsRequest) {
        this.priceClaimsRequest = priceClaimsRequest;
    }

    @Override
    public ImsTransaction<PriceClaimsApproveOrRejectResponse> apply(final ImsConversationCtx ctx) {
        final PriceClaimsResponse priceClaimsResponse = new PriceClaimsResponse();
        final PriceClaimsApproveOrRejectResponse approveResponse = new PriceClaimsApproveOrRejectResponse();
        final short pfKey = toPfKey(this.priceClaimsRequest.getActionTaken());
        return ctx
            .transact(WipsTransactionConstant.AAIMEPPS_TRANSACTION_NAME, this.priceClaimsRequest,
                new EppsTransactionInput().loadEppsInput(), new M00mTransactionOutput().getEppsOutput())
            .thenTransactIf(new M00mTransactionOutput().notUserJobCode(this.priceClaimsRequest),
                WipsTransactionConstant.AAIMM00M_TRANSACTION_NAME,
                new M00mTransactionInput().loadM00mSwitchJobInput(this.priceClaimsRequest),
                new M00mTransactionOutput().getM00mSwitchJobOutput())
            .thenTransact(WipsTransactionConstant.AAIMM00M_Q43L_TRANSACTION_NAME,
                new M00mTransactionInput().loadM00mInput(this.priceClaimsRequest),
                new Q43lTransactionOutput().getQ43lOutput())
            .thenTransact(WipsTransactionConstant.AAIMQ43L_C36U_TRANSACTION_NAME,
                new Q43lTransactionInput().loadQ43lInput(this.priceClaimsRequest, WipsConstant.PFKEY15),
                new PriceClaimSummaryInformation().get(this.priceClaimsRequest, priceClaimsResponse))
            .thenNestTransactionsIf(remarksIsModified(this.priceClaimsRequest),
                new Function<ImsTransaction<PriceClaimSummaryInformation>, ImsTransaction<PriceClaimSummaryInformation>>() {
                    @Override
                    public ImsTransaction<PriceClaimSummaryInformation> apply(
                        final ImsTransaction<PriceClaimSummaryInformation> txn) {
                        return txn
                            .thenTransact(WipsTransactionConstant.AAIMC36U_CR1X_TRANSACTION_NAME,
                                C36uTransactionInput.loadC36uInput(WipsConstant.PFKEY9),
                                new Cr1xTransactionOutput().getCr1xSaveOutput())
                            .thenTransactWhile(
                                saveRemarksPredicate(ApproveOrRejectPriceClaim.this.priceClaimsRequest),
                                WipsTransactionConstant.AAIMCR1X_TRANSACTION_NAME,
                                Cr1xTransactionInput.loadCr1xInput(WipsConstant.PFKEY8,
                                    ApproveOrRejectPriceClaim.this.priceClaimsRequest),
                                new Cr1xTransactionOutput().getCr1xSaveOutput())
                            .thenTransact(WipsTransactionConstant.AAIMCR1X_C36U_TRANSACTION_NAME,
                                new Cr1xTransactionInput().loadCr1xInputRemarks(
                                    ApproveOrRejectPriceClaim.this.priceClaimsRequest,
                                    WipsConstant.PFKEY3),
                                new PriceClaimSummaryInformation().get(
                                    ApproveOrRejectPriceClaim.this.priceClaimsRequest,
                                    priceClaimsResponse));
                    }
                })
            .thenTransact(WipsTransactionConstant.AAIMC36U_S01I0_TRANSACTION_NAME,
                C36uTransactionInput.loadC36uInput(pfKey),
                new S01i0TransactionOutput().getS01i0Output())
            .map(toResponseInCaseOfError(this.priceClaimsRequest, approveResponse))
            .thenTransactIf(actionTakenIsConfirm(this.priceClaimsRequest),
                WipsTransactionConstant.AAIMC36U_S01I0_TRANSACTION_NAME,
                new C36uTransactionInput().loadC36uS01i0Input(pfKey),
                new S01i0TransactionOutput().getS01i0Output())
            .thenNestTransactionsIf(thereIsNoError(this.priceClaimsRequest),
                new Function<ImsTransaction<S01i0TransactionOutput>, ImsTransaction<S01i0TransactionOutput>>() {
                    @Override
                    public ImsTransaction<S01i0TransactionOutput> apply(
                        final ImsTransaction<S01i0TransactionOutput> txn) {
                        return txn
                            .thenTransact(WipsTransactionConstant.AAIMS01I0_S01I1_TRANSACTION_NAME,
                                new S01i0TransactionInput().loadS01i0Input(WipsConstant.PFKEY13),
                                new PriceClaimSummaryInformation().get(
                                    ApproveOrRejectPriceClaim.this.priceClaimsRequest,
                                    priceClaimsResponse))
                            .map(approveMessageToResponse(approveResponse));
                    }
                })
            .map(finalResponse(approveResponse));
    }

    private short toPfKey(final String actionTaken) {
        return (actionTaken.equals(WipsConstant.APPROVE) || actionTaken.equals(WipsConstant.CONFIRM))
            ? WipsConstant.PFKEY13 : WipsConstant.PFKEY17;
    }

    private Predicate<PriceClaimSummaryInformation> remarksIsModified(final PriceClaimsRequest priceClaimsRequest) {
        return new Predicate<PriceClaimSummaryInformation>() {
            @Override
            public boolean test(final PriceClaimSummaryInformation pcsRemarks) {
                return priceClaimsRequest.isRemarksEditFlag();
            }
        };
    }

    private FunctionThrowsException<S01i0TransactionOutput, S01i0TransactionOutput, Exception> toResponseInCaseOfError(
        final PriceClaimsRequest priceClaimsRequest, final PriceClaimsApproveOrRejectResponse approveResponse) {
        return new FunctionThrowsException<S01i0TransactionOutput, S01i0TransactionOutput, Exception>() {

            @Override
            public S01i0TransactionOutput apply(final S01i0TransactionOutput summary) throws Exception {
                if (!priceClaimsRequest.getActionTaken().equals(WipsConstant.CONFIRM) && summary.isErrorFlag()) {
                    final List<ApproveWarningHandler> warningMessages = new ArrayList<ApproveWarningHandler>();
                    final ApproveWarningHandler handler = new ApproveWarningHandler();
                    handler.setErrorMessage(summary.getErrorMessage());
                    handler.setMessageCode(summary.getErrorMessage().trim().substring(0, 8));
                    handler.setActionTaken(priceClaimsRequest.getActionTaken());
                    warningMessages.add(handler);
                    approveResponse.setApprovalWarningFlag(true);
                    approveResponse.setWarningMessagesList(warningMessages);
                    approveResponse.setErrorFlag(true);
                }
                return summary;

            }
        };
    }

    private Predicate<S01i0TransactionOutput> thereIsNoError(final PriceClaimsRequest priceClaimsRequest) {
        return new Predicate<S01i0TransactionOutput>() {

            @Override
            public boolean test(final S01i0TransactionOutput output) {
                return (WipsConstant.REJECT).equals(priceClaimsRequest.getActionTaken()) || (WipsConstant.CONFIRM).equals(priceClaimsRequest.getActionTaken()) || ((WipsConstant.APPROVE).equals(priceClaimsRequest.getActionTaken()) && !output.isErrorFlag());
            }
        };
    }

    private FunctionThrowsException<PriceClaimSummaryInformation, S01i0TransactionOutput, Exception> approveMessageToResponse(
        final PriceClaimsApproveOrRejectResponse approveResponse) {
        return new FunctionThrowsException<PriceClaimSummaryInformation, S01i0TransactionOutput, Exception>() {

            @Override
            public S01i0TransactionOutput apply(final PriceClaimSummaryInformation summary) throws Exception {
                approveResponse.setApproveOrRejectMessage(summary.getErrorMessage());
                return new S01i0TransactionOutput();
            }
        };
    }

    private FunctionThrowsException<S01i0TransactionOutput, PriceClaimsApproveOrRejectResponse, Exception> finalResponse(
        final PriceClaimsApproveOrRejectResponse approveResponse) {
        return new FunctionThrowsException<S01i0TransactionOutput, PriceClaimsApproveOrRejectResponse, Exception>() {

            @Override
            public PriceClaimsApproveOrRejectResponse apply(final S01i0TransactionOutput summary) throws Exception {
                return approveResponse;
            }
        };
    }

    private Predicate<S01i0TransactionOutput> actionTakenIsConfirm(final PriceClaimsRequest priceClaimsRequest) {
        return new Predicate<S01i0TransactionOutput>() {

            @Override
            public boolean test(final S01i0TransactionOutput t) {
                return priceClaimsRequest.getActionTaken().equals(WipsConstant.CONFIRM);
            }

        };
    }

    private Predicate<PriceClaimRemarks> saveRemarksPredicate(final PriceClaimsRequest priceClaimsRequest) {
        return new Predicate<PriceClaimRemarks>() {
            @Override
            public boolean test(final PriceClaimRemarks pcsRemarks) {
                return !priceClaimsRequest.getCurrentJobCode().equals(pcsRemarks.getApprovalDetails().getJobCode())
                    && pcsRemarks.getHasMorePages().contains(WipsConstant.MORE);
            }
        };
    }
}
