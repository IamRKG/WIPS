package com.ford.purchasing.wips.domain.layer;

import java.text.MessageFormat;

import javax.inject.Inject;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.ims.ImsConversation;
import com.ford.it.exception.FordSelfLoggingException;
import com.ford.it.logging.ILogger;
import com.ford.it.logging.Level;
import com.ford.it.logging.LogFactory;
import com.ford.purchasing.wips.common.atp.EppsTransactionInput;
import com.ford.purchasing.wips.common.layer.WipsBaseRequest;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.WipsImsInput;
import com.ford.purchasing.wips.common.layer.exception.WipsImsTransactionException;
import com.ford.purchasing.wips.domain.atp.beans.M00mTransactionOutput;
import com.ford.purchasing.wips.domain.atp.connector.M00mTransaction;
import com.ford.purchasing.wips.domain.connector.EppsTransaction;
import com.ford.purchasing.wips.domain.layer.exception.WipsImsInterfaceException;

/**
 * This Service is used as wrapper for Application services
 */
public abstract class WipsBaseAS {

    private static final String CLASS_NAME = WipsBaseAS.class.getName();
    private static ILogger log = LogFactory.getInstance().getLogger(CLASS_NAME);
    protected ImsConversation imsConversation;

    @Inject
    protected EppsTransaction eppsTransaction;

    @Inject
    private M00mTransaction m00mTransaction;

    public static final String WIPS_SYSTEM_ERROR = "Unable to process your request currently, Please contact support team with this reference code [{0}].";

    /**
     * This method is meant to be used within AS only.
     */
    public ImsConversation startImsConversation() {
        return new ImsConversation();
    }

    /**
     * This method ends the IMS conversion if present.
     */
    public void endImsConversation() {
        final String methodName = "processUserLoginRequest";
        if (this.imsConversation != null) {
            try {
                this.imsConversation.endConversation();
            } catch (final ConnectorException connectorException) {
                if ((connectorException.getMessage()).indexOf(WipsConstant.END_CONVERSATION_ERROR) != -1) {
                    log.log(Level.WARNING, " End Conversation Error");
                }
                log.throwing(CLASS_NAME, methodName, connectorException);
            }
        }

    }

    protected M00mTransactionOutput executeEppsCallForAuthentication(final WipsBaseRequest wipsBaseRequest)
            throws WipsImsInterfaceException, WipsImsTransactionException {
        return (M00mTransactionOutput) this.eppsTransaction
                .executeWipsImsTransaction(populateEppsInput(wipsBaseRequest), this.imsConversation);
    }

    protected M00mTransactionOutput executeM00mCallForswitchDelegateJob(final WipsBaseRequest delegateJobCodeRequest)
            throws WipsImsInterfaceException, WipsImsTransactionException {
        return (M00mTransactionOutput) this.m00mTransaction
                .executeWipsImsTransaction(populateM00mInput(delegateJobCodeRequest), this.imsConversation);
    }

    protected WipsImsInput populateM00mInput(final WipsBaseRequest wipsBaseRequest) {
        final WipsImsInput m00mInput = new WipsImsInput();
        m00mInput.setTpiIoindic(WipsConstant.InputI);
        m00mInput.setTpiPfKey(WipsConstant.PFKEY0);
        m00mInput.setUserRacfId(wipsBaseRequest.getUserRacfId());
        m00mInput.setTpiOption(WipsConstant.BLANK_SPACE_3);
        m00mInput.setJobCode(wipsBaseRequest.getJobCode());
        m00mInput.setTpiPageNo(WipsConstant.PAGENUMBERONE);
        m00mInput.setLterm(wipsBaseRequest.getLterm());
        return m00mInput;
    }

    /**
     * Populates the WipsImsEppsInput object to pass to the connector class to
     * make the EPPS transaction.
     */
    private EppsTransactionInput populateEppsInput(final WipsBaseRequest wipsBaseRequest) {
        final EppsTransactionInput wipsEppsInput = new EppsTransactionInput();
        wipsEppsInput.setTpiIoindic(WipsConstant.InputO);
        wipsEppsInput.setTpiPfKey(WipsConstant.PFKEY0);
        wipsEppsInput.setUserRacfId(wipsBaseRequest.getUserRacfId());
        wipsEppsInput.setImsAction(wipsBaseRequest.getActionTaken());
        wipsEppsInput.setLterm(wipsBaseRequest.getLterm());
        return wipsEppsInput;
    }

    protected String getErrorMessageWithLogReferenceCode(final FordSelfLoggingException fordSelfLoggingException) {
        final String logReferenceCode = fordSelfLoggingException.getLogReferenceCode();
        final Object[] parameters = new Object[] {
                logReferenceCode.substring(logReferenceCode.lastIndexOf(WipsConstant.STRING_HYPEN) + 1) };
        return MessageFormat.format(WIPS_SYSTEM_ERROR, parameters);
    }

}
