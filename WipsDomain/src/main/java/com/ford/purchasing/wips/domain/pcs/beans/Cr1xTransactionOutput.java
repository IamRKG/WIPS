package com.ford.purchasing.wips.domain.pcs.beans;

import java.util.Map;

import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.connector.BiFunctionThrowsException;
import com.ford.purchasing.wips.common.layer.ApprovalDetail;
import com.ford.purchasing.wips.common.layer.WipsImsOutput;

public class Cr1xTransactionOutput extends WipsImsOutput {

    public static BiFunctionThrowsException<DataArea, String, PriceClaimRemarks, Exception> getCr1xOutput(
        final Map<String, ApprovalDetail> remarksMap) {
        return new BiFunctionThrowsException<DataArea, String, PriceClaimRemarks, Exception>() {

            @Override
            public PriceClaimRemarks apply(final DataArea outputDataArea, final String rawOutput) throws Exception {
                final PriceClaimRemarks priceClaimRemarks = new PriceClaimRemarks().from(outputDataArea);
                setPcsRemarks(priceClaimRemarks, remarksMap);
                return priceClaimRemarks;
            }
        };
    }

    public BiFunctionThrowsException<DataArea, String, PriceClaimRemarks, Exception> getCr1xSaveOutput() {
        return new BiFunctionThrowsException<DataArea, String, PriceClaimRemarks, Exception>() {

            @Override
            public PriceClaimRemarks apply(final DataArea outputDataArea, final String rawOutput) throws Exception {
                return new PriceClaimRemarks().from(outputDataArea);
            }
        };
    }

    private static void setPcsRemarks(final PriceClaimRemarks from, final Map<String, ApprovalDetail> remarksMap) {
        final ApprovalDetail approver = remarksMap.get(from.getApprovalDetails().getJobCode());
        approver.setRemarks(from.getApprovalDetails().getRemarks());
    }
}
