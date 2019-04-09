//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.pcs.beans;

import java.util.List;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.connector.BiConsumerThrowsException;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.util.StringUtil;
import com.ford.purchasing.wips.common.priceclaims.PriceClaimsRequest;

public class Cr1xTransactionInput {

    public List<String> buildPcsRemarks(final String[] remarks) {
        return StringUtil.splitStringPcsRemarks(StringUtil.buildUserRemarks(remarks));
    }

    public static BiConsumerThrowsException<PriceClaimRemarks, DataArea, ConnectorException> loadCr1xInput(
        final short pfkey, final PriceClaimsRequest priceClaimsRequest) {
        return new BiConsumerThrowsException<PriceClaimRemarks, DataArea, ConnectorException>() {

            @Override
            public void accept(
                final PriceClaimRemarks remarks,
                final DataArea inputDataArea)
                throws ConnectorException {
                if (priceClaimsRequest.getCurrentJobCode().equals(remarks.getApprovalDetails().getJobCode())) {
                    populate(pfkey, remarks.getApprovalDetails().getRemarks(), inputDataArea);
                } else {
                    inputDataArea.put("TPI-IOINDIC", WipsConstant.InputI);
                    inputDataArea.put("TPI-PFKEY", pfkey);
                }
            }
        };
    }

    public BiConsumerThrowsException<PriceClaimRemarks, DataArea, ConnectorException> loadCr1xInputRemarks(
        final PriceClaimsRequest priceClaimsRequest, final short pfKey) {
        return new BiConsumerThrowsException<PriceClaimRemarks, DataArea, ConnectorException>() {

            @Override
            public void accept(final PriceClaimRemarks priceClaimRemarksOutput, final DataArea inputDataArea)
                throws ConnectorException {
                final List<String> remarks = buildPcsRemarks(priceClaimsRequest.getRemarks());
                populate(pfKey, remarks, inputDataArea);
            }
        };
    }

    private static void populate(final short pfkey, final List<String> remarks, final DataArea inputDataArea) {
        inputDataArea.put("TPI-IOINDIC", WipsConstant.InputI);
        inputDataArea.put("TPI-PFKEY", pfkey);
        for (int count = 0; count < 13; count++) {
            if (remarks.size() > count)
                populateInputDataArea(count, inputDataArea, remarks.get(count));
            else
                populateInputDataArea(count, inputDataArea, WipsConstant.BLANK_SPACE_13.toString());
        }
    }

    private static void populateInputDataArea(final int count, final DataArea inputDataArea, final String value) {
        inputDataArea.put("TPI-SORMK" + (count + 1), value);
    }
}
