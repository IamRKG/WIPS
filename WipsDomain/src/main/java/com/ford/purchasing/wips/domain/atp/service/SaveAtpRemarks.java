//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.atp.service;

import java.util.ArrayList;
import java.util.List;

import com.ford.purchasing.wips.common.atp.AtpRemarksRequest;
import com.ford.purchasing.wips.common.atp.AtpSupplierDetail;
import com.ford.purchasing.wips.common.atp.EppsTransactionInput;
import com.ford.purchasing.wips.common.atp.G51xTransactionInput;
import com.ford.purchasing.wips.common.atp.G51xTransactionOutput;
import com.ford.purchasing.wips.common.connector.FunctionThrowsException;
import com.ford.purchasing.wips.common.connector.Predicate;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.util.StringUtil;
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

public class SaveAtpRemarks
        implements Function<ImsConversationCtx, ImsTransaction<AtpRemarksResponse>> {

    private AtpRemarksRequest atpRemarksRequest;

    public SaveAtpRemarks(final AtpRemarksRequest atpRemarksRequest) {
        this.atpRemarksRequest = atpRemarksRequest;
    }

    @Override
    public ImsTransaction<AtpRemarksResponse> apply(final ImsConversationCtx ctx) {
        final List<AtpSupplierDetail> suppliersCollector = new ArrayList<AtpSupplierDetail>();
        final G53xTransactionOutput g53xOutput = new G53xTransactionOutput();
        final String actionTaken = WipsConstant.SAVE_REMARKS;
        final List<String> userRemarksToSave =
                StringUtil.splitString(
                        StringUtil.buildUserRemarks(this.atpRemarksRequest.getUserRemarks()));
        final List<String> savedRemarksFromIms = new ArrayList<String>();
        return ctx.transact(WipsTransactionConstant.AAIMEPPS_TRANSACTION_NAME,
                this.atpRemarksRequest,
                new EppsTransactionInput().loadEppsInput(),
                new M00mTransactionOutput().getEppsOutput())
                .thenTransactIf(
                        new M00mTransactionOutput().notUserJobCode(this.atpRemarksRequest),
                        WipsTransactionConstant.AAIMM00M_TRANSACTION_NAME,
                        new M00mTransactionInput()
                                .loadM00mSwitchJobInput(this.atpRemarksRequest),
                        new M00mTransactionOutput().getM00mSwitchJobOutput())
                .thenTransact(WipsTransactionConstant.AAIMM00M_Q01X_TRANSACTION_NAME,
                        new M00mTransactionInput().loadM00mInput(this.atpRemarksRequest),
                        new Q01xTransactionOutput().getQ01xOutput(this.atpRemarksRequest))
                .thenTransact(WipsTransactionConstant.Q01X_TRANSACTION,
                        new Q01xTransactionInput().loadQ01xInput(this.atpRemarksRequest,
                                WipsConstant.PFKEY0, WipsConstant.SELCAT),
                        new Q01xTransactionOutput().getQ01xOutput(this.atpRemarksRequest))
                .thenNestTransactionsIfElse(nextScreenIsG53x(),
                        new Function<ImsTransaction<Q01xTransactionOutput>, ImsTransaction<G53xTransactionOutput>>() {

                            @Override
                            public ImsTransaction<G53xTransactionOutput> apply(
                                    final ImsTransaction<Q01xTransactionOutput> txn) {
                                return txn.thenTransact(
                                        WipsTransactionConstant.Q01XG53X_TRANSACTION,
                                        new Q01xTransactionInput().loadQ01xInput(
                                                atpRemarksRequest, WipsConstant.PFKEY13,
                                                WipsConstant.SELCAT),
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
                                        new Q01xTransactionInput().loadQ01xInput(
                                                atpRemarksRequest, WipsConstant.PFKEY13,
                                                WipsConstant.SELCAT),
                                        new G51xTransactionOutput().getG51xOutput())
                                        .thenTransact(
                                                WipsTransactionConstant.G51XG53XTRANSACTION,
                                                new G51xTransactionInput().loadG51xInput(),
                                                new G53xTransactionOutput().getG53xOutput(
                                                        suppliersCollector, g53xOutput));
                            }
                        })
                .thenTransact(WipsTransactionConstant.G53X_G56X_TRANSACTION,
                        new G53xTransactionInput().loadG53xInput(this.atpRemarksRequest,
                                actionTaken),
                        new AtpRemarksOutput().populateAtpRemarksDetailsForSave())
                .thenDoTransactWhile(
                        notUserJobCode(this.atpRemarksRequest.getCurrentJobCode()),
                        WipsTransactionConstant.G56X_TRANSACTION,
                        new G56xTransactionInput()
                                .loadG56xInputForNextUserForSave(this.atpRemarksRequest),
                        new AtpRemarksOutput().populateUserRemarksForSave())
                .thenDoTransactWhile(moreUserRemarks(),
                        WipsTransactionConstant.G56X_TRANSACTION,
                        new G56xTransactionInput()
                                .loadG56xInputForClearUserRemarks(this.atpRemarksRequest),
                        new AtpRemarksOutput().populateUserRemarksForSave())
                .thenDoNestTransactWhile(moreLines(userRemarksToSave),
                        new Function<ImsTransaction<AtpRemarksOutput>, ImsTransaction<AtpRemarksOutput>>() {

                            @Override
                    public ImsTransaction<AtpRemarksOutput> apply(final ImsTransaction<AtpRemarksOutput> txn) {
                        return txn.thenTransact(WipsTransactionConstant.G56X_TRANSACTION,
                                    new G56xTransactionInput().loadG56xInputForSaveUserRemarks(atpRemarksRequest, userRemarksToSave,
                                        savedRemarksFromIms.size()),
                                    new AtpRemarksOutput().populateSavedRemarks(savedRemarksFromIms))
                            .thenTransactIf(moreUserRemarks(),
                                                WipsTransactionConstant.G56X_TRANSACTION,
                                                new G56xTransactionInput()
                                                        .loadG56xInputForNextPageUserRemarks(
                                                                atpRemarksRequest),
                                                new AtpRemarksOutput().populateSavedRemarks(
                                                        savedRemarksFromIms));
                            }
                        })
                .map(toResponse(savedRemarksFromIms, this.atpRemarksRequest));
    }

    private FunctionThrowsException<AtpRemarksOutput, AtpRemarksResponse, Exception> toResponse(
            final List<String> savedRemarksFromIms,
            final AtpRemarksRequest atpRemarksRequest) {
        return new FunctionThrowsException<AtpRemarksOutput, AtpRemarksResponse, Exception>() {

            @Override
            public AtpRemarksResponse apply(final AtpRemarksOutput output) throws Exception {
                final AtpRemarksResponse response = new AtpRemarksResponse();
                output.setG56xInput(null);
                output.setAtpNumber(atpRemarksRequest.getAtpNumber());
                output.getRemark().get(0).setRemarks(savedRemarksFromIms);
                response.setRemarks(output);
                return response;
            }
        };
    }

    private Predicate<AtpRemarksOutput> moreLines(final List<String> userRemarksToSave) {
        return new Predicate<AtpRemarksOutput>() {

            @Override
            public boolean test(final AtpRemarksOutput output) {
                return output.linesProcessed() < userRemarksToSave.size()
                       || output.getMoreRemarks().contains(WipsTransactionConstant.MORE);
            }
        };
    }

    private Predicate<AtpRemarksOutput> moreUserRemarks() {
        return new Predicate<AtpRemarksOutput>() {

            @Override
            public boolean test(final AtpRemarksOutput output) {
                return output.getMoreRemarks().contains(WipsTransactionConstant.MORE);
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

    private Predicate<AtpRemarksOutput> notUserJobCode(final String jobCode) {
        return new Predicate<AtpRemarksOutput>() {
            @Override
            public boolean test(final AtpRemarksOutput atpRemarks) {
                return atpRemarks.getRemark() != null
                       && !atpRemarks.getRemark().get(0).getUserJobCode().contains(jobCode);
            }
        };
    }
}
