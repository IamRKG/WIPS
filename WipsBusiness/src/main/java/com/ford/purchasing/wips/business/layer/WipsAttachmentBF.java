//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.business.layer;

import java.math.BigInteger;

import javax.xml.ws.Holder;

import com.ford.it.logging.ILogger;
import com.ford.it.logging.LogFactory;
import com.ford.it.wscore.exception.WscConsumerSoapException;
import com.ford.purchasing.wips.common.layer.WipsConstant;

import ford.application.attachment.v1.ApplicationAttachmentInfoType;
import ford.application.attachment.v1.AttachmentApplicationType;
import ford.application.attachment.v1.AttachmentType;
import ford.application.attachment.v1.AttachmentsType;

@SuppressWarnings("javadoc")
public class WipsAttachmentBF extends WipsConsumerBaseBF {

    private static final String CLASS_NAME = WipsAttachmentBF.class.getName();
    private static final ILogger log = LogFactory.getInstance().getLogger(CLASS_NAME);

    public AttachmentType viewAttachment(final BigInteger attachmentId) {
        final String methodName = "retrieveAttachment";
        log.entering(CLASS_NAME, methodName);
        final ApplicationAttachmentInfoType attachmentDownloadRequest =
                createDownloadAttachmentRequest(attachmentId);
        final Holder<ApplicationAttachmentInfoType> holder =
                new Holder<ApplicationAttachmentInfoType>();
        holder.value = attachmentDownloadRequest;
        try {
            getWipsConsumerAS().processConsumer("download", holder);
        } catch (final WscConsumerSoapException consumerSoapException) {
            log.throwing(CLASS_NAME, methodName, consumerSoapException);
            throw consumerSoapException;
        }
        log.exiting(CLASS_NAME, methodName);
        return holder.value.getAttachments().getAttachment().get(0);
    }

    private ApplicationAttachmentInfoType createDownloadAttachmentRequest(
            final BigInteger attachmentId) {
        final ApplicationAttachmentInfoType attachmentDownloadRequest =
                new ApplicationAttachmentInfoType();
        attachmentDownloadRequest.setAttachmentApplication(createAttachmentApplicationType());
        attachmentDownloadRequest.setAttachments(createAttachments(attachmentId));
        return attachmentDownloadRequest;
    }

    private AttachmentsType createAttachments(final BigInteger attachmentId) {
        final AttachmentsType attachments = new AttachmentsType();
        attachments.getAttachment().add(createAttachmentType(attachmentId));
        return attachments;
    }

    private AttachmentType createAttachmentType(final BigInteger attachmentId) {
        final AttachmentType attachmentType = new AttachmentType();
        attachmentType.setAttachmentID(attachmentId);
        return attachmentType;
    }

    private AttachmentApplicationType createAttachmentApplicationType() {
        final AttachmentApplicationType attachmentApplicationType =
                new AttachmentApplicationType();
        attachmentApplicationType.setApplicationID(WipsConstant.WIPS_APPLICATION_ID);
        attachmentApplicationType.setEntityName(WipsConstant.WIPS_ENTITY_NAME);
        return attachmentApplicationType;
    }
}
