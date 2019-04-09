//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.security;

import com.ford.purchasing.wips.common.layer.LumpSumType;
import com.ford.purchasing.wips.common.layer.WipsConstant;

@SuppressWarnings("javadoc")
public class FinanceAnalyst extends UserRole {

    @Override
    public boolean isClassificationEditable(final LumpSumType lumpSumType,
            final String lumpSumVersion) {
        boolean editable = false;
        switch (lumpSumType) {
        case UNLINKED_RESERVE:
        case RESERVE_NTE:
        case UNLINKED_SETTLEMENT:
        case SETTLEMENT_NTE:
            editable = isInitialAmendment(lumpSumVersion);
            break;
        case NTE:
            editable = true;
            break;
        default:
            editable = false;
            break;
        }
        return editable;
    }

    @Override
    public boolean isShortAndLongTermCostEditable(final LumpSumType lumpSumType,
            final String prepaymentType) {
        boolean editable = false;
        if (WipsConstant.PAYMENT_TYPE_PREPAID.equals(prepaymentType)
            && !(lumpSumType == LumpSumType.FORECAST)) {
            editable = true;
        }
        return editable;
    }

}
