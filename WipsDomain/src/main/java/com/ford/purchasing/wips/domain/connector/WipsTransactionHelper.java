//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.connector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.layer.Category;
import com.ford.purchasing.wips.common.layer.PendingApproval;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.util.TransactionOutputUtil;
import com.ford.purchasing.wips.domain.atp.beans.M00mTransactionOutput;

public class WipsTransactionHelper {

    public M00mTransactionOutput populateM00mOutput(final DataArea outputDataArea)
            throws ConnectorException {
        final M00mTransactionOutput m00mTransactionOutputBean = new M00mTransactionOutput();
        m00mTransactionOutputBean.setUserJobCode(TransactionOutputUtil.getString(
                outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-JOBCODE"));
        m00mTransactionOutputBean.setUserJobName(TransactionOutputUtil.getString(
                outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-EMPNAME"));
        final List<DataArea> pendingApprovalDataAreaList = TransactionOutputUtil
                .getArray(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-BUFFER-SEGLOOP");
        DataArea dataArea;
        PendingApproval pendingApproval;
        final List<PendingApproval> pendingApprovalList = new ArrayList<PendingApproval>();
        for (final Iterator<DataArea> i = pendingApprovalDataAreaList.iterator(); i
                .hasNext();) {
            dataArea = i.next();
            final String approvalCode =
                    TransactionOutputUtil.getString(dataArea, "TPO-CATNO");
            if (WipsConstant.ATP_ENTITY_CODE.equals(approvalCode)
                || WipsConstant.LUMPSUM_ENTITY_CODE.equals(approvalCode)) {
                pendingApproval = new PendingApproval();
                pendingApproval.setCategory(Category.getCategory(approvalCode));
                pendingApproval
                        .setTotalCount(
                                TransactionOutputUtil.getString(dataArea, "TPO-TOTAL"));
                pendingApprovalList.add(pendingApproval);
            }
        }

        final String priceClaimsCount = TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-OPTION3.TPO-OPTION3-CLM-CNT");
        if (StringUtils.isNotEmpty(priceClaimsCount) && !WipsConstant.ZERO_COUNT.equals(priceClaimsCount)) {
            pendingApprovalList.add(pendingPriceClaims(priceClaimsCount));
        }
        m00mTransactionOutputBean.setPendingApprovalList(pendingApprovalList);
        return m00mTransactionOutputBean;
    }

    private PendingApproval pendingPriceClaims(final String priceClaimsCount) {
        final PendingApproval priceClaims = new PendingApproval();
        priceClaims.setCategory(Category.PRICE_CLAIMS);
        priceClaims.setTotalCount(priceClaimsCount);
        return priceClaims;
    }

}
