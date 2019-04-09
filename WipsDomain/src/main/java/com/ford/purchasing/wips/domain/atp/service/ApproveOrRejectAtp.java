//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.atp.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.ford.purchasing.wips.common.atp.ApproveWarningHandler;
import com.ford.purchasing.wips.common.atp.AtpApproveOrRejectResponse;
import com.ford.purchasing.wips.common.atp.AtpRequest;
import com.ford.purchasing.wips.common.atp.AtpSupplierDetail;
import com.ford.purchasing.wips.common.atp.EppsTransactionInput;
import com.ford.purchasing.wips.common.atp.G51xTransactionInput;
import com.ford.purchasing.wips.common.atp.G51xTransactionOutput;
import com.ford.purchasing.wips.common.atp.S01i0TransactionInput;
import com.ford.purchasing.wips.common.atp.S01i0TransactionOutput;
import com.ford.purchasing.wips.common.atp.S01i1TransactionOutput;
import com.ford.purchasing.wips.common.connector.FunctionThrowsException;
import com.ford.purchasing.wips.common.connector.Predicate;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.WipsPendingApprovalOutput;
import com.ford.purchasing.wips.domain.atp.beans.G53xTransactionInput;
import com.ford.purchasing.wips.domain.atp.beans.G53xTransactionOutput;
import com.ford.purchasing.wips.domain.atp.beans.M00mTransactionInput;
import com.ford.purchasing.wips.domain.atp.beans.M00mTransactionOutput;
import com.ford.purchasing.wips.domain.atp.beans.Q01xTransactionInput;
import com.ford.purchasing.wips.domain.atp.beans.Q01xTransactionOutput;
import com.ford.purchasing.wips.domain.connector.Function;
import com.ford.purchasing.wips.domain.connector.ImsConversationCtx;
import com.ford.purchasing.wips.domain.connector.ImsTransaction;
import com.ford.purchasing.wips.domain.layer.WipsTransactionConstant;

@SuppressWarnings("javadoc")
public class ApproveOrRejectAtp implements
    Function<ImsConversationCtx, ImsTransaction<AtpApproveOrRejectResponse>> {
    private AtpRequest atpApproveOrRejectRequest;

    @Inject
    public ApproveOrRejectAtp() {
    }

    public ApproveOrRejectAtp(final AtpRequest atpApproveOrRejectRequest) {
        this.atpApproveOrRejectRequest = atpApproveOrRejectRequest;
    }

    @Override
    public ImsTransaction<AtpApproveOrRejectResponse> apply(final ImsConversationCtx ctx) {
        final List<WipsPendingApprovalOutput> atpRecap =
            new ArrayList<WipsPendingApprovalOutput>();
        final G53xTransactionOutput g53xOutput = new G53xTransactionOutput();
        final short pfKey = toPfKey(this.atpApproveOrRejectRequest.getActionTaken());
        final List<AtpSupplierDetail> suppliersCollector = new ArrayList<AtpSupplierDetail>();
        final AtpApproveOrRejectResponse atpApproveOrRejectResponse = new AtpApproveOrRejectResponse();
        final List<ApproveWarningHandler> warningMessages = new ArrayList<ApproveWarningHandler>();
        return ctx
            .transact(WipsTransactionConstant.AAIMEPPS_TRANSACTION_NAME, this.atpApproveOrRejectRequest,
                new EppsTransactionInput().loadEppsInput(),
                new M00mTransactionOutput().getEppsOutput())
            .thenTransactIf(new M00mTransactionOutput().notUserJobCode(this.atpApproveOrRejectRequest),
                WipsTransactionConstant.AAIMM00M_TRANSACTION_NAME,
                new M00mTransactionInput()
                    .loadM00mSwitchJobInput(this.atpApproveOrRejectRequest),
                new M00mTransactionOutput().getM00mSwitchJobOutput())
            .thenTransact(WipsTransactionConstant.AAIMM00M_Q01X_TRANSACTION_NAME,
                new M00mTransactionInput().loadM00mInput(this.atpApproveOrRejectRequest),
                new Q01xTransactionOutput().getQ01xOutput(atpRecap))
            .thenTransact(WipsTransactionConstant.Q01X_TRANSACTION,
                new Q01xTransactionInput().loadQ01xInput(this.atpApproveOrRejectRequest, WipsConstant.PFKEY0,
                    WipsConstant.SELCAT),
                new Q01xTransactionOutput().getQ01xOutput(atpRecap))
            .thenNestTransactionsIfElse(nextScreenIsG53x(),
                new Function<ImsTransaction<Q01xTransactionOutput>, ImsTransaction<G53xTransactionOutput>>() {

                    @Override
                    public ImsTransaction<G53xTransactionOutput> apply(
                        final ImsTransaction<Q01xTransactionOutput> txn) {
                        return txn
                            .thenTransact(WipsTransactionConstant.Q01XG53X_TRANSACTION,
                                new Q01xTransactionInput().loadQ01xInput(
                                    ApproveOrRejectAtp.this.atpApproveOrRejectRequest,
                                    WipsConstant.PFKEY13, WipsConstant.SELCAT),
                                new G53xTransactionOutput().getG53xOutput(suppliersCollector, g53xOutput));
                    }
                },
                new Function<ImsTransaction<Q01xTransactionOutput>, ImsTransaction<G53xTransactionOutput>>() {

                    @Override
                    public ImsTransaction<G53xTransactionOutput> apply(
                        final ImsTransaction<Q01xTransactionOutput> txn) {
                        return txn
                            .thenTransact(WipsTransactionConstant.Q01XG51X_TRANSACTION,
                                new Q01xTransactionInput().loadQ01xInput(
                                    ApproveOrRejectAtp.this.atpApproveOrRejectRequest, WipsConstant.PFKEY13,
                                    WipsConstant.SELCAT),
                                new G51xTransactionOutput().getG51xOutput())
                            .thenTransact(WipsTransactionConstant.G51XG53XTRANSACTION,
                                new G51xTransactionInput().loadG51xInput(),
                                new G53xTransactionOutput().getG53xOutput(suppliersCollector, g53xOutput));
                    }
                })
            .thenTransact(WipsTransactionConstant.AAIM53X_S010_TRANSACTION_NAME,
                new G53xTransactionInput().loadG53xInputFromG53(pfKey),
                new S01i0TransactionOutput().getS01i0ApproveOrRejectOutput(atpApproveOrRejectResponse, warningMessages))
            .thenNestTransactionsIf(errorMessageExist(),
                new Function<ImsTransaction<S01i0TransactionOutput>, ImsTransaction<S01i0TransactionOutput>>() {
                    @Override
                    public ImsTransaction<S01i0TransactionOutput> apply(
                        final ImsTransaction<S01i0TransactionOutput> txn) {
                        return txn
                            .thenDoTransactWhile(isErrorMessagePredicate(),
                                WipsTransactionConstant.AAIM53X_S010_TRANSACTION_NAME,
                                new G53xTransactionInput().loadG53xInputApproveOrReject(g53xOutput.getG53xTransactionInput(),
                                    pfKey),
                                new S01i0TransactionOutput().getS01i0ApproveOrRejectOutput(atpApproveOrRejectResponse,
                                    warningMessages))
                            .map(actionTakenToResponse(ApproveOrRejectAtp.this.atpApproveOrRejectRequest,
                                atpApproveOrRejectResponse));
                    }
                })
            .thenNestTransactionsIfElse(noErrorMessageExist(atpApproveOrRejectResponse),
                new Function<ImsTransaction<S01i0TransactionOutput>, ImsTransaction<AtpApproveOrRejectResponse>>() {
                    @Override
                    public ImsTransaction<AtpApproveOrRejectResponse> apply(
                        final ImsTransaction<S01i0TransactionOutput> txn) {
                        return txn.thenTransact(WipsTransactionConstant.S01I0_TRANSACTION,
                            new S01i0TransactionInput().loadS01i0Input(WipsConstant.PFKEY13),
                            new S01i1TransactionOutput().getS01i1Output())
                            .map(approveMessageToResponse(atpApproveOrRejectResponse));
                    }
                }, new Function<ImsTransaction<S01i0TransactionOutput>, ImsTransaction<AtpApproveOrRejectResponse>>() {
                    @Override
                    public ImsTransaction<AtpApproveOrRejectResponse> apply(
                        final ImsTransaction<S01i0TransactionOutput> txn) {
                        return txn.map(toResponse(atpApproveOrRejectResponse));
                    }
                });
    }

    private short toPfKey(final String actionTaken) {
        return (actionTaken.equals(WipsConstant.APPROVE)
            || actionTaken.equals(WipsConstant.CONFIRM)) ? WipsConstant.PFKEY13
                : WipsConstant.PFKEY14;
    }

    private FunctionThrowsException<S01i0TransactionOutput, S01i0TransactionOutput, Exception> actionTakenToResponse(
        final AtpRequest atpRequest, final AtpApproveOrRejectResponse approveResponse) {
        return new FunctionThrowsException<S01i0TransactionOutput, S01i0TransactionOutput, Exception>() {

            @Override
            public S01i0TransactionOutput apply(
                final S01i0TransactionOutput s01i0TransactionOutput) throws Exception {

                final ApproveWarningHandler handler =
                    approveResponse.getWarningMessagesList()
                        .get(approveResponse.getWarningMessagesList().size() - 1);
                if (WipsConstant.CONFIRM.equalsIgnoreCase(handler.getActionTaken()))
                    (approveResponse.getWarningMessagesList()
                        .get(approveResponse.getWarningMessagesList().size() - 1)).setActionTaken(WipsConstant.APPROVE);
                if (!WipsConstant.CONFIRM.equalsIgnoreCase(atpRequest.getActionTaken())) {
                    approveResponse.setErrorFlag(true);
                    approveResponse.setErrorMessage(approveResponse.getWarningMessagesList().toString());
                }
                return s01i0TransactionOutput;
            }
        };
    }

    private FunctionThrowsException<S01i1TransactionOutput, AtpApproveOrRejectResponse, Exception> approveMessageToResponse(
        final AtpApproveOrRejectResponse approveOrRejectResponse) {
        return new FunctionThrowsException<S01i1TransactionOutput, AtpApproveOrRejectResponse, Exception>() {

            @Override
            public AtpApproveOrRejectResponse apply(final S01i1TransactionOutput summary)
                throws Exception {
                approveOrRejectResponse
                    .setApproveOrRejectMessage(summary.getConfirmMessage());
                return approveOrRejectResponse;
            }
        };
    }

    private FunctionThrowsException<S01i0TransactionOutput, AtpApproveOrRejectResponse, Exception> toResponse(
        final AtpApproveOrRejectResponse approveOrRejectResponse) {
        return new FunctionThrowsException<S01i0TransactionOutput, AtpApproveOrRejectResponse, Exception>() {

            @Override
            public AtpApproveOrRejectResponse apply(final S01i0TransactionOutput summary)
                throws Exception {
                return approveOrRejectResponse;
            }
        };
    }

    private Predicate<S01i0TransactionOutput> isErrorMessagePredicate() {
        return new Predicate<S01i0TransactionOutput>() {
            @Override
            public boolean test(final S01i0TransactionOutput output) {
                return output.getErrorMessage() != null
                    && (output.getErrorMessage().contains("MSG-4276")
                        || output.getErrorMessage().contains("MSG-4236")
                        || output.getErrorMessage().contains("MSG-4524"));
            }
        };
    }

    private Predicate<S01i0TransactionOutput> errorMessageExist() {
        return new Predicate<S01i0TransactionOutput>() {

            @Override
            public boolean test(final S01i0TransactionOutput output) {
                return output.getErrorMessage() != null;
            }
        };
    }

    private Predicate<S01i0TransactionOutput> noErrorMessageExist(
        final AtpApproveOrRejectResponse atpApproveOrRejectResponse) {
        return new Predicate<S01i0TransactionOutput>() {

            @Override
            public boolean test(final S01i0TransactionOutput output) {
                return !atpApproveOrRejectResponse.isErrorFlag();
            }
        };
    }

    private Predicate<Q01xTransactionOutput> nextScreenIsG53x() {
        return new Predicate<Q01xTransactionOutput>() {

            @Override
            public boolean test(final Q01xTransactionOutput output) {
                return output.getPendingApprItemsList()
                    .get(0)
                    .getSubsequentProgram()
                    .contains("53");
            }
        };
    }
}
