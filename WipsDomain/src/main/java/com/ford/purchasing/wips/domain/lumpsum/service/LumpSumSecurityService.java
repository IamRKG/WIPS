//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.lumpsum.service;

import com.ford.purchasing.wips.domain.lumpsum.beans.LumpSumResponse;
import com.ford.purchasing.wips.domain.lumpsum.beans.LumpSumSecurity;
import com.ford.purchasing.wips.domain.security.WipsSecurity;

@SuppressWarnings("javadoc")
public class LumpSumSecurityService {

    public LumpSumSecurity getLumpSumSecurity(final String jobTitle,
            final LumpSumResponse lumpSumResponse) {
        final LumpSumSecurity lumpSumSecurity = new LumpSumSecurity();
        final String lsTypeCode = lumpSumResponse.getLumpSumInformation().getLsTypeCode();
        final String lumpSumVersion =
                lumpSumResponse.getLumpSumInformation().getCurrentAmendment();
        final String prePayment =
                lumpSumResponse.getAdditionalLumpSumInformation().getPrePayment();
        final WipsSecurity securityForUserRole = new WipsSecurity(jobTitle);
        lumpSumSecurity.setClassificationEditable(
                securityForUserRole.isClassificationEditable(lsTypeCode, lumpSumVersion));
        lumpSumSecurity
                .setWorkTaskEditable(
                        securityForUserRole.isWorkTaskEditable(lsTypeCode));
        lumpSumSecurity.setShortAndLongTermEditable(securityForUserRole
                .isShortAndLongTermCostEditable(lsTypeCode, prePayment));
        lumpSumSecurity
                .setGlobalSupplierAuditEditable(
                        securityForUserRole.isGsaAuditEditable());
        return lumpSumSecurity;
    }

}
