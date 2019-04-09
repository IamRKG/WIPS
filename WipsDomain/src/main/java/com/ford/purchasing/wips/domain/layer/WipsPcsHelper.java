package com.ford.purchasing.wips.domain.layer;

import static com.ford.purchasing.wips.common.layer.util.TransactionOutputUtil.*;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.layer.util.WipsUtil;
import com.ford.purchasing.wips.domain.pcs.beans.PriceClaimSummaryInformation;

public class WipsPcsHelper {
    public PriceClaimSummaryInformation populatePcsSummaryInformation(
            final DataArea outputDataArea)
            throws ConnectorException {
        final PriceClaimSummaryInformation pcsSummaryInformation =
                new PriceClaimSummaryInformation();
        pcsSummaryInformation.setLinkedPcNumber(getStringWithUnderScoreTrimmed(
                outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-CLAIMNO"));
        pcsSummaryInformation.setSupplier(getStringWithUnderScoreTrimmed(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-SCODE"));
        pcsSummaryInformation.setBuyer(getStringWithUnderScoreTrimmed(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-BCODE1") + " - "
                                       + getStringWithUnderScoreTrimmed(outputDataArea,
                                               "TP-OUTPUT-BUFFER-FIELDS.TPO-BNAME1"));
        pcsSummaryInformation.setCausalFactor(getStringWithUnderScoreTrimmed(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-CAUSCDE"));
        pcsSummaryInformation.setEffectiveDate(WipsUtil.convertDateStringToFormattedDateString(getStringWithUnderScoreTrimmed(
                outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-EFFDATE")));
        pcsSummaryInformation.setExchangeRate(getStringWithUnderScoreTrimmed(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-CURRENCY")
                                              + " "
                                              + getStringWithUnderScoreTrimmed(
                                                      outputDataArea,
                                                      "TP-OUTPUT-BUFFER-FIELDS.TPO-EXCH-CHAR"));
        pcsSummaryInformation.setVolumes(getStringWithUnderScoreTrimmed(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-VOLUME"));
        pcsSummaryInformation.setPcsRemark(getStringWithUnderScoreTrimmed(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-PCSRMK"));
        pcsSummaryInformation.setIntPrc(getStringWithUnderScoreTrimmed(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-INTPRC"));
        /* pcsSummaryInformation.setPartsIncluded(getStringWithUnderScoreTrimmed(outputDataArea,
                 "TP-OUTPUT-BUFFER-FIELDS.TPO-PARTINCL"));*/
        return pcsSummaryInformation;

    }
}
