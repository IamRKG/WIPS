//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.sms.service;

import javax.inject.Inject;

import com.ford.it.logging.ILogger;
import com.ford.it.logging.LogFactory;
import com.ford.purchasing.wips.common.layer.TextMessageOptinRequest;
import com.ford.purchasing.wips.common.layer.TextMessageOptinResponse;
import com.ford.purchasing.wips.common.layer.WipsBaseResponse;
import com.ford.purchasing.wips.domain.dao.db2.TextMessageOptinDAO;
import com.ford.purchasing.wips.domain.layer.CDSLookup;
import com.ford.purchasing.wips.domain.layer.WipsBaseAS;
import com.ford.purchasing.wips.domain.layer.exception.WipsDb2DatabaseException;

@SuppressWarnings("javadoc")
public class TextMessageOptinAS extends WipsBaseAS {
    private static final String CLASS_NAME = TextMessageOptinAS.class.getName();
    private static ILogger log = LogFactory.getInstance().getLogger(CLASS_NAME);

    @Inject
    private TextMessageOptinDAO smsDAO;
    @Inject
    private CDSLookup cdsLookup;

    public TextMessageOptinResponse returnAuxiliaryPhoneAndCDS(final String racfId) {
        final String methodName = "returnAuxiliaryPhoneAndCDS";
        TextMessageOptinResponse retrieveCdsIdAndOptinOptout = new TextMessageOptinResponse();
        try {
            retrieveCdsIdAndOptinOptout =
                    this.smsDAO.retrieveCdsIdAndOptinOptout(racfId);
            retrieveCdsIdAndOptinOptout.setAuxiliaryPhone(cdsLookup
                    .getAttributesFromLdapByCdsid(retrieveCdsIdAndOptinOptout.getCdsId()));
        } catch (final WipsDb2DatabaseException wipsDb2DatabaseException) {
            log.throwing(CLASS_NAME, methodName, wipsDb2DatabaseException);
            retrieveCdsIdAndOptinOptout
                    .populateErrorDetails(getErrorMessageWithLogReferenceCode(
                            wipsDb2DatabaseException));
        } catch (final Exception exception) {
            log.throwing(CLASS_NAME, methodName, exception);
            retrieveCdsIdAndOptinOptout
                    .populateErrorDetails(exception.getMessage());
        }
        return retrieveCdsIdAndOptinOptout;
    }

    public WipsBaseResponse updateOptedValue(final TextMessageOptinRequest request,
            final String racfId) {
        final String methodName = "updateOptedValue";
        final WipsBaseResponse response = new WipsBaseResponse();
        try {
            this.smsDAO.updateOptinOptout(request.getSelectedOption(), racfId);
        } catch (final WipsDb2DatabaseException wipsDb2DatabaseException) {
            log.throwing(CLASS_NAME, methodName, wipsDb2DatabaseException);
            response
                    .populateErrorDetails(getErrorMessageWithLogReferenceCode(
                            wipsDb2DatabaseException));
        }
        return response;

    }
}
