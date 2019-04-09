package com.ford.purchasing.wips.domain.pcs.beans;

import static com.ford.purchasing.wips.common.layer.util.TransactionOutputUtil.*;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.connector.BiFunctionThrowsException;
import com.ford.purchasing.wips.common.layer.WipsImsOutput;
import com.ford.purchasing.wips.common.layer.exception.WipsImsTransactionException;
import com.ford.purchasing.wips.common.layer.util.TransactionOutputUtil;
import com.ford.purchasing.wips.common.priceclaims.PriceClaimsRequest;
import com.ford.purchasing.wips.domain.layer.WipsPcsHelper;
import com.ford.purchasing.wips.domain.layer.WipsTransactionConstant;
import com.ford.purchasing.wips.domain.priceclaims.beans.PriceClaimsResponse;

public class PriceClaimSummaryInformation extends WipsImsOutput {
    String linkedPcNumber;
    String supplier;
    String buyer;
    String causalFactor;
    String effectiveDate;
    String exchangeRate;
    String volumes;
    String pcsRemark;
    String intPrc;
    String partsIncluded;

    public String getLinkedPcNumber() {
        return linkedPcNumber;
    }

    public void setLinkedPcNumber(final String linkedPcNumber) {
        this.linkedPcNumber = linkedPcNumber;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(final String supplier) {
        this.supplier = supplier;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(final String buyer) {
        this.buyer = buyer;
    }

    public String getCausalFactor() {
        return causalFactor;
    }

    public void setCausalFactor(final String causalFactor) {
        this.causalFactor = causalFactor;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(final String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(final String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getVolumes() {
        return volumes;
    }

    public void setVolumes(final String volumes) {
        this.volumes = volumes;
    }

    public String getPcsRemark() {
        return pcsRemark;
    }

    public void setPcsRemark(final String pcsRemark) {
        this.pcsRemark = pcsRemark;
    }

    public String getIntPrc() {
        return intPrc;
    }

    public void setIntPrc(final String intPrc) {
        this.intPrc = intPrc;
    }

    public String getPartsIncluded() {
        return partsIncluded;
    }

    public void setPartsIncluded(final String partsIncluded) {
        this.partsIncluded = partsIncluded;
    }

    public static PriceClaimSummaryInformation from(final DataArea outputDataArea,
            final String rawOutput) throws ConnectorException, WipsImsTransactionException {
        PriceClaimSummaryInformation summary = new PriceClaimSummaryInformation();
        final String transactionName = TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-PGMNAME");
        if (WipsTransactionConstant.C36U_TRANSACTION_NAME.contains(transactionName)) {
            final WipsPcsHelper helper = new WipsPcsHelper();
            summary =
                    helper.populatePcsSummaryInformation(outputDataArea);
            if (getStringWithUnderScoreTrimmed(
                    outputDataArea,
                    "TP-OUTPUT-BUFFER-FIELDS.TPO-ERRMSG1").contains("MSG-1370"))
                throw new WipsImsTransactionException("C36U", getStringWithUnderScoreTrimmed(
                        outputDataArea,
                        "TP-OUTPUT-BUFFER-FIELDS.TPO-ERRMSG1"));

        }
        summary.setTransactionName(transactionName);
        summary.setErrorMessage(rawOutput.substring(62, 142));
        return summary;
    }

    public BiFunctionThrowsException<DataArea, String, PriceClaimSummaryInformation, Exception> get(
            final PriceClaimsRequest priceClaimsRequest,
            final PriceClaimsResponse priceClaimsResponse) {
        return new BiFunctionThrowsException<DataArea, String, PriceClaimSummaryInformation, Exception>() {

            @Override
            public PriceClaimSummaryInformation apply(
                    final DataArea outputDataArea,
                    final String rawOutput) throws Exception {
                final PriceClaimSummaryInformation summary =
                        PriceClaimSummaryInformation
                                .from(outputDataArea,
                                        rawOutput);
                summary.setSupplier(
                        priceClaimsRequest.getSupplier());
                priceClaimsResponse
                        .setPcsSummaryInformation(summary);
                return summary;
            }

        };
    }
    
    public BiFunctionThrowsException<DataArea, String, PriceClaimSummaryInformation, Exception> retrieveC36uOutput() {
        return new BiFunctionThrowsException<DataArea, String, PriceClaimSummaryInformation, Exception>() {

            @Override
            public PriceClaimSummaryInformation apply(final DataArea outputDataArea, final String rawOutput)
                throws Exception {
                return from(outputDataArea, rawOutput);
            }
        };
    }
}
