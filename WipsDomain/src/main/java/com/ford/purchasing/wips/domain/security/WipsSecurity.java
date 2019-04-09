//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.security;

import com.ford.purchasing.wips.common.layer.LumpSumType;

@SuppressWarnings("javadoc")
public class WipsSecurity {

    private UserRole userRole;

    public WipsSecurity(final String jobTitle) {
        this.userRole = UserRole.userRole(jobTitle);
    }

    public boolean isClassificationEditable(final String lumpsumType,
            final String lumpSumVersion) {
        return this.userRole.isClassificationEditable(
                LumpSumType.getLumpSumType(lumpsumType), lumpSumVersion);
    }

    public boolean isWorkTaskEditable(final String lumpsumType) {
        return this.userRole.isWorkTaskEditable(
                LumpSumType.getLumpSumType(lumpsumType));
    }

    public boolean isShortAndLongTermCostEditable(final String lumpsumType,
            final String prepaymentType) {
        return this.userRole.isShortAndLongTermCostEditable(
                LumpSumType.getLumpSumType(lumpsumType), prepaymentType);
    }

    public boolean isGsaAuditEditable() {
        return this.userRole.isGsaAuditEditable();
    }

}
