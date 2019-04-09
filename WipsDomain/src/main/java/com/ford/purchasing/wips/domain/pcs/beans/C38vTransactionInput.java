//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.pcs.beans;

import org.apache.commons.lang3.StringUtils;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.connector.BiConsumerThrowsException;
import com.ford.purchasing.wips.common.layer.WipsConstant;

public class C38vTransactionInput {

    public static BiConsumerThrowsException<FinancialImpactInformation, DataArea, ConnectorException> loadC38vInput(
        final short pfKey, final String selectedPM, final String selectedYear) {
        return new BiConsumerThrowsException<FinancialImpactInformation, DataArea, ConnectorException>() {

            @Override
            public void accept(final FinancialImpactInformation financialImpactInformation,
                final DataArea inputDataArea)
                throws ConnectorException {
                populate(pfKey, selectedPM, selectedYear, financialImpactInformation, inputDataArea);
            }

        };
    }

    private static String getSelectedBU(final String selectedBU) {
        return StringUtils.isEmpty(selectedBU) ? WipsConstant.STAR_ALL_UNDERSCORE : selectedBU;
    }

    private static String getSelectedYear(final String selectedYear) {
        return StringUtils.isEmpty(selectedYear) ? WipsConstant.STAR_ALL : selectedYear;
    }

    private static String getSelectedPM(final String selectedPM) {
        return StringUtils.isEmpty(selectedPM) ? WipsConstant.STAR_ALL : selectedPM;
    }

    private static void populate(final short pfKey, final String selectedPM, final String selectedYear,
        final FinancialImpactInformation financialImpactInformation, final DataArea inputDataArea) {
        inputDataArea.put("TPI-IOINDIC", WipsConstant.InputI);
        inputDataArea.put("TPI-PFKEY", pfKey);
        inputDataArea.put("TPI-SELBU", getSelectedBU(financialImpactInformation.getSelectedBU()));
        inputDataArea.put("TPI-SELPM", getSelectedPM(selectedPM));
        inputDataArea.put("TPI-SELYEAR", getSelectedYear(selectedYear));
    }

    public static BiConsumerThrowsException<FinancialImpactInformation, DataArea, ConnectorException> loadC38vInput(
        final String selectedPM, final String selectedYear) {
        return new BiConsumerThrowsException<FinancialImpactInformation, DataArea, ConnectorException>() {

            @Override
            public void accept(final FinancialImpactInformation financialImpactInformation, final DataArea inputDataArea)
                throws ConnectorException {
                final short pfKey =
                    financialImpactInformation.getHasMorePages().contains(WipsConstant.MORE) ? WipsConstant.PFKEY8
                        : WipsConstant.PFKEY18;
                populate(pfKey, selectedPM, selectedYear, financialImpactInformation, inputDataArea);
            }
        };
    }

}
