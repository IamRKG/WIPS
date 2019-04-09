//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.business.atp;

import java.util.List;

import javax.inject.Inject;

import com.ford.it.logging.ILogger;
import com.ford.it.logging.LogFactory;
import com.ford.it.wscore.exception.WscConsumerSoapException;
import com.ford.purchasing.wips.business.layer.WipsConsumerBaseBF;
import com.ford.purchasing.wips.common.atp.AtpApproveOrRejectResponse;
import com.ford.purchasing.wips.common.atp.AtpRemarksRequest;
import com.ford.purchasing.wips.common.atp.AtpRequest;
import com.ford.purchasing.wips.common.layer.AttachmentDetail;
import com.ford.purchasing.wips.common.layer.PendingApprovalItemsResponse;
import com.ford.purchasing.wips.common.layer.WipsBaseResponse;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.WipsPendingApprovalOutput;
import com.ford.purchasing.wips.domain.atp.AtpRecapResponse;
import com.ford.purchasing.wips.domain.atp.AtpRemarksResponse;
import com.ford.purchasing.wips.domain.atp.service.AtpApproveOrRejectAS;
import com.ford.purchasing.wips.domain.atp.service.AtpRecapAS;
import com.ford.purchasing.wips.domain.atp.service.AtpRemarksAS;
import com.ford.purchasing.wips.domain.atp.service.AtpStrategyAS;
import com.ford.purchasing.wips.domain.layer.PendingApprovalAS;

import ford.application.attachment.v1.AttachIndicator;
import ford.application.attachment.v1.AttachmentSearchCriteriaType;

/**
 * Business Facade class for ATP Approval Module functionality.
 */
public class AtpApprovalBF extends WipsConsumerBaseBF {

    private static final String CLASS_NAME = AtpApprovalBF.class.getName();
    private static final ILogger log = LogFactory.getInstance().getLogger(CLASS_NAME);

    @Inject
    private AtpRecapAS atpRecapAS;

    @Inject
    private AtpStrategyAS atpStrategyAS;

    @Inject
    private AtpApproveOrRejectAS atpApproveOrRejectAS;

    @Inject
    private PendingApprovalAS pendingApprovalAS;

    @Inject
    private AtpRemarksAS atpRemarksAS;

    /**
     * Invokes ATP Recap AS class to get the Recap details from Mainframe system.
     */
    public WipsBaseResponse getRecapDetails(final AtpRequest atpRecapRequest) {
        final String methodName = "getRecapDetails";
        final AtpRecapResponse atpRecapResponse = (AtpRecapResponse)this.atpRecapAS.retrieveRecap(atpRecapRequest);
        if (atpRecapResponse.getG53xTransactionOutput().isAttachmentsExist()) {
            try {
                atpRecapResponse.getG53xTransactionOutput()
                    .setAttachmentDetails(retrieveAttachments(createAtpttachmentSearchCriteria(atpRecapRequest)));
            } catch (final WscConsumerSoapException consumerSoapException) {
                log.throwing(CLASS_NAME, methodName, consumerSoapException);
                final String attachmentsErrorMessage = createErrorMessage(consumerSoapException);
                atpRecapResponse.setAttachmentsErrorFlag(true);
                atpRecapResponse.setAttachmentsErrorMessage(attachmentsErrorMessage);
            }
        }
        return atpRecapResponse;
    }

    private String createErrorMessage(final WscConsumerSoapException consumerSoapException) {
        String attachmentsErrorMessage = null;
        if (WipsConstant.CLIENT_VALIDATION_INVALID_DATA.equals(consumerSoapException.getFaultCode())) {
            attachmentsErrorMessage = WipsConstant.ATP_NUMBER_REQUIRED;
        } else if (WipsConstant.CLIENT_VALIDATION_DOCUMENT_NOT_FOUND.equals(consumerSoapException.getFaultCode())) {
            attachmentsErrorMessage = WipsConstant.DOCUMENT_CURRENTLY_UNAVAILABLE_IN_WEB_QUOTE;
        } else {
            attachmentsErrorMessage = WipsConstant.UNABLE_TO_RETRIEVE_ATTACHMENTS;
        }
        return attachmentsErrorMessage;
    }
    private List<AttachmentDetail> retrieveAttachments(final AttachmentSearchCriteriaType attachmentSearchCriteriaType) {
        return getWipsConsumerAS().searchAttachments(attachmentSearchCriteriaType);
    }

    private AttachmentSearchCriteriaType createAtpttachmentSearchCriteria(
        final AtpRequest atpRequest) {
        final AttachmentSearchCriteriaType attachmentSearchCriteriaType =
            new AttachmentSearchCriteriaType();
        attachmentSearchCriteriaType.setApplicationID(WipsConstant.WIPS_APPLICATION_ID);
        attachmentSearchCriteriaType.setDocumentNumber(atpRequest.getAtpNumber());
        attachmentSearchCriteriaType.setDocumentVersion(WipsConstant.DEFAULT_ATP_VERSION);
        attachmentSearchCriteriaType.setIndicator(AttachIndicator.I);
        return attachmentSearchCriteriaType;
    }

    public WipsBaseResponse getStrategyDetails(final AtpRequest strategyRequest) {
        return this.atpStrategyAS.retrieveStrategy(strategyRequest);
    }

    private List<WipsPendingApprovalOutput> populateAtpList(final AtpRequest request) {
        WipsBaseResponse response = new WipsBaseResponse();
        PendingApprovalItemsResponse pendingApprovalItemsResponse =
                new PendingApprovalItemsResponse();
        response = this.pendingApprovalAS.getPendingAtpsAndLumpsums(request);
        if (!response.isErrorFlag()) {
            pendingApprovalItemsResponse = (PendingApprovalItemsResponse)response;
        }
        return pendingApprovalItemsResponse.getAtp();
    }

    public WipsBaseResponse approveOrRejectAtp(final AtpRequest request) {
        final WipsBaseResponse response =
            this.atpApproveOrRejectAS.approveOrRejectAtp(request);
        if (!response.isErrorFlag()) {
            ((AtpApproveOrRejectResponse)response).setAtp(populateAtpList(request));
        }
        return response;
    }

    public WipsBaseResponse getRemarks(final AtpRequest request) {
        return this.atpRemarksAS.getRemarks(request);
    }

    public AtpRemarksResponse saveRemarks(final AtpRemarksRequest request) {
        return this.atpRemarksAS.saveRemarks(request);
    }

    public AtpApproveOrRejectResponse saveRemarksAndApproveOrRejectAtp(
            final AtpRemarksRequest request) {
        final AtpApproveOrRejectResponse response =
                this.atpRemarksAS.saveRemarksAndApproveOrRejectAtp(request);
        if (!response.isErrorFlag()) {
            response.setAtp(populateAtpList(request));
        }
        return response;
    }
}
