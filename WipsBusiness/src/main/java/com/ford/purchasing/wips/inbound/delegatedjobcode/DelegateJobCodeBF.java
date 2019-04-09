package com.ford.purchasing.wips.inbound.delegatedjobcode;

import javax.inject.Inject;

import com.ford.it.logging.ILogger;
import com.ford.it.logging.LogFactory;
import com.ford.purchasing.wips.common.layer.PendingApprovalResponse;
import com.ford.purchasing.wips.common.layer.WipsBaseRequest;
import com.ford.purchasing.wips.domain.delegatedjobcode.service.DelegatedJobCodeAS;

@SuppressWarnings("javadoc")
public class DelegateJobCodeBF {

    private static final String CLASS_NAME = DelegateJobCodeBF.class.getName();
    private static ILogger log = LogFactory.getInstance().getLogger(CLASS_NAME);

    @Inject
    DelegatedJobCodeAS delegateJobCodeAS;

    public PendingApprovalResponse viewPendingApprovals(
            final WipsBaseRequest delegatedJobCodeRequest) {
        final String methodName = "viewPendingApprovals";
        log.entering(CLASS_NAME, methodName);
        final PendingApprovalResponse pendingApprovalResponseBean = this.delegateJobCodeAS
                .processDelegateJobCodeRequest(delegatedJobCodeRequest);
        log.exiting(CLASS_NAME, methodName);
        return pendingApprovalResponseBean;
    }

}
