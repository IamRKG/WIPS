//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.atp.service;

import java.util.ArrayList;
import java.util.List;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.atp.ApproveWarningHandler;
import com.ford.purchasing.wips.common.atp.AtpApproveOrRejectResponse;
import com.ford.purchasing.wips.common.atp.AtpRemarksRequest;
import com.ford.purchasing.wips.common.atp.AtpSupplierDetail;
import com.ford.purchasing.wips.common.atp.EppsTransactionInput;
import com.ford.purchasing.wips.common.atp.G51xTransactionInput;
import com.ford.purchasing.wips.common.atp.G51xTransactionOutput;
import com.ford.purchasing.wips.common.atp.S01i1TransactionOutput;
import com.ford.purchasing.wips.common.connector.BiConsumerThrowsException;
import com.ford.purchasing.wips.common.connector.BiFunctionThrowsException;
import com.ford.purchasing.wips.common.connector.FunctionThrowsException;
import com.ford.purchasing.wips.common.connector.Predicate;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.WipsImsInput;
import com.ford.purchasing.wips.common.layer.exception.WipsImsTransactionException;
import com.ford.purchasing.wips.common.layer.util.StringUtil;
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

public class SaveRemarksAndApproveOrReject
        implements Function<ImsConversationCtx, ImsTransaction<AtpApproveOrRejectResponse>> {

    private AtpRemarksRequest atpRemarksRequest;
    private static final String CONFIRM = "Confirm";

    public SaveRemarksAndApproveOrReject(final AtpRemarksRequest atpRemarksRequest) {
        this.atpRemarksRequest = atpRemarksRequest;
    }

    @Override
    public ImsTransaction<AtpApproveOrRejectResponse> apply(final ImsConversationCtx ctx) {
        final List<AtpSupplierDetail> suppliersCollector = new ArrayList<AtpSupplierDetail>();
        final G53xTransactionOutput g53xOutput = new G53xTransactionOutput();
        final String actionTaken = WipsConstant.SAVE_REMARKS;
        final List<String> userRemarksToSave =
                StringUtil.splitString(
                        StringUtil.buildUserRemarks(this.atpRemarksRequest.getUserRemarks()));
        final List<String> savedRemarksFromIms = new ArrayList<String>();
        final List<ApproveWarningHandler> warningMessages = new ArrayList<ApproveWarningHandler>();
        final AtpRemarksOutput g56xInputHolder = new AtpRemarksOutput();
        return ctx.transact(WipsTransactionConstant.AAIMEPPS_TRANSACTION_NAME,
            this.atpRemarksRequest,
            new EppsTransactionInput().loadEppsInput(),
            new M00mTransactionOutput().getEppsOutput())
            .thenTransactIf(new M00mTransactionOutput().notUserJobCode(this.atpRemarksRequest),
                WipsTransactionConstant.AAIMM00M_TRANSACTION_NAME,
                new M00mTransactionInput().loadM00mSwitchJobInput(this.atpRemarksRequest),
                new M00mTransactionOutput().getM00mSwitchJobOutput())
            .thenTransact(WipsTransactionConstant.AAIMM00M_Q01X_TRANSACTION_NAME,
                new M00mTransactionInput().loadM00mInput(this.atpRemarksRequest),
                new Q01xTransactionOutput().getQ01xOutput(this.atpRemarksRequest))
            .thenTransact(WipsTransactionConstant.Q01X_TRANSACTION,
                new Q01xTransactionInput().loadQ01xInput(this.atpRemarksRequest, WipsConstant.PFKEY0, WipsConstant.SELCAT),
                new Q01xTransactionOutput().getQ01xOutput(this.atpRemarksRequest))
            .thenNestTransactionsIfElse(nextScreenIsG53x(),
                new Function<ImsTransaction<Q01xTransactionOutput>, ImsTransaction<G53xTransactionOutput>>() {

                    @Override
                    public ImsTransaction<G53xTransactionOutput> apply(
                        final ImsTransaction<Q01xTransactionOutput> txn) {
                        return txn.thenTransact(WipsTransactionConstant.Q01XG53X_TRANSACTION,
                            new Q01xTransactionInput().loadQ01xInput(atpRemarksRequest, WipsConstant.PFKEY13,
                                WipsConstant.SELCAT),
                            new G53xTransactionOutput().getG53xOutput(suppliersCollector, g53xOutput));
                    }

                },
                new Function<ImsTransaction<Q01xTransactionOutput>, ImsTransaction<G53xTransactionOutput>>() {

                    @Override
                    public ImsTransaction<G53xTransactionOutput> apply(
                        final ImsTransaction<Q01xTransactionOutput> txn) {
                        return txn.thenTransact(WipsTransactionConstant.Q01XG51X_TRANSACTION,
                            new Q01xTransactionInput().loadQ01xInput(atpRemarksRequest, WipsConstant.PFKEY13,
                                WipsConstant.SELCAT),
                            new G51xTransactionOutput().getG51xOutput())
                            .thenTransact(WipsTransactionConstant.G51XG53XTRANSACTION,
                                new G51xTransactionInput().loadG51xInput(),
                                new G53xTransactionOutput().getG53xOutput(suppliersCollector, g53xOutput));
                    }
                })
            .thenTransact(WipsTransactionConstant.G53X_G56X_TRANSACTION,
                new G53xTransactionInput().loadG53xInput(this.atpRemarksRequest, actionTaken),
                new AtpRemarksOutput().populateAtpRemarksDetailsForSave())
            .thenDoTransactWhile(notUserJobCode(this.atpRemarksRequest.getCurrentJobCode()),
                WipsTransactionConstant.G56X_TRANSACTION,
                new G56xTransactionInput().loadG56xInputForNextUserForSave(this.atpRemarksRequest),
                new AtpRemarksOutput().populateUserRemarksForSave())
            .thenDoTransactWhile(moreUserRemarks(),
                WipsTransactionConstant.G56X_TRANSACTION,
                new G56xTransactionInput().loadG56xInputForClearUserRemarks(this.atpRemarksRequest),
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
                                new G56xTransactionInput().loadG56xInputForNextPageUserRemarks(atpRemarksRequest),
                                new AtpRemarksOutput().populateSavedRemarks(savedRemarksFromIms));
                    }
                })
            .thenDoTransactWhile(errorMsgExits(), WipsTransactionConstant.AAIMG56X_S01I0,
                new G56xTransactionInput().loadG56xInputApproveOrReject(atpRemarksRequest, g56xInputHolder),
                new AtpRemarksOutput().populate(warningMessages))
            .thenNestTransactionsIfElse(noErrorOrConfirmAction(atpRemarksRequest, warningMessages),
                new Function<ImsTransaction<AtpRemarksOutput>, ImsTransaction<AtpApproveOrRejectResponse>>() {
                    @Override
                    public ImsTransaction<AtpApproveOrRejectResponse> apply(final ImsTransaction<AtpRemarksOutput> txn) {
                        return txn.thenTransact(WipsTransactionConstant.S01I0_TRANSACTION,
                            loadInputForConfirm(atpRemarksRequest),
                            loadOutput()).map(toResponse());
                    }

                }, new Function<ImsTransaction<AtpRemarksOutput>, ImsTransaction<AtpApproveOrRejectResponse>>() {
                    @Override
                    public ImsTransaction<AtpApproveOrRejectResponse> apply(final ImsTransaction<AtpRemarksOutput> txn) {
                        return txn.map(toResponse(warningMessages));
                    }
                });
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

    private FunctionThrowsException<S01i1TransactionOutput, AtpApproveOrRejectResponse, Exception> toResponse() {
        return new FunctionThrowsException<S01i1TransactionOutput, AtpApproveOrRejectResponse, Exception>() {

            @Override
            public AtpApproveOrRejectResponse apply(
                    final S01i1TransactionOutput approveOutput)
                    throws Exception {
                final AtpApproveOrRejectResponse response = new AtpApproveOrRejectResponse();
                response.setApproveOrRejectMessage(approveOutput.getConfirmMessage());
                return response;
            }

        };
    }

    private FunctionThrowsException<AtpRemarksOutput, AtpApproveOrRejectResponse, Exception> toResponse(
            final List<ApproveWarningHandler> warningMessages) {
        return new FunctionThrowsException<AtpRemarksOutput, AtpApproveOrRejectResponse, Exception>() {

            @Override
            public AtpApproveOrRejectResponse apply(final AtpRemarksOutput output)
                    throws Exception {
                final AtpApproveOrRejectResponse response = new AtpApproveOrRejectResponse();
                response.setWarningMessagesList(warningMessages);
                response.setErrorFlag(true);
                response.setErrorMessage(warningMessages.toString());
                return response;
            }
        };
    }

    private BiFunctionThrowsException<DataArea, String, S01i1TransactionOutput, Exception> loadOutput() {
        return new BiFunctionThrowsException<DataArea, String, S01i1TransactionOutput, Exception>() {

            @Override
            public S01i1TransactionOutput apply(final DataArea outputDataArea,
                    final String rawoutput) throws Exception {
                final S01i1TransactionOutput transactionOutput = new S01i1TransactionOutput();
                if (rawoutput.length() == 1616) {
                    transactionOutput.setConfirmMessage(rawoutput.substring(62, 138));
                    transactionOutput.setErrorFlag(false);
                } else {
                    throw new WipsImsTransactionException(
                            WipsTransactionConstant.S01I0_TRANSACTION,
                            rawoutput.substring(38, 113));
                }
                return transactionOutput;
            }
        };
    }

    private BiConsumerThrowsException<AtpRemarksOutput, DataArea, ConnectorException> loadInputForConfirm(
            final AtpRemarksRequest atpRemarksRequest) {
        return new BiConsumerThrowsException<AtpRemarksOutput, DataArea, ConnectorException>() {

            @Override
            public void accept(final AtpRemarksOutput output, final DataArea inputDataArea)
                    throws ConnectorException {
                final WipsImsInput s01i0Input = new WipsImsInput();
                s01i0Input.setLterm(atpRemarksRequest.getLterm());
                s01i0Input.setTpiPfKey(WipsConstant.PFKEY13);

                inputDataArea.put("TPI-PFKEY", s01i0Input.getTpiPfKey());
            }
        };
    }

    Predicate<AtpRemarksOutput> noErrorOrConfirmAction(
            final AtpRemarksRequest atpRemarksRequest,
            final List<ApproveWarningHandler> warningMessages) {
        return new Predicate<AtpRemarksOutput>() {

            @Override
            public boolean test(final AtpRemarksOutput output) {
                return CONFIRM.equals(atpRemarksRequest.getActionTaken())
                       || warningMessages.size() == 0;
            }
        };
    }

    private Predicate<AtpRemarksOutput> errorMsgExits() {
        return new Predicate<AtpRemarksOutput>() {

            @Override
            public boolean test(final AtpRemarksOutput s01i0TransactionOutput) {
                final String errorMessage = s01i0TransactionOutput.getErrorMessage();
                return errorMessage != null && (errorMessage.contains("MSG-4276")
                                                || errorMessage.contains("MSG-4236")
                                                || errorMessage.contains("MSG-4524"));
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

    private Predicate<AtpRemarksOutput> moreUserRemarks() {
        return new Predicate<AtpRemarksOutput>() {

            @Override
            public boolean test(final AtpRemarksOutput output) {
                return output.getMoreRemarks().contains(WipsTransactionConstant.MORE);
            }
        };
    }
}
