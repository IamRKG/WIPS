package com.ford.purchasing.wips.domain.lumpsum.beans;

import java.util.List;

import com.ford.purchasing.wips.common.layer.WipsBaseResponse;
import com.ford.purchasing.wips.common.lumpsum.C32uTransactionOutput;

@SuppressWarnings("javadoc")
public class LumpSumResponse extends WipsBaseResponse {

    private C32uTransactionOutput lumpSumInformation = new C32uTransactionOutput();
    private C33uTransactionOutput additionalLumpSumInformation = new C33uTransactionOutput();
    private CR5xTransactionOutput lumpSumRemarks = new CR5xTransactionOutput();
    private List<String> availableAmendments;
    private boolean lumpSumReadOnlyFlag;
    private boolean attachmentsErrorFlag;
    private String attachmentsErrorMessage;
    private boolean lumpSumDetailSavedFlag;
    private LumpSumSecurity lumpSumSecurity;

    public boolean isLumpSumDetailSavedFlag() {
        return this.lumpSumDetailSavedFlag;
    }

    public void setLumpSumDetailSavedFlag(final boolean lumpSumDetailSavedFlag) {

        this.lumpSumDetailSavedFlag = lumpSumDetailSavedFlag;
    }

    public boolean isAttachmentsErrorFlag() {
        return this.attachmentsErrorFlag;
    }

    public void setAttachmentsErrorFlag(final boolean attachmentsErrorFlag) {
        this.attachmentsErrorFlag = attachmentsErrorFlag;
    }

    public String getAttachmentsErrorMessage() {
        return this.attachmentsErrorMessage;
    }

    public void setAttachmentsErrorMessage(final String attachmentsErrorMessage) {
        this.attachmentsErrorMessage = attachmentsErrorMessage;
    }

    public C32uTransactionOutput getLumpSumInformation() {
        return this.lumpSumInformation;
    }

    public void setLumpSumInformation(final C32uTransactionOutput lumpSumInformation) {
        this.lumpSumInformation = lumpSumInformation;
    }

    public C33uTransactionOutput getAdditionalLumpSumInformation() {
        return this.additionalLumpSumInformation;
    }

    public void setAdditionalLumpSumInformation(
            final C33uTransactionOutput additionalLumpSumInformation) {
        this.additionalLumpSumInformation = additionalLumpSumInformation;
    }

    public CR5xTransactionOutput getLumpSumRemarks() {
        return this.lumpSumRemarks;
    }

    public void setLumpSumRemarks(final CR5xTransactionOutput lumpSumRemarks) {
        this.lumpSumRemarks = lumpSumRemarks;
    }

    public List<String> getAvailableAmendments() {
        return this.availableAmendments;
    }

    public void setAvailableAmendments(final List<String> availableAmendments) {
        this.availableAmendments = availableAmendments;
    }

    public boolean isLumpSumReadOnlyFlag() {
        return this.lumpSumReadOnlyFlag;
    }

    public void setLumpSumReadOnlyFlag(final boolean lumpSumReadOnlyFlag) {
        this.lumpSumReadOnlyFlag = lumpSumReadOnlyFlag;
    }

    public void setSecurityInformation(final LumpSumSecurity lumpSumSecurity) {
        this.lumpSumSecurity = lumpSumSecurity;
    }

    public LumpSumSecurity getSecurityInformation() {
        return this.lumpSumSecurity;
    }
}
