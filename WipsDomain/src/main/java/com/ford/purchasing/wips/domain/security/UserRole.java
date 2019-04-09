//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.security;

import com.ford.purchasing.wips.common.layer.LumpSumType;
import com.ford.purchasing.wips.common.layer.WipsConstant;

@SuppressWarnings("javadoc")
public abstract class UserRole {

    private static final UserRole OTHER_ROLE = new UserRole() {

        @Override
        public boolean isShortAndLongTermCostEditable(final LumpSumType lumpSumType,
                final String prepaymentType) {
            return false;
        }

        @Override
        public boolean isWorkTaskEditable(final LumpSumType lumpSumType) {
            return false;
        };

        @Override
        public boolean isClassificationEditable(final LumpSumType lumpsumType,
                final String lumpSumVersion) {
            return false;
        }
    };

    private static final UserRole FINANCE_ANALYST = new FinanceAnalyst();
    private static final UserRole FINANCE_LL6 = new FinanceLL6();
    private static final UserRole GLOBAL_SUPPLIER_AUDIT = new GlobalSupplierAudit();

    public static UserRole userRole(final String jobTitle) {
        UserRole userRole = OTHER_ROLE;
        if (WipsConstant.FINANCE_ANALYST_CODE.equalsIgnoreCase(jobTitle)) {
            userRole = FINANCE_ANALYST;
        } else if (WipsConstant.FINANCE_LL6_CODE.equalsIgnoreCase(jobTitle)) {
            userRole = FINANCE_LL6;
        } else if (WipsConstant.GLOBAL_SUPPLIER_AUDIT_CODE.equalsIgnoreCase(jobTitle)) {
            userRole = GLOBAL_SUPPLIER_AUDIT;
        }
        return userRole;
    }

    public abstract boolean isClassificationEditable(LumpSumType lumpsumType,
            String lumpSumVersion);

    protected boolean isInitialAmendment(final String lumpSumVersion) {
        return WipsConstant.INITIAL_AMENDMENT.equals(lumpSumVersion);
    }

    public boolean isWorkTaskEditable(final LumpSumType lumpSumType) {
        boolean editable = true;
        switch (lumpSumType) {
        case FORECAST:
            editable = false;
            break;
        }
        return editable;
    }

    public abstract boolean isShortAndLongTermCostEditable(final LumpSumType lumpSumType,
            final String prepaymentType);

    public boolean isGsaAuditEditable() {
        return false;
    }
}
