//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.lumpsum.beans;

@SuppressWarnings("javadoc")
public class LumpSumSecurity {

    private boolean classificationEditable;
    private boolean workTaskEditable;
    private boolean shortAndLongTermEditable;
    private boolean globalSupplierAuditEditable;

    public boolean isClassificationEditable() {
        return this.classificationEditable;
    }

    public void setClassificationEditable(final boolean classificationEditable) {
        this.classificationEditable = classificationEditable;
    }

    public boolean isWorkTaskEditable() {
        return this.workTaskEditable;
    }

    public void setWorkTaskEditable(final boolean workTaskEditable) {
        this.workTaskEditable = workTaskEditable;
    }

    public boolean isShortAndLongTermEditable() {
        return this.shortAndLongTermEditable;
    }

    public void setShortAndLongTermEditable(final boolean shortAndLongTermEditable) {
        this.shortAndLongTermEditable = shortAndLongTermEditable;
    }

    public boolean isGlobalSupplierAuditEditable() {
        return this.globalSupplierAuditEditable;
    }

    public void setGlobalSupplierAuditEditable(final boolean globalSupplierAuditEditable) {
        this.globalSupplierAuditEditable = globalSupplierAuditEditable;
    }
}
