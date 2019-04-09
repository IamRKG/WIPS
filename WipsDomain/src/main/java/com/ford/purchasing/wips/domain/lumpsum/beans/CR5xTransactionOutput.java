package com.ford.purchasing.wips.domain.lumpsum.beans;

import java.util.ArrayList;
import java.util.List;

import com.ford.purchasing.wips.common.layer.ApprovalDetail;
import com.ford.purchasing.wips.common.layer.WipsImsOutput;
import com.ford.purchasing.wips.common.lumpsum.WipsImsCR5xInput;

@SuppressWarnings("javadoc")
public class CR5xTransactionOutput extends WipsImsOutput {

    private List<ApprovalDetail> approvalDetails = new ArrayList<ApprovalDetail>();
    private WipsImsCR5xInput wipsImsCR5xInput;
    private String hasMorePages;
    private boolean checkMorePagesFlag;
    private String enterUser = null;
    boolean remarksSuccessfullySaved = false;

    public boolean isRemarksSuccessfullySaved() {
        return this.remarksSuccessfullySaved;
    }

    public void setRemarksSuccessfullySaved(final boolean remarksSuccessfullySaved) {

        this.remarksSuccessfullySaved = remarksSuccessfullySaved;
    }

    public String getEnterUser() {
        return this.enterUser;
    }

    public void setEnterUser(final String enterUser) {

        this.enterUser = enterUser;
    }

    public List<ApprovalDetail> getApprovalDetails() {
        return this.approvalDetails;
    }

    public void setApprovalDetails(final List<ApprovalDetail> approvalDetails) {
        this.approvalDetails = approvalDetails;
    }

    /**
     * @return Returns the wipsImsCR5xInput.
     */
    public WipsImsCR5xInput getWipsImsCR5xInput() {
        return this.wipsImsCR5xInput;
    }

    /**
     * @param wipsImsCR5xInput The wipsImsCR5xInput to set.
     */
    public void setWipsImsCR5xInput(final WipsImsCR5xInput wipsImsCR5xInput) {

        this.wipsImsCR5xInput = wipsImsCR5xInput;

    }

    /**
     * @return Returns the hasMorePages.
     */
    public String getHasMorePages() {
        return this.hasMorePages;
    }

    /**
     * @param hasMorePages The hasMorePages to set.
     */
    public void setHasMorePages(final String hasMorePages) {

        this.hasMorePages = hasMorePages;

    }

    /**
     * @return Returns the checkMorePagesFlag.
     */
    public boolean isCheckMorePagesFlag() {
        return this.checkMorePagesFlag;
    }

    /**
     * @param checkMorePagesFlag The checkMorePagesFlag to set.
     */
    public void setCheckMorePagesFlag(final boolean checkMorePagesFlag) {

        this.checkMorePagesFlag = checkMorePagesFlag;

    }

}
