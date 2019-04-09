//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.security;

import com.ford.purchasing.wips.common.layer.LumpSumType;

public class GlobalSupplierAudit extends UserRole {

    @Override
    public boolean isClassificationEditable(final LumpSumType lumpsumType,
            final String lumpSumVersion) {
        return false;
    }

    @Override
    public boolean isShortAndLongTermCostEditable(final LumpSumType lumpSumType,
            final String prepaymentType) {
        return false;
    }

    @Override
    public boolean isGsaAuditEditable() {
        return true;
    }

    @Override
    public boolean isWorkTaskEditable(final LumpSumType lumpSumType) {
        return false;
    }
}
