package com.ford.purchasing.wips.inbound.lumpsum;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.ford.it.logging.ILogger;
import com.ford.it.logging.LogFactory;
import com.ford.it.wscore.exception.WscConsumerSoapException;
import com.ford.purchasing.wips.business.layer.WipsConsumerBaseBF;
import com.ford.purchasing.wips.common.layer.AttachmentDetail;
import com.ford.purchasing.wips.common.layer.PendingApprovalItemsResponse;
import com.ford.purchasing.wips.common.layer.WipsBaseResponse;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.WipsPendingApprovalOutput;
import com.ford.purchasing.wips.common.lumpsum.LumpSumApproveOrRejectResponse;
import com.ford.purchasing.wips.common.lumpsum.LumpSumRequest;
import com.ford.purchasing.wips.domain.layer.PendingApprovalAS;
import com.ford.purchasing.wips.domain.lumpsum.beans.LumpSumResponse;
import com.ford.purchasing.wips.domain.lumpsum.service.LumpSumDetailAS;

import ford.application.attachment.v1.AttachmentSearchCriteriaType;

@SuppressWarnings("javadoc")
public class LumpSumDetailBF extends WipsConsumerBaseBF {

    private static final String CLASS_NAME = LumpSumDetailBF.class.getName();
    private static final ILogger log = LogFactory.getInstance().getLogger(CLASS_NAME);

    @Inject
    private LumpSumDetailAS lumpSumDetailAS;

    @Inject
    private PendingApprovalAS pendingApprovalAS;

    public LumpSumResponse getLumpSumDetail(final LumpSumRequest lumpSumRequest) {
        final String methodName = "getLumpSumDetail";
        final LumpSumResponse lumpSumResponse =
                this.lumpSumDetailAS.retrieveLumpSumDetail(lumpSumRequest);
        if (hasAttachments(lumpSumResponse)) {
            try {
                lumpSumResponse.getLumpSumInformation()
                        .setAttachmentDetails(retrieveListOfAttachments(lumpSumResponse));
            } catch (final WscConsumerSoapException consumerSoapException) {
                log.throwing(CLASS_NAME, methodName, consumerSoapException);
                final String attachmentsErrorMessage =
                        createErrorMessage(consumerSoapException);
                lumpSumResponse.setAttachmentsErrorFlag(true);
                lumpSumResponse.setAttachmentsErrorMessage(attachmentsErrorMessage);
            }
        }
        return lumpSumResponse;
    }

    public WipsBaseResponse confirmPaymentDescription(final LumpSumRequest lumpSumRequest) {
        return this.lumpSumDetailAS.confirmPaymentDescription(lumpSumRequest);
    }

    private String createErrorMessage(final WscConsumerSoapException consumerSoapException) {
        String attachmentsErrorMessage = null;
        if (WipsConstant.CLIENT_VALIDATION_INVALID_DATA
                .equals(consumerSoapException.getFaultCode())) {
            attachmentsErrorMessage = WipsConstant.LUMPSUM_NUMBER_AND_VERSION_REQUIRED;
        } else if (WipsConstant.CLIENT_VALIDATION_DOCUMENT_NOT_FOUND
                .equals(consumerSoapException.getFaultCode())) {
            attachmentsErrorMessage =
                    WipsConstant.DOCUMENT_CURRENTLY_UNAVAILABLE_IN_WEB_QUOTE;
        } else {
            attachmentsErrorMessage = WipsConstant.UNABLE_TO_RETRIEVE_ATTACHMENTS;
        }
        return attachmentsErrorMessage;
    }

    private boolean hasAttachments(final LumpSumResponse lumpSumResponse) {
        return WipsConstant.YES
                .equals(lumpSumResponse.getLumpSumInformation().getHasAttachments());
    }

    List<AttachmentDetail> retrieveLumpSumAttachments(
            final AttachmentSearchCriteriaType attachmentSearchCriteriaType) {
        return getWipsConsumerAS().searchAttachments(attachmentSearchCriteriaType);
    }

    private List<AttachmentDetail> retrieveListOfAttachments(
            final LumpSumResponse lumpSumResponse) {
        final AttachmentSearchCriteriaType attachmentSearchCriteriaType =
            createLumpSumAttachmentSearchCriteria(lumpSumResponse);
        return retrieveLumpSumAttachments(attachmentSearchCriteriaType);
    }

    private AttachmentSearchCriteriaType createLumpSumAttachmentSearchCriteria(
            final LumpSumResponse lumpSumResponse) {
        final AttachmentSearchCriteriaType attachmentSearchCriteriaType =
                new AttachmentSearchCriteriaType();
        attachmentSearchCriteriaType.setApplicationID(WipsConstant.WIPS_APPLICATION_ID);
        attachmentSearchCriteriaType.setDocumentNumber(
                lumpSumResponse.getLumpSumInformation().getLumpsumNumber());
        attachmentSearchCriteriaType.setDocumentVersion(
                lumpSumResponse.getLumpSumInformation().getCurrentAmendment());
        return attachmentSearchCriteriaType;
    }

    public LumpSumResponse saveLumpSumDetail(final LumpSumRequest request) {
        return this.lumpSumDetailAS.saveLumpSumDetails(request);
    }

    private List<WipsPendingApprovalOutput> populateLumpsumList(
            final LumpSumRequest request) {
        WipsBaseResponse response = new WipsBaseResponse();
        PendingApprovalItemsResponse pendingApprovalItemsResponse =
                new PendingApprovalItemsResponse();
        response = this.pendingApprovalAS.getPendingAtpsAndLumpsums(request);
        if (!response.isErrorFlag()) {
            pendingApprovalItemsResponse = (PendingApprovalItemsResponse)response;
        }
        return pendingApprovalItemsResponse.getLumpSum();

    }

    public LumpSumApproveOrRejectResponse saveAndApproveOrRejectLumpSumDetails(
            final LumpSumRequest request) {
        final LumpSumApproveOrRejectResponse response = this.lumpSumDetailAS
                .saveAndApproveOrRejectLumpSumDetails(request);
        if (!response.isErrorFlag()) {
            response.setLumpSum(populateLumpsumList(request));
            if (response.getLumpSum() == null)
                response.setLumpSum(new ArrayList<WipsPendingApprovalOutput>());
        }
        return response;
    }
}
