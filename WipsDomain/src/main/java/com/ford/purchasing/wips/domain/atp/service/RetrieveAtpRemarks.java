//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.atp.service;

import java.util.ArrayList;
import java.util.List;

import com.ford.purchasing.wips.common.atp.AtpRemark;
import com.ford.purchasing.wips.common.atp.AtpRequest;
import com.ford.purchasing.wips.common.atp.AtpSupplierDetail;
import com.ford.purchasing.wips.common.atp.EppsTransactionInput;
import com.ford.purchasing.wips.common.atp.G51xTransactionInput;
import com.ford.purchasing.wips.common.atp.G51xTransactionOutput;
import com.ford.purchasing.wips.common.connector.FunctionThrowsException;
import com.ford.purchasing.wips.common.connector.Predicate;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.domain.atp.AtpRemarksResponse;
import com.ford.purchasing.wips.domain.atp.beans.AtpRemarksOutput;
import com.ford.purchasing.wips.domain.atp.beans.G53xTransactionInput;
import com.ford.purchasing.wips.domain.atp.beans.G53xTransactionOutput;
import com.ford.purchasing.wips.domain.atp.beans.G56xTransactionInput;
import com.ford.purchasing.wips.domain.atp.beans.M00mTransactionInput;
import com.ford.purchasing.wips.domain.atp.beans.M00mTransactionOutput;
import com.ford.purchasing.wips.domain.atp.beans.Q01xTransactionInput;
import com.ford.purchasing.wips.domain.atp.beans.Q01xTransactionOutput;
import com.ford.purchasing.wips.domain.connector.Function;
import com.ford.purchasing.wips.domain.connector.ImsConversationCtx;
import com.ford.purchasing.wips.domain.connector.ImsTransaction;
import com.ford.purchasing.wips.domain.layer.WipsTransactionConstant;

public class RetrieveAtpRemarks
        implements Function<ImsConversationCtx, ImsTransaction<AtpRemarksResponse>> {

    private final AtpRequest atpRequest;

    public RetrieveAtpRemarks(final AtpRequest atpRequest) {
        this.atpRequest = atpRequest;
    }

    @Override
    public ImsTransaction<AtpRemarksResponse> apply(final ImsConversationCtx ctx) {
        final List<AtpSupplierDetail> suppliersCollector = new ArrayList<AtpSupplierDetail>();
        final G53xTransactionOutput g53xOutput = new G53xTransactionOutput();
        final String actionTaken = WipsConstant.RETRIEVE_REMARKS;
        final AtpRemarksOutput atpRemarksCollector = new AtpRemarksOutput();
        final List<AtpRemark> systemRemarks = new ArrayList<AtpRemark>();
        final List<AtpRemark> userRemarksCollector = new ArrayList<AtpRemark>();
        return ctx.transact(WipsTransactionConstant.AAIMEPPS_TRANSACTION_NAME,
                this.atpRequest,
                new EppsTransactionInput().loadEppsInput(),
                new M00mTransactionOutput().getEppsOutput())
                .thenTransactIf(new M00mTransactionOutput().notUserJobCode(this.atpRequest),
                        WipsTransactionConstant.AAIMM00M_TRANSACTION_NAME,
                        new M00mTransactionInput().loadM00mSwitchJobInput(this.atpRequest),
                        new M00mTransactionOutput().getM00mSwitchJobOutput())
                .thenTransact(WipsTransactionConstant.AAIMM00M_Q01X_TRANSACTION_NAME,
                        new M00mTransactionInput().loadM00mInput(this.atpRequest),
                        new Q01xTransactionOutput().getQ01xOutput(this.atpRequest))
                .thenTransact(WipsTransactionConstant.Q01X_TRANSACTION,
                        new Q01xTransactionInput().loadQ01xInput(this.atpRequest,
                                WipsConstant.PFKEY0, WipsConstant.SELCAT),
                        new Q01xTransactionOutput().getQ01xOutput(this.atpRequest))
                .thenNestTransactionsIfElse(nextScreenIsG53x(),
                        new Function<ImsTransaction<Q01xTransactionOutput>, ImsTransaction<G53xTransactionOutput>>() {

                            @Override
                            public ImsTransaction<G53xTransactionOutput> apply(
                                    final ImsTransaction<Q01xTransactionOutput> txn) {
                                return txn.thenTransact(
                                        WipsTransactionConstant.Q01XG53X_TRANSACTION,
                                        new Q01xTransactionInput().loadQ01xInput(atpRequest,
                                                WipsConstant.PFKEY13, WipsConstant.SELCAT),
                                        new G53xTransactionOutput().getG53xOutput(
                                                suppliersCollector, g53xOutput));
                            }

                        },
                        new Function<ImsTransaction<Q01xTransactionOutput>, ImsTransaction<G53xTransactionOutput>>() {

                            @Override
                            public ImsTransaction<G53xTransactionOutput> apply(
                                    final ImsTransaction<Q01xTransactionOutput> txn) {
                                return txn.thenTransact(
                                        WipsTransactionConstant.Q01XG51X_TRANSACTION,
                                        new Q01xTransactionInput().loadQ01xInput(atpRequest,
                                                WipsConstant.PFKEY13, WipsConstant.SELCAT),
                                        new G51xTransactionOutput().getG51xOutput())
                                        .thenTransact(
                                                WipsTransactionConstant.G51XG53XTRANSACTION,
                                                new G51xTransactionInput().loadG51xInput(),
                                                new G53xTransactionOutput().getG53xOutput(
                                                        suppliersCollector, g53xOutput));
                            }
                        })
                .thenTransact(WipsTransactionConstant.G53X_G56X_TRANSACTION,
                        new G53xTransactionInput().loadG53xInput(this.atpRequest,
                                actionTaken),
                        atpRemarksCollector.populateAtpRemarksDetails(systemRemarks))
                .thenTransactWhile(moreSystemRemarks(),
                        WipsTransactionConstant.G56X_TRANSACTION,
                        new G56xTransactionInput()
                                .loadG56xInputSystemRemarks(this.atpRequest),
                        atpRemarksCollector.populateMoreSystemRemarks(systemRemarks))
                .thenDoNestTransactWhile(notSystemCode(),
                        new Function<ImsTransaction<AtpRemarksOutput>, ImsTransaction<AtpRemarksOutput>>() {
                            @Override
                            public ImsTransaction<AtpRemarksOutput> apply(
                                    final ImsTransaction<AtpRemarksOutput> txn) {
                                final List<AtpRemark> userRemarks =
                                        new ArrayList<AtpRemark>();
                                final ImsTransaction<AtpRemarksOutput> imsTransaction =
                                        txn.thenDoTransactWhile(hasMoreRemarks(),
                                                WipsTransactionConstant.G56X_TRANSACTION,
                                                new G56xTransactionInput()
                                                        .loadG56xInputForNextUser(atpRequest),
                                                atpRemarksCollector
                                                        .populateUserRemarks(userRemarks))
                                                .map(moreUserRemarks(userRemarksCollector,
                                                        userRemarks));
                                return imsTransaction;
                            }

                        })
                .map(toResponse(atpRemarksCollector, systemRemarks, userRemarksCollector));
    }

    private FunctionThrowsException<AtpRemarksOutput, AtpRemarksOutput, Exception> moreUserRemarks(
            final List<AtpRemark> userRemarksCollector, final List<AtpRemark> userRemarks) {
        return new FunctionThrowsException<AtpRemarksOutput, AtpRemarksOutput, Exception>() {

            @Override
            public AtpRemarksOutput apply(final AtpRemarksOutput output) throws Exception {
                userRemarksCollector.addAll(userRemarks);
                return output;
            }
        };
    }

    private Predicate<AtpRemarksOutput> hasMoreRemarks() {
        return new Predicate<AtpRemarksOutput>() {

            @Override
            public boolean test(final AtpRemarksOutput output) {
                return output.getMoreRemarks().contains(WipsTransactionConstant.MORE);
            }
        };
    }

    private Predicate<AtpRemarksOutput> notSystemCode() {
        return new Predicate<AtpRemarksOutput>() {
            @Override
            public boolean test(final AtpRemarksOutput atpRemarks) {
                return atpRemarks.getRemark() != null
                       && !atpRemarks.getRemark().get(0).getUserJobCode().contains("***");
            }
        };
    }

    private Predicate<AtpRemarksOutput> moreSystemRemarks() {
        return new Predicate<AtpRemarksOutput>() {

            @Override
            public boolean test(final AtpRemarksOutput output) {
                return output.getMoreRemarks().contains(WipsTransactionConstant.MORE);
            }
        };
    }

    private FunctionThrowsException<AtpRemarksOutput, AtpRemarksResponse, Exception> toResponse(
            final AtpRemarksOutput atpRemarksCollector, final List<AtpRemark> systemRemarks,
            final List<AtpRemark> userRemarksCollector) {
        return new FunctionThrowsException<AtpRemarksOutput, AtpRemarksResponse, Exception>() {

            @Override
            public AtpRemarksResponse apply(final AtpRemarksOutput lastPageRemarks)
                    throws Exception {
                final AtpRemarksResponse atpRemarksResponse = new AtpRemarksResponse();
                atpRemarksCollector.setRemark(systemRemarks);
                atpRemarksCollector.getRemark().addAll(userRemarksCollector);
                atpRemarksCollector.setG56xInput(null);
                atpRemarksCollector.setAtpNumber(atpRequest.getAtpNumber());
                atpRemarksResponse.setRemarks(atpRemarksCollector);
                return atpRemarksResponse;
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
