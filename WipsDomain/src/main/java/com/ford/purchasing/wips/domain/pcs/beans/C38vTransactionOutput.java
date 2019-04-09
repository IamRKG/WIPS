package com.ford.purchasing.wips.domain.pcs.beans;

import java.util.List;

import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.connector.BiFunctionThrowsException;
import com.ford.purchasing.wips.common.connector.Predicate;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.WipsImsOutput;
import com.ford.purchasing.wips.common.priceclaims.PCSBusinessUnit;
import com.ford.purchasing.wips.common.priceclaims.PurchasingManagerDetail;

public class C38vTransactionOutput extends WipsImsOutput {

    public static BiFunctionThrowsException<DataArea, String, FinancialImpactInformation, Exception> getC38vOutput(
        final String selectedPM, final List<String> purchaseManager, final List<PCSBusinessUnit> pcsBusinessUnits,
        final List<PurchasingManagerDetail> purchasingManagerDetailList) {
        return new BiFunctionThrowsException<DataArea, String, FinancialImpactInformation, Exception>() {

            @Override
            public FinancialImpactInformation apply(final DataArea outputDataArea, final String rawOutput)
                throws Exception {
                return new FinancialImpactInformation().from(outputDataArea, rawOutput, selectedPM,
                    purchaseManager, pcsBusinessUnits, purchasingManagerDetailList);
            }

        };
    }

    public static BiFunctionThrowsException<DataArea, String, FinancialImpactInformation, Exception> getC38vOutputAfterClear(
        final String selectedPM, final List<String> purchaseManager, final List<PCSBusinessUnit> pcsBusinessUnits,
        final List<PurchasingManagerDetail> purchasingManagerDetailList) {
        if (WipsConstant.STAR_ALL.equalsIgnoreCase(selectedPM)) {
            purchasingManagerDetailList.clear();
            purchaseManager.clear();
            purchaseManager.add(WipsConstant.ALL);
        }
        return getC38vOutput(selectedPM, purchaseManager, pcsBusinessUnits, purchasingManagerDetailList);
    }
    
    public static Predicate<FinancialImpactInformation> isMoreDataPredicate() {
        return new Predicate<FinancialImpactInformation>() {
            @Override
            public boolean test(final FinancialImpactInformation financialImpactInformation) {
                return financialImpactInformation.getHasMorePages().contains(WipsConstant.MORE_PAGES);
            }
        };
    }

}
