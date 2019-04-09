package com.ford.purchasing.wips.inbound.layer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ford.purchasing.wips.business.layer.WipsAttachmentBF;

import ford.application.attachment.v1.AttachmentType;

/**
 * Servlet implementation class WipsAttachmentServletIC
 */
@WebServlet("/WipsAttachmentServletIC")
public class WipsAttachmentServletIC extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Inject
    private WipsAttachmentBF wipsAttachmentBF;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        final BigInteger attachmentId = new BigInteger(request.getParameter("attachmentId"));
        final AttachmentType attachmentDetails =
                this.wipsAttachmentBF.viewAttachment(attachmentId);
        response.addHeader("Cache-Control",
                "must-revalidate, post-check=0, pre-check=0");
        response.addHeader("Pragma", "public");
        response.addHeader("Content-Disposition",
                "inline;filename=" + attachmentDetails.getAttachmentName());
        response.addHeader("Content-Type", attachmentDetails.getAttachmentMIMEType());

        final InputStream inputStream = attachmentDetails.getFile().getInputStream();
        final OutputStream outputStream = response.getOutputStream();
        final BufferedInputStream bufferedInputStream =
                new BufferedInputStream(inputStream);
        final byte[] byteBuffer = new byte[4 * 1024];
        int bytesRead;
        while ((bytesRead = bufferedInputStream.read(byteBuffer)) != -1) {
            outputStream.write(byteBuffer, 0, bytesRead);
        }
        inputStream.close();
        outputStream.flush();
        outputStream.close();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doPost(final HttpServletRequest request,
            final HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
