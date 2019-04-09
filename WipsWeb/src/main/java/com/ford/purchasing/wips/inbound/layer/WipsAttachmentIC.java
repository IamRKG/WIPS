//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.inbound.layer;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.ford.purchasing.wips.business.layer.WipsAttachmentBF;

import ford.application.attachment.v1.AttachmentType;

/**
 * This class responsible for producing the multipart form data for the file download request
 * from client.
 */
@RequestScoped
@Path("/Attachment")
public class WipsAttachmentIC extends WipsRestBaseIC {

    @Inject
    private WipsAttachmentBF wipsAttachmentBF;

    @GET
    @Path("/ViewAttachment/{attachmentId}")
    @Produces(MediaType.MULTIPART_FORM_DATA)
    public Response retrieveLumpSumAttachment(
            @PathParam("attachmentId") final BigInteger attachmentId) throws IOException {
        final AttachmentType attachmentDetails =
                this.wipsAttachmentBF.viewAttachment(attachmentId);
        final InputStream inputStream = attachmentDetails.getFile().getInputStream();
        final ResponseBuilder response = Response.ok(inputStream);
        response.header("Cache-Control",
                "must-revalidate, post-checkResponse0, pre-check=0");
        response.header("Pragma", "public");
        response.header("Content-Disposition",
                "attachment;filename=" + attachmentDetails.getAttachmentName());
        response.header("Content-Header",
                "attachment;filename=" + attachmentDetails.getAttachmentName());
        response.header("Content-Type", attachmentDetails.getAttachmentMIMEType());
        return response.build();
    }

}
