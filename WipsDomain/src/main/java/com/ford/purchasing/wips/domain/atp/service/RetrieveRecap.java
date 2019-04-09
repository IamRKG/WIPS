//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.atp.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.ford.purchasing.wips.common.atp.AtpRequest;
import com.ford.purchasing.wips.common.atp.AtpSupplierDetail;
import com.ford.purchasing.wips.common.atp.EppsTransactionInput;
import com.ford.purchasing.wips.common.atp.G51xTransactionInput;
import com.ford.purchasing.wips.common.atp.G51xTransactionOutput;
import com.ford.purchasing.wips.common.connector.FunctionThrowsException;
import com.ford.purchasing.wips.common.connector.Predicate;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.WipsPendingApprovalOutput;
import com.ford.purchasing.wips.domain.atp.AtpRecapResponse;
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
import com.ford.purchasing.wips.domain.layer.exception.WipsDb2DatabaseException;
import com.ford.purchasing.wips.domain.pcs.beans.A02lTransactionOutput;

@SuppressWarnings("javadoc")
public class RetrieveRecap implements
        Function<ImsConversationCtx, ImsTransaction<AtpRecapResponse>> {
    private AtpRequest atpRecapRequest;

    @Inject
    public RetrieveRecap() {
    }

    public RetrieveRecap(final AtpRequest atpRecapRequest) {
        this.atpRecapRequest = atpRecapRequest;
    }

    @Override
    public ImsTransaction<AtpRecapResponse> apply(final ImsConversationCtx ctx) {
        final List<WipsPendingApprovalOutput> atpRecap =
                new ArrayList<WipsPendingApprovalOutput>();
        final G53xTransactionOutput g53xOutput = new G53xTransactionOutput();
        final List<AtpSupplierDetail> suppliersCollector = new ArrayList<AtpSupplierDetail>();
        return ctx
                .transact(WipsTransactionConstant.AAIMEPPS_TRANSACTION_NAME,
                        this.atpRecapRequest,
                        new EppsTransactionInput().loadEppsInput(),
                        new M00mTransactionOutput().getEppsOutput())
                .thenTransactIf(new M00mTransactionOutput()
                        .notUserJobCode(this.atpRecapRequest),
                        WipsTransactionConstant.AAIMM00M_TRANSACTION_NAME,
                        new M00mTransactionInput()
                                .loadM00mSwitchJobInput(this.atpRecapRequest),
                        new M00mTransactionOutput().getM00mSwitchJobOutput())
                .thenTransact(
                        WipsTransactionConstant.AAIMM00M_Q01X_TRANSACTION_NAME,
                        new M00mTransactionInput()
                                .loadM00mInput(this.atpRecapRequest),
                        new Q01xTransactionOutput()
                                .getQ01xOutput(atpRecap))
                .thenTransact(
                        WipsTransactionConstant.Q01X_TRANSACTION,
                        new Q01xTransactionInput()
                                .loadQ01xInput(this.atpRecapRequest,
                                        WipsConstant.PFKEY0, WipsConstant.SELCAT),
                        new Q01xTransactionOutput()
                                .getQ01xOutput(atpRecap))
                .thenNestTransactionsIfElse(nextScreenIsG53x(),
                        new Function<ImsTransaction<Q01xTransactionOutput>, ImsTransaction<G53xTransactionOutput>>() {

                            @Override
                            public ImsTransaction<G53xTransactionOutput> apply(
                                    final ImsTransaction<Q01xTransactionOutput> txn) {
                                return txn.thenTransact(
                                        WipsTransactionConstant.Q01XG53X_TRANSACTION,
                                        new Q01xTransactionInput()
                                                .loadQ01xInput(
                                                        RetrieveRecap.this.atpRecapRequest,
                                                        WipsConstant.PFKEY13,
                                                        WipsConstant.SELCAT),
                                        new G53xTransactionOutput()
                                                .getG53xOutput(suppliersCollector,
                                                        g53xOutput));
                            }

                        },
                        new Function<ImsTransaction<Q01xTransactionOutput>, ImsTransaction<G53xTransactionOutput>>() {

                            @Override
                            public ImsTransaction<G53xTransactionOutput> apply(
                                    final ImsTransaction<Q01xTransactionOutput> txn) {
                                return txn.thenTransact(
                                        WipsTransactionConstant.Q01XG51X_TRANSACTION,
                                        new Q01xTransactionInput()
                                                .loadQ01xInput(
                                                        RetrieveRecap.this.atpRecapRequest,
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
                .thenTransactWhile(thereAreMoreSuppliers(),
                        WipsTransactionConstant.G53X_TRANSACTION,
                        new G53xTransactionInput().loadG53xInputFromG53(WipsConstant.PFKEY8),
                        new G53xTransactionOutput()
                                .getG53xOutput(suppliersCollector,
                                        g53xOutput))
                .thenNestTransactionsIf(thereAreMoreApprovers(),
                        new Function<ImsTransaction<G53xTransactionOutput>, ImsTransaction<G53xTransactionOutput>>() {

                            @Override
                            public ImsTransaction<G53xTransactionOutput> apply(
                                    final ImsTransaction<G53xTransactionOutput> txn) {
                                return txn.thenTransact(
                                        WipsTransactionConstant.G53X_A02L_TRANSACTION,
                                        new G53xTransactionInput()
                                                .loadG53xInputFromG53(WipsConstant.PFKEY18),
                                        new A02lTransactionOutput().getA02lOutput())
                                        .map(approvers(g53xOutput));
                            }
                        })
                .map(recapDetails(suppliersCollector, g53xOutput));
    }

    private FunctionThrowsException<A02lTransactionOutput, G53xTransactionOutput, Exception> approvers(
            final G53xTransactionOutput g53xOutput) {
        return new FunctionThrowsException<A02lTransactionOutput, G53xTransactionOutput, Exception>() {

            @Override
            public G53xTransactionOutput apply(final A02lTransactionOutput output)
                    throws Exception {
                if (output.getApprovers() != null)
                    g53xOutput.setApprovers(output.getApprovers());
                return g53xOutput;
            }
        };
    }

    private Predicate<G53xTransactionOutput> thereAreMoreApprovers() {
        return new Predicate<G53xTransactionOutput>() {

            @Override
            public boolean test(final G53xTransactionOutput g53xOutput) {
                return g53xOutput.isHasMoreApprovers();
            }
        };
    }

    private FunctionThrowsException<G53xTransactionOutput, AtpRecapResponse, WipsDb2DatabaseException> recapDetails(
            final List<AtpSupplierDetail> suppliersCollector,
            final G53xTransactionOutput g53xOutput) {
        return new FunctionThrowsException<G53xTransactionOutput, AtpRecapResponse, WipsDb2DatabaseException>() {

            @Override
            public AtpRecapResponse apply(final G53xTransactionOutput transactionOutput)
                    throws WipsDb2DatabaseException {
                final AtpRecapResponse response = new AtpRecapResponse();
                g53xOutput.setG53xTransactionInput(null);
                g53xOutput.setTotalCost(calculatetotalAnnualCost(suppliersCollector));
                g53xOutput.setSuppliers(suppliersCollector);
                response.setG53xTransactionOutput(g53xOutput);
                return response;
            }

        };

    }

    private String calculatetotalAnnualCost(
            final List<AtpSupplierDetail> suppliersCollector) {
        Double annualCost = 0.0;
        Double totalAnnualCost = 0.0;
        AtpSupplierDetail bean = new AtpSupplierDetail();
        for (int count = 0; count < suppliersCollector.size(); count++) {
            bean = (AtpSupplierDetail)suppliersCollector.get(count);
            annualCost = 0.0;
            final String annualCostValue = bean.getAnnualCost();
            if (annualCostValue != null && !"".equals(annualCostValue)
                && !"00".equals(bean.getRecapType())) {
                annualCost = Double.parseDouble(
                        annualCostValue.replace(",", "").replace("+", "").replace("-", ""));
            }
            if ("+".equals(bean.getCostSign()))
                totalAnnualCost += annualCost;
            else
                totalAnnualCost -= annualCost;
        }
        final DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(totalAnnualCost).toString() + " USD";

    }

    private Predicate<G53xTransactionOutput> thereAreMoreSuppliers() {
        return new Predicate<G53xTransactionOutput>() {

            @Override
            public boolean test(final G53xTransactionOutput output) {
                return output.isHasMoreSuppliers();
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
