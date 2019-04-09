package com.ford.purchasing.wips.domain.pcs.beans;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.exception.WipsImsTransactionException;
import com.ford.purchasing.wips.common.layer.util.TransactionOutputUtil;
import com.ford.purchasing.wips.common.priceclaims.BusinessUnitDetail;
import com.ford.purchasing.wips.common.priceclaims.PCSBusinessUnit;
import com.ford.purchasing.wips.common.priceclaims.PCSFinancialInfoDetail;
import com.ford.purchasing.wips.common.priceclaims.PurchasingManagerDetail;
import com.ford.purchasing.wips.domain.layer.WipsTransactionConstant;

public class FinancialImpactInformation {

    private List<String> claimYear;
    private List<String> purchaseManager;
    private List<PCSBusinessUnit> businessUnits;
    private List<PurchasingManagerDetail> purchasingManagerDetails;
    private PCSFinancialInfoDetail businessUnitTotalDetails;
    private String hasMorePages;
    private String selectedBU;
    private String selectedPM;
    private String bufferSegmentLoop = "TP-OUTPUT-BUFFER-FIELDS.TPO-BUFFER-SEGLOOP";
    private String detailCode = "TPO-DETLLINE.TPO-DETAIL-CODE";

    public FinancialImpactInformation from(final DataArea outputDataArea, final String rawOutput,
        final String selectedPM, final List<String> purchaseManagers, final List<PCSBusinessUnit> pcsBusinessUnits,
        final List<PurchasingManagerDetail> purchasingManagerDetailList)
        throws ConnectorException, WipsImsTransactionException {

        if (WipsTransactionConstant.C38V_TRANSACTION_NAME.contains(TransactionOutputUtil.getString(outputDataArea,
            "TP-OUTPUT-BUFFER-FIELDS.TPO-PGMNAME"))) {
            return populateFinancialImpactInformation(outputDataArea, selectedPM, purchaseManagers, pcsBusinessUnits,
                purchasingManagerDetailList);
        } else {
            throw new WipsImsTransactionException("", rawOutput.substring(62, 141));
        }
    }

    private FinancialImpactInformation populateFinancialImpactInformation(final DataArea outputDataArea,
        final String selectedPM,
        final List<String> purchaseManagers, final List<PCSBusinessUnit> pcsBusinessUnits,
        final List<PurchasingManagerDetail> purchasingManagerDetailList) throws ConnectorException {
        final FinancialImpactInformation financialImpactInformation = new FinancialImpactInformation();
        financialImpactInformation.setClaimYear(getClaimYear(outputDataArea));
        financialImpactInformation.setPurchasingManagerDetails(purchasingManagerDetailList);
        addPurchaseManagerName(outputDataArea, selectedPM, purchaseManagers);
        financialImpactInformation.setPurchaseManager(purchaseManagers);
        addPMandBUDetail(outputDataArea, selectedPM, pcsBusinessUnits, purchasingManagerDetailList,
            financialImpactInformation);
        financialImpactInformation.setBusinessUnitTotalDetails(getPcsFinancialInfoTotalDetail(outputDataArea, true));
        financialImpactInformation.setHasMorePages(TransactionOutputUtil.getString(outputDataArea,
            "TP-OUTPUT-BUFFER-FIELDS.TPO-MORE"));
        financialImpactInformation
            .setSelectedBU(TransactionOutputUtil.getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-SELBU"));
        financialImpactInformation.setSelectedPM(selectedPM);
        return financialImpactInformation;
    }

    private void addPMandBUDetail(final DataArea outputDataArea, final String selectedPM,
        final List<PCSBusinessUnit> pcsBusinessUnits, final List<PurchasingManagerDetail> purchasingManagerDetailList,
        final FinancialImpactInformation financialImpactInformation) throws ConnectorException {
        if (StringUtils.isNotEmpty(selectedPM) && checkEqualsStarAll(selectedPM)) {
            addPurchaseMangerDetail(outputDataArea, purchasingManagerDetailList);
            financialImpactInformation.setPurchasingManagerDetails(purchasingManagerDetailList);
        } else {
            addPcsBusinessUnit(outputDataArea, pcsBusinessUnits);
            financialImpactInformation.setBusinessUnits(pcsBusinessUnits);
        }
    }

    private void addPurchaseMangerDetail(final DataArea outputDataArea,
        final List<PurchasingManagerDetail> purchasingManagerDetailList) throws ConnectorException {
        final List<DataArea> outputDetailsList = TransactionOutputUtil.getArray(outputDataArea,
            this.bufferSegmentLoop);
        for (final DataArea dataArea : outputDetailsList) {
            final PurchasingManagerDetail purchasingManagerDetail = getPurchasingManagerDetails(dataArea);
            if (StringUtils.isNotBlank(purchasingManagerDetail.getPmCode())) {
                purchasingManagerDetailList.add(purchasingManagerDetail);
            }
        }
    }

    private void addPcsBusinessUnit(final DataArea outputDataArea, final List<PCSBusinessUnit> pcsBusinessUnits)
        throws ConnectorException {
        final PCSBusinessUnit pcsBusinessUnit = new PCSBusinessUnit();
        final String businessUnit =
            TransactionOutputUtil.getStringWithUnderScoreTrimmed(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-SELBU");
        if (StringUtils.isNotEmpty(businessUnit) && !checkEqualsStarAll(businessUnit)) {
            pcsBusinessUnit.setBusinessUnit(businessUnit);
            final List<BusinessUnitDetail> businessUnitDetailsList = new ArrayList<BusinessUnitDetail>();
            final List<DataArea> outputDetailsList = TransactionOutputUtil.getArray(outputDataArea, this.bufferSegmentLoop);
            for (final DataArea dataArea : outputDetailsList) {
                final BusinessUnitDetail businessUnitDetail = getPcsBusinessUnitDetails(dataArea);
                if (StringUtils.isNotBlank(businessUnitDetail.getCommodityCode())) {
                    businessUnitDetailsList.add(businessUnitDetail);
                }
            }
            fetchExistingOrAddNewBU(outputDataArea, pcsBusinessUnits, pcsBusinessUnit, businessUnitDetailsList);
        }
    }

    private void fetchExistingOrAddNewBU(final DataArea outputDataArea, final List<PCSBusinessUnit> pcsBusinessUnits,
        final PCSBusinessUnit pcsBusinessUnit, final List<BusinessUnitDetail> businessUnitDetailsList)
        throws ConnectorException {
        if (pcsBusinessUnits.contains(pcsBusinessUnit)) {
            pcsBusinessUnits.get(pcsBusinessUnits.indexOf(pcsBusinessUnit))
                .getPcsBusinessUnitDetails()
                .addAll(businessUnitDetailsList);
        } else {
            pcsBusinessUnit.setPcsBusinessUnitDetails(businessUnitDetailsList);
            pcsBusinessUnit.setBusinessUnitTotalDetail(getPcsFinancialInfoTotalDetail(outputDataArea, true));
            pcsBusinessUnits.add(pcsBusinessUnit);
        }
    }

    private List<String> getClaimYear(final DataArea outputDataArea) throws ConnectorException {
        final List<String> claimYearList = new ArrayList<String>();
        final String claimCurrentYear = TransactionOutputUtil.getStringWithUnderScoreTrimmed(outputDataArea,
            "TP-OUTPUT-BUFFER-FIELDS.TPO-YEAR1");
        final String claimNextYear = TransactionOutputUtil.getStringWithUnderScoreTrimmed(outputDataArea,
            "TP-OUTPUT-BUFFER-FIELDS.TPO-YEAR2");
        claimYearList.add(WipsConstant.ALL);
        addClaimYear(claimYearList, claimCurrentYear);
        addClaimYear(claimYearList, claimNextYear);
        return claimYearList;
    }

    private void addClaimYear(final List<String> claimYearList, final String claimYear) {
        if (StringUtils.isNotBlank(claimYear)) {
            claimYearList.add(claimYear);
        }
    }

    private void addPurchaseManagerName(final DataArea outputDataArea, final String selectedPM,
        final List<String> purchaseManagers) throws ConnectorException {
        final List<DataArea> outputDetailList = TransactionOutputUtil.getArray(outputDataArea,
            this.bufferSegmentLoop);
        String purchaseManagerName = null;
        for (final DataArea dataArea : outputDetailList) {
            purchaseManagerName = TransactionOutputUtil.getString(dataArea, this.detailCode);
            if (StringUtils.isNotBlank(purchaseManagerName) && checkEqualsStarAll(selectedPM)) {
                purchaseManagerName =
                    purchaseManagerName + " - " + TransactionOutputUtil.getString(dataArea, "TPO-DETLLINE.TPO-DETAIL-NAME");
                purchaseManagers.add(purchaseManagerName);
            }
        }
    }

    private BusinessUnitDetail getPcsBusinessUnitDetails(final DataArea dataArea)
        throws ConnectorException {
        final BusinessUnitDetail pCSBusinessUnitDetails = new BusinessUnitDetail();
        pCSBusinessUnitDetails.setCommodityCode(TransactionOutputUtil.getString(dataArea, this.detailCode));
        pCSBusinessUnitDetails.setPcsFinancialInfoDetail(getPcsFinancialInfoTotalDetail(dataArea, false));
        return pCSBusinessUnitDetails;
    }

    private PCSFinancialInfoDetail getPcsFinancialInfoTotalDetail(final DataArea outputDataArea,
        final boolean isTotalInfo) throws ConnectorException {
        final PCSFinancialInfoDetail pcsFinancialInfoTotalDetail = new PCSFinancialInfoDetail();
        pcsFinancialInfoTotalDetail.setClaimTOPercentage(TransactionOutputUtil.getStringWithUnderScoreTrimmed(outputDataArea,
            isTotalInfo ? "TP-OUTPUT-BUFFER-FIELDS.TPO-TOTLINE.TPO-TOTAL-CLM-PCENT"
                : "TPO-DETLLINE.TPO-DETAIL-CLM-PCENT"));
        pcsFinancialInfoTotalDetail.setFrznTO(TransactionOutputUtil.getStringWithUnderScoreTrimmed(outputDataArea,
            isTotalInfo ? "TP-OUTPUT-BUFFER-FIELDS.TPO-TOTLINE.TPO-TOTAL-FROZ-TURN"
                : "TPO-DETLLINE.TPO-DETAIL-FROZ-TURN"));
        pcsFinancialInfoTotalDetail.setNDPCSave((TransactionOutputUtil.getStringWithUnderScoreTrimmed(outputDataArea,
            isTotalInfo ? "TP-OUTPUT-BUFFER-FIELDS.TPO-TOTLINE.TPO-TOTAL-NDPCII-SAVE"
                : "TPO-DETLLINE.TPO-DETAIL-NDPCII-SAVE"))
            + (TransactionOutputUtil.getStringWithUnderScoreTrimmed(outputDataArea,
                isTotalInfo ? "TP-OUTPUT-BUFFER-FIELDS.TPO-TOTLINE.TPO-TOTAL-NDPCII-SIGN1"
                    : "TPO-DETLLINE.TPO-DETAIL-NDPCII-SIGN1")));
        pcsFinancialInfoTotalDetail
            .setNDPCSavePercentage((TransactionOutputUtil.getStringWithUnderScoreTrimmed(outputDataArea,
                isTotalInfo ? "TP-OUTPUT-BUFFER-FIELDS.TPO-TOTLINE.TPO-TOTAL-NDPCII-PCENT"
                    : "TPO-DETLLINE.TPO-DETAIL-NDPCII-PCENT"))
                + (TransactionOutputUtil.getStringWithUnderScoreTrimmed(outputDataArea,
                    isTotalInfo ? "TP-OUTPUT-BUFFER-FIELDS.TPO-TOTLINE.TPO-TOTAL-NDPCII-SIGN2"
                        : "TPO-DETLLINE.TPO-DETAIL-NDPCII-SIGN2")));
        return pcsFinancialInfoTotalDetail;
    }

    private PurchasingManagerDetail getPurchasingManagerDetails(final DataArea dataArea)
        throws ConnectorException {
        final PurchasingManagerDetail purchasingManagerDetail = new PurchasingManagerDetail();
        purchasingManagerDetail.setPmCode(TransactionOutputUtil.getString(dataArea, this.detailCode));
        purchasingManagerDetail.setPmName(TransactionOutputUtil.getString(dataArea, "TPO-DETLLINE.TPO-DETAIL-NAME"));
        purchasingManagerDetail.setPcsFinancialInfoDetail(getPcsFinancialInfoTotalDetail(dataArea, false));
        return purchasingManagerDetail;
    }

    private boolean checkEqualsStarAll(final String value) {
        return WipsConstant.STAR_ALL.equalsIgnoreCase(value);
    }

    public List<String> getClaimYear() {
        return this.claimYear;
    }

    public void setClaimYear(final List<String> claimYear) {

        this.claimYear = claimYear;

    }

    public List<String> getPurchaseManager() {
        return this.purchaseManager;
    }

    public void setPurchaseManager(final List<String> purchaseManager) {

        this.purchaseManager = purchaseManager;

    }

    public List<PCSBusinessUnit> getBusinessUnits() {
        return this.businessUnits;
    }

    public void setBusinessUnits(final List<PCSBusinessUnit> businessUnits) {

        this.businessUnits = businessUnits;

    }

    public List<PurchasingManagerDetail> getPurchasingManagerDetails() {
        return this.purchasingManagerDetails;
    }

    public void setPurchasingManagerDetails(final List<PurchasingManagerDetail> purchasingManagerDetails) {

        this.purchasingManagerDetails = purchasingManagerDetails;

    }

    public PCSFinancialInfoDetail getBusinessUnitTotalDetails() {
        return this.businessUnitTotalDetails;
    }

    public void setBusinessUnitTotalDetails(final PCSFinancialInfoDetail businessUnitTotalDetails) {

        this.businessUnitTotalDetails = businessUnitTotalDetails;

    }

    public String getHasMorePages() {
        return this.hasMorePages;
    }

    public void setHasMorePages(final String hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public String getSelectedBU() {
        return this.selectedBU;
    }

    public void setSelectedBU(final String selectedBU) {
        this.selectedBU = selectedBU;
    }

    public String getSelectedPM() {
        return this.selectedPM;
    }

    public void setSelectedPM(final String selectedPM) {

        this.selectedPM = selectedPM;

    }

}
