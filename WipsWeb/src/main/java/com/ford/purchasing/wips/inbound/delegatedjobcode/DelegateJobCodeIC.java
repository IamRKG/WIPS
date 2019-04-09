package com.ford.purchasing.wips.inbound.delegatedjobcode;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ford.purchasing.wips.common.layer.JobDetail;
import com.ford.purchasing.wips.common.layer.PendingApprovalResponse;
import com.ford.purchasing.wips.common.layer.UserProfile;
import com.ford.purchasing.wips.common.layer.WipsBaseRequest;
import com.ford.purchasing.wips.common.layer.util.StringUtil;
import com.ford.purchasing.wips.inbound.layer.WipsRestBaseIC;

@SuppressWarnings("javadoc")
@RequestScoped
@Path("/DelegateJobCode")
public class DelegateJobCodeIC extends WipsRestBaseIC {

    @Inject
    private DelegateJobCodeBF delegateJobCodeBF;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrievePendingApprovalsMenu(
            final WipsBaseRequest delegateJobCodeRequest) {
        delegateJobCodeRequest
                .setUserRacfId(getUserRacfIdFromCache(delegateJobCodeRequest.getLterm()));
        final Status status = Response.Status.OK;
        final PendingApprovalResponse pendingApprovalResponse = this.delegateJobCodeBF
                .viewPendingApprovals(delegateJobCodeRequest);
        pendingApprovalResponse
                .setJobDetail(new JobDetail(delegateJobCodeRequest.getJobCode(),
                        delegateJobCodeRequest.getJobName()));
        updateDelegateJobCodeIntoCache(delegateJobCodeRequest);
        return buildResponse(pendingApprovalResponse, status);
    }

    protected void updateDelegateJobCodeIntoCache(
            final WipsBaseRequest delegateJobCodeRequest) {
        final UserProfile userProfile = getUserProfile(delegateJobCodeRequest.getLterm());
        if (!userProfile.getUserJobCode().equals(delegateJobCodeRequest.getJobCode())) {
            userProfile.setSwitchJobCode(delegateJobCodeRequest.getJobCode());
        } else {
            userProfile.setSwitchJobCode(StringUtil.createBlankSpaces(4));
        }
    }

}
