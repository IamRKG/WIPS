//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.priceclaims.service;

import java.util.ArrayList;
import java.util.List;

import com.ford.purchasing.wips.common.atp.EppsTransactionInput;
import com.ford.purchasing.wips.common.connector.FunctionThrowsException;
import com.ford.purchasing.wips.common.connector.Predicate;
import com.ford.purchasing.wips.common.layer.ApprovalDetail;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.util.StringUtil;
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
import com.ford.purchasing.wips.domain.pcs.beans.Cr3vTransactionOutput;
import com.ford.purchasing.wips.domain.pcs.beans.PriceClaimRemarks;
import com.ford.purchasing.wips.domain.pcs.beans.PriceClaimSummaryInformation;
import com.ford.purchasing.wips.domain.pcs.beans.Q43lTransactionInput;
import com.ford.purchasing.wips.domain.pcs.beans.Q43lTransactionOutput;
import com.ford.purchasing.wips.domain.priceclaims.beans.PriceClaimsResponse;

public class SavePriceClaimRemarks implements Function<ImsConversationCtx, ImsTransaction<PriceClaimsResponse>> {

    private PriceClaimsRequest priceClaimsRequest;

    public SavePriceClaimRemarks(final PriceClaimsRequest priceClaimsRequest) {
        this.priceClaimsRequest = priceClaimsRequest;
    }

    @Override
    public ImsTransaction<PriceClaimsResponse> apply(final ImsConversationCtx ctx) {
        final PriceClaimsResponse priceClaimsResponse = new PriceClaimsResponse();
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
            .thenTransact(WipsTransactionConstant.AAIMC36U_CR1X_TRANSACTION_NAME,
                C36uTransactionInput.loadC36uInput(WipsConstant.PFKEY9),
                new Cr1xTransactionOutput().getCr1xSaveOutput())
            .thenTransactWhile(saveRemarksPredicate(this.priceClaimsRequest),
                WipsTransactionConstant.AAIMCR1X_TRANSACTION_NAME,
                Cr1xTransactionInput.loadCr1xInput(WipsConstant.PFKEY8,
                    this.priceClaimsRequest),
                new Cr1xTransactionOutput().getCr1xSaveOutput())
            .thenTransact(WipsTransactionConstant.AAIMCR1X_CR3V_TRANSACTION_NAME,
                new Cr1xTransactionInput().loadCr1xInputRemarks(this.priceClaimsRequest,
                    WipsConstant.PFKEY15),
                Cr3vTransactionOutput.getCr3vOutput())
            .map(savedRemarksToResponse(this.priceClaimsRequest, priceClaimsResponse));
    }

    private FunctionThrowsException<Cr3vTransactionOutput, PriceClaimsResponse, Exception> savedRemarksToResponse(
        final PriceClaimsRequest priceClaimsRequest, final PriceClaimsResponse priceClaimsResponse) {
        return new FunctionThrowsException<Cr3vTransactionOutput, PriceClaimsResponse, Exception>() {

            @Override
            public PriceClaimsResponse apply(final Cr3vTransactionOutput summary) throws Exception {
                final ApprovalDetail detail = new ApprovalDetail();
                detail.setJobCode(priceClaimsRequest.getCurrentJobCode());
                detail.setRemarks(
                    StringUtil.splitStringPcsRemarks(StringUtil.buildUserRemarks(priceClaimsRequest.getRemarks())));
                final List<ApprovalDetail> remarks = new ArrayList<ApprovalDetail>();
                remarks.add(detail);
                priceClaimsResponse.setPriceClaimRemarks(remarks);
                return priceClaimsResponse;
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
