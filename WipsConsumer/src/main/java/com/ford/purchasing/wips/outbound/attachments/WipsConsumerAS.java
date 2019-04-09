//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.outbound.attachments;

import java.util.ArrayList;
import java.util.List;

import com.ford.it.wscore.consumer.as.WscBaseGenericConsumerAS;
import com.ford.purchasing.wips.common.layer.AttachmentDetail;

import ford.application.attachment.v1.AttachmentSearchCriteriaType;
import ford.application.attachment.v1.AttachmentSearchResult;
import ford.application.attachment.v1.AttachmentSearchResultsType;

@SuppressWarnings("javadoc")
public class WipsConsumerAS extends WscBaseGenericConsumerAS {

    private static final String APPLICATION_ATTACHMENT_SERVICE =
            "ApplicationAttachmentService";
    private static final long serialVersionUID = 1L;

    public WipsConsumerAS() {
        super(APPLICATION_ATTACHMENT_SERVICE);
    }

    public List<AttachmentDetail> searchAttachments(final AttachmentSearchCriteriaType attachmentSearchCriteriaType) {
        final AttachmentSearchResultsType attachmentResults = this.processConsumer("search", attachmentSearchCriteriaType);
        return populateAttachmentDetail(attachmentResults.getAttachmentSearchResults());
    }

    private List<AttachmentDetail> populateAttachmentDetail(final List<AttachmentSearchResult> attachmentSearchResults) {
        final List<AttachmentDetail> attachmentDetails = new ArrayList<AttachmentDetail>();
        AttachmentDetail attachment = null;
        for (final AttachmentSearchResult attachmentSearchResult : attachmentSearchResults) {
            attachment = new AttachmentDetail();
            attachment.setId(attachmentSearchResult.getAttachmentID());
            attachment.setName(attachmentSearchResult.getAttachmentName());
            attachment.setDescription(attachmentSearchResult.getAttachmentDescription());
            attachment.setUploadDate(attachmentSearchResult.getUploadDate());
            attachmentDetails.add(attachment);
        }
        return attachmentDetails;
    }

}
