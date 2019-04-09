package com.ford.purchasing.wips.domain.delegatedjobcode.service;

import com.ford.it.logging.ILogger;
import com.ford.it.logging.LogFactory;
import com.ford.purchasing.wips.common.layer.PendingApprovalResponse;
import com.ford.purchasing.wips.common.layer.WipsBaseRequest;
import com.ford.purchasing.wips.common.layer.exception.WipsImsTransactionException;
import com.ford.purchasing.wips.domain.atp.beans.M00mTransactionOutput;
import com.ford.purchasing.wips.domain.layer.WipsBaseAS;
import com.ford.purchasing.wips.domain.layer.exception.WipsImsInterfaceException;

@SuppressWarnings("javadoc")
public class DelegatedJobCodeAS extends WipsBaseAS {

    private static final String CLASS_NAME = "DelegatedJobCodeAS";
    private static ILogger log = LogFactory.getInstance().getLogger(CLASS_NAME);

    public PendingApprovalResponse processDelegateJobCodeRequest(
            final WipsBaseRequest delegateJobCodeRequest) {
        final String METHOD_NAME = "processDelegateJobCodeRequest";
        log.entering(CLASS_NAME, METHOD_NAME);
        PendingApprovalResponse pendingApprovalResponse = new PendingApprovalResponse();
        M00mTransactionOutput m00mTransactionOutput = null;
        try {
            this.imsConversation = startImsConversation();
            executeEppsCallForAuthentication(delegateJobCodeRequest);
            m00mTransactionOutput =
                    executeM00mCallForswitchDelegateJob(delegateJobCodeRequest);
            pendingApprovalResponse = populatePendingApprovalResponse(m00mTransactionOutput);
        } catch (final WipsImsInterfaceException wipsImsInterfaceException) {
            log.throwing(CLASS_NAME, METHOD_NAME, wipsImsInterfaceException);
            pendingApprovalResponse.populateErrorDetails(getErrorMessageWithLogReferenceCode(
                    wipsImsInterfaceException));
        } catch (final WipsImsTransactionException wipsImsTransactionException) {
            log.throwing(CLASS_NAME, METHOD_NAME, wipsImsTransactionException);
            pendingApprovalResponse
                    .populateErrorDetails(wipsImsTransactionException.getErrorMessage());
        } finally {
            endImsConversation();
        }
        log.exiting(CLASS_NAME, METHOD_NAME);
        return pendingApprovalResponse;
    }

    private PendingApprovalResponse populatePendingApprovalResponse(
            final M00mTransactionOutput m00mTransactionOutput) {
        final PendingApprovalResponse pendingApprovalResponse = new PendingApprovalResponse();
        pendingApprovalResponse.setPendingApprovals(m00mTransactionOutput
                .getPendingApprovalList());
        return pendingApprovalResponse;
    }

}
