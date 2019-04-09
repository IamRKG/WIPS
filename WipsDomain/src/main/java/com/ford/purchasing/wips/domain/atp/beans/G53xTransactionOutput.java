//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.atp.beans;

import java.util.List;

import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.atp.Approvers;
import com.ford.purchasing.wips.common.atp.AtpSupplierDetail;
import com.ford.purchasing.wips.common.connector.BiFunctionThrowsException;
import com.ford.purchasing.wips.common.layer.AttachmentDetail;
import com.ford.purchasing.wips.common.layer.WipsImsOutput;
import com.ford.purchasing.wips.common.layer.exception.WipsImsTransactionException;
import com.ford.purchasing.wips.common.layer.util.TransactionOutputUtil;
import com.ford.purchasing.wips.domain.layer.WipsTransactionConstant;

@SuppressWarnings("javadoc")
public class G53xTransactionOutput extends WipsImsOutput {

    private List<Approvers> approvers;
    private List<AtpSupplierDetail> suppliers;
    private String autoPo;
    private boolean capacityShortfallFlag;
    private String effectiveDate;
    private String part;
    private String reasonCode;
    private String strategy;
    private String totalCost;
    private String atpNumber;
    private boolean hasMoreSuppliers = false;
    private boolean hasMoreApprovers;
    private String rawEffectiveDate;
    private G53xTransactionInput g53xTransactionInput;
    private boolean attachmentsExist;
    private List<AttachmentDetail> attachmentDetails;

    public List<Approvers> getApprovers() {
        return this.approvers;
    }

    public void setApprovers(final List<Approvers> approvers) {

        this.approvers = approvers;
    }

    public List<AtpSupplierDetail> getSuppliers() {
        return this.suppliers;
    }

    public void setSuppliers(final List<AtpSupplierDetail> suppliers) {
        this.suppliers = suppliers;
    }

    public String getAutoPo() {
        return this.autoPo;
    }

    public void setAutoPo(final String autoPo) {

        this.autoPo = autoPo;
    }

    public boolean isCapacityShortfallFlag() {
        return this.capacityShortfallFlag;
    }

    public void setCapacityShortfallFlag(final boolean capacityShortfallFlag) {

        this.capacityShortfallFlag = capacityShortfallFlag;
    }

    public String getEffectiveDate() {
        return this.effectiveDate;
    }

    public void setEffectiveDate(final String effectiveDate) {

        this.effectiveDate = effectiveDate;
    }

    public String getPart() {
        return this.part;
    }

    public void setPart(final String part) {

        this.part = part;
    }

    public String getReasonCode() {
        return this.reasonCode;
    }

    public void setReasonCode(final String reasonCode) {

        this.reasonCode = reasonCode;
    }

    public String getStrategy() {
        return this.strategy;
    }

    public void setStrategy(final String strategy) {

        this.strategy = strategy;
    }

    public String getTotalCost() {
        return this.totalCost;
    }

    public void setTotalCost(final String totalCost) {

        this.totalCost = totalCost;
    }

    public String getAtpNumber() {
        return this.atpNumber;
    }

    public void setAtpNumber(final String atpNumber) {

        this.atpNumber = atpNumber;
    }

    public boolean isHasMoreSuppliers() {
        return this.hasMoreSuppliers;
    }

    public void setHasMoreSuppliers(final boolean hasMoreSuppliers) {

        this.hasMoreSuppliers = hasMoreSuppliers;
    }

    public boolean isHasMoreApprovers() {
        return this.hasMoreApprovers;
    }

    public void setHasMoreApprovers(final boolean hasMoreApprovers) {

        this.hasMoreApprovers = hasMoreApprovers;
    }

    public String getRawEffectiveDate() {
        return this.rawEffectiveDate;
    }

    public void setRawEffectiveDate(final String rawEffectiveDate) {

        this.rawEffectiveDate = rawEffectiveDate;
    }

    public G53xTransactionInput getG53xTransactionInput() {
        return this.g53xTransactionInput;
    }

    public void setG53xTransactionInput(final G53xTransactionInput g53xTransactionInput) {

        this.g53xTransactionInput = g53xTransactionInput;
    }

    public boolean isAttachmentsExist() {
        return this.attachmentsExist;
    }

    public void setAttachmentsExist(final boolean attachmentsExist) {
        this.attachmentsExist = attachmentsExist;
    }

    public List<AttachmentDetail> getAttachmentDetails() {
        return this.attachmentDetails;
    }

    public void setAttachmentDetails(final List<AttachmentDetail> attachmentDetails) {
        this.attachmentDetails = attachmentDetails;
    }

    public BiFunctionThrowsException<DataArea, String, G53xTransactionOutput, Exception> getG53xOutput(
            final List<AtpSupplierDetail> suppliersCollector, final G53xTransactionOutput g53xOutput) {
        return new BiFunctionThrowsException<DataArea, String, G53xTransactionOutput, Exception>() {

            @Override
            public G53xTransactionOutput apply(final DataArea outputDataArea, final String rawOutput) throws Exception {
                final String transactionName = TransactionOutputUtil.getString(outputDataArea,
                        "TP-OUTPUT-BUFFER-FIELDS.TPO-PROGRAM");
                if (WipsTransactionConstant.G53X_TRANSACTION_NAME.equals(transactionName)) {
                    final AtpRecapHelper atpRecapHelper = new AtpRecapHelper();
                    atpRecapHelper.getAtpRecapDetails(outputDataArea, g53xOutput);
                    g53xOutput.setSuppliers(atpRecapHelper.getAtpRecapSuppliers(outputDataArea));
                    g53xOutput.setG53xTransactionInput(atpRecapHelper.setG53xInput(outputDataArea));
                    g53xOutput.getG53xTransactionInput().setTpiPageNo(
                            TransactionOutputUtil.getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-PAGENO-CHAR"));
                    g53xOutput.setHasMoreSuppliers(
                            (TransactionOutputUtil.getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-MORE")
                                    .contains(WipsTransactionConstant.MORE)) ? true : false);
                    if (TransactionOutputUtil.getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-APPCD" + 6)
                            .contains(WipsTransactionConstant.MORE)) {
                        g53xOutput.setHasMoreApprovers(true);
                    }
                    g53xOutput.setApprovers(atpRecapHelper.getAtpRecapApprovers(outputDataArea));
                } else {
                    throw new WipsImsTransactionException(WipsTransactionConstant.G53X_TRANSACTION_NAME,
                            rawOutput.substring(63, 139));
                }
                suppliersCollector.addAll(g53xOutput.getSuppliers());
                return g53xOutput;
            }

        };
    }

}
