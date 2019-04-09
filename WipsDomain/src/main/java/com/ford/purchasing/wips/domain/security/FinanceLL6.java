//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.security;

import com.ford.purchasing.wips.common.layer.LumpSumType;
import com.ford.purchasing.wips.common.layer.WipsConstant;

@SuppressWarnings("javadoc")
public class FinanceLL6 extends UserRole {

    @Override
    public boolean isClassificationEditable(final LumpSumType lumpsumType,
            final String lumpSumVersion) {
        boolean editable = false;
        switch (lumpsumType) {
        case FORECAST:
        case RESERVE_NTE:
        case SETTLEMENT_NTE:
        case UNLINKED_RESERVE:
        case UNLINKED_SETTLEMENT:
            editable = isInitialAmendment(lumpSumVersion);
            break;
        case NTE:
            editable = true;
            break;
        default:
            break;
        }
        return editable;
    }

    @Override
    public boolean isShortAndLongTermCostEditable(final LumpSumType lumpSumType,
            final String prepaymentType) {
        boolean editable = false;
        if (WipsConstant.PAYMENT_TYPE_PREPAID.equals(prepaymentType)) {
            editable = true;
        }
        return editable;
    }

}
