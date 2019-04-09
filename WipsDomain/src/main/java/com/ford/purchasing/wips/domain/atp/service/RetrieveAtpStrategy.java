//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.atp.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.ford.purchasing.wips.common.atp.AtpRequest;
import com.ford.purchasing.wips.common.atp.AtpStrategyResponse;
import com.ford.purchasing.wips.common.atp.AtpSupplierDetail;
import com.ford.purchasing.wips.common.atp.EppsTransactionInput;
import com.ford.purchasing.wips.common.atp.G51xTransactionInput;
import com.ford.purchasing.wips.common.atp.G51xTransactionOutput;
import com.ford.purchasing.wips.common.atp.StrategyOutput;
import com.ford.purchasing.wips.common.connector.FunctionThrowsException;
import com.ford.purchasing.wips.common.connector.Predicate;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.WipsPendingApprovalOutput;
import com.ford.purchasing.wips.domain.atp.beans.G52xTransactionInput;
import com.ford.purchasing.wips.domain.atp.beans.G52xTransactionOutput;
import com.ford.purchasing.wips.domain.atp.beans.G53xTransactionInput;
import com.ford.purchasing.wips.domain.atp.beans.G53xTransactionOutput;
import com.ford.purchasing.wips.domain.atp.beans.G55xTransactionInput;
import com.ford.purchasing.wips.domain.atp.beans.G55xTransactionOutput;
import com.ford.purchasing.wips.domain.atp.beans.M00mTransactionInput;
import com.ford.purchasing.wips.domain.atp.beans.M00mTransactionOutput;
import com.ford.purchasing.wips.domain.atp.beans.Q01xTransactionInput;
import com.ford.purchasing.wips.domain.atp.beans.Q01xTransactionOutput;
import com.ford.purchasing.wips.domain.connector.Function;
import com.ford.purchasing.wips.domain.connector.ImsConversationCtx;
import com.ford.purchasing.wips.domain.connector.ImsTransaction;
import com.ford.purchasing.wips.domain.layer.WipsTransactionConstant;

public class RetrieveAtpStrategy
        implements Function<ImsConversationCtx, ImsTransaction<AtpStrategyResponse>> {

    private AtpRequest strategyRequest;

    @Inject
    public RetrieveAtpStrategy() {
    }

    public RetrieveAtpStrategy(final AtpRequest strategyRequest) {
        this.strategyRequest = strategyRequest;
    }

    @Override
    public ImsTransaction<AtpStrategyResponse> apply(final ImsConversationCtx ctx) {
        final AtpStrategyResponse response = new AtpStrategyResponse();
        final List<AtpSupplierDetail> suppliersCollector = new ArrayList<AtpSupplierDetail>();
        final List<WipsPendingApprovalOutput> atpRecap =
                new ArrayList<WipsPendingApprovalOutput>();
        final G53xTransactionOutput g53xOutput = new G53xTransactionOutput();
        final G55xTransactionOutput g55xOutput = new G55xTransactionOutput();
        return ctx
                .transact(WipsTransactionConstant.AAIMEPPS_TRANSACTION_NAME,
                        this.strategyRequest,
                        new EppsTransactionInput().loadEppsInput(),
                        new M00mTransactionOutput().getEppsOutput())
                .thenTransactIf(
                        new M00mTransactionOutput().notUserJobCode(this.strategyRequest),
                        WipsTransactionConstant.AAIMM00M_TRANSACTION_NAME,
                        new M00mTransactionInput()
                                .loadM00mSwitchJobInput(this.strategyRequest),
                        new M00mTransactionOutput().getM00mSwitchJobOutput())
                .thenTransact(WipsTransactionConstant.AAIMM00M_Q01X_TRANSACTION_NAME,
                        new M00mTransactionInput().loadM00mInput(this.strategyRequest),
                        new Q01xTransactionOutput().getQ01xOutput(atpRecap))
                .thenTransact(WipsTransactionConstant.Q01X_TRANSACTION,
                        new Q01xTransactionInput().loadQ01xInput(this.strategyRequest,
                                WipsConstant.PFKEY0,
                                WipsConstant.SELCAT),
                        new Q01xTransactionOutput().getQ01xOutput(atpRecap))
                .thenNestTransactionsIfElse(nextScreenIsG53x(),
                        new Function<ImsTransaction<Q01xTransactionOutput>, ImsTransaction<G53xTransactionOutput>>() {

                            @Override
                            public ImsTransaction<G53xTransactionOutput> apply(
                                    final ImsTransaction<Q01xTransactionOutput> txn) {
                                return txn.thenTransact(
                                        WipsTransactionConstant.Q01XG53X_TRANSACTION,
                                        new Q01xTransactionInput().loadQ01xInput(
                                                RetrieveAtpStrategy.this.strategyRequest,
                                                WipsConstant.PFKEY13,
                                                WipsConstant.SELCAT),
                                        new G53xTransactionOutput().getG53xOutput(
                                                suppliersCollector, g53xOutput));
                            }

                        },
                        new Function<ImsTransaction<Q01xTransactionOutput>, ImsTransaction<G53xTransactionOutput>>() {

                            @Override
                            public ImsTransaction<G53xTransactionOutput> apply(
                                    final ImsTransaction<Q01xTransactionOutput> txn) {
                                return txn
                                        .thenTransact(
                                                WipsTransactionConstant.Q01XG51X_TRANSACTION,
                                                new Q01xTransactionInput().loadQ01xInput(
                                                        RetrieveAtpStrategy.this.strategyRequest,
                                                        WipsConstant.PFKEY13,
                                                        WipsConstant.SELCAT),
                                                new G51xTransactionOutput().getG51xOutput())
                                        .thenTransact(
                                                WipsTransactionConstant.G51XG53XTRANSACTION,
                                                new G51xTransactionInput().loadG51xInput(),
                                                new G53xTransactionOutput()
                                                        .getG53xOutput(suppliersCollector,
                                                                g53xOutput));
                            }
                        })
                .thenTransact(WipsTransactionConstant.AAIMG53X_G52X_TRANSACTION_NAME,
                        new G53xTransactionInput().loadG53xInputFromG53(
                                WipsConstant.PFKEY17),
                        new G52xTransactionOutput().getG52xOutput())
                .map(g52xOutputToStrategyResponse(response))
                .thenTransact(WipsTransactionConstant.AAIMG52X_G55X_TRANSACTION_NAME,
                        new G52xTransactionInput().loadG52xInput(),
                        new G55xTransactionOutput().getG55xOutput(g55xOutput))
                .thenTransactIf(isMorePlants(), WipsTransactionConstant.G55X_TRANSACTION,
                        new G55xTransactionInput().loadG55xInput(),
                        new G55xTransactionOutput().getG55xOutput(g55xOutput))
                .map(strategyResponse(response));

    }

    public void setAtpRequest(final AtpRequest atpStrategyRequest) {
        this.strategyRequest = atpStrategyRequest;
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

    private Predicate<G55xTransactionOutput> isMorePlants() {
        return new Predicate<G55xTransactionOutput>() {
            @Override
            public boolean test(final G55xTransactionOutput g55xTransactionOutput) {
                return g55xTransactionOutput.isMorePlants();
            }
        };
    }

    private FunctionThrowsException<G55xTransactionOutput, AtpStrategyResponse, Exception> strategyResponse(
            final AtpStrategyResponse atpStrategyResponse) {
        return new FunctionThrowsException<G55xTransactionOutput, AtpStrategyResponse, Exception>() {

            @Override
            public AtpStrategyResponse apply(final G55xTransactionOutput output)
                    throws Exception {
                atpStrategyResponse.getStrategyOutput().setPlantDetails(output.getPlants());
                atpStrategyResponse.getStrategyOutput()
                        .setAtpNumber(strategyRequest.getAtpNumber());
                return atpStrategyResponse;
            }
        };
    }

    private FunctionThrowsException<StrategyOutput, StrategyOutput, Exception> g52xOutputToStrategyResponse(
            final AtpStrategyResponse response) {
        return new FunctionThrowsException<StrategyOutput, StrategyOutput, Exception>() {

            @Override
            public StrategyOutput apply(final StrategyOutput output) throws Exception {
                response.setStrategyOutput(output);
                return output;
            }
        };
    }

}
