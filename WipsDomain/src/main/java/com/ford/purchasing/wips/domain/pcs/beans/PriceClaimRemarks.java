package com.ford.purchasing.wips.domain.pcs.beans;

import static com.ford.purchasing.wips.common.layer.util.TransactionOutputUtil.*;

import java.util.ArrayList;
import java.util.List;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.layer.ApprovalDetail;
import com.ford.purchasing.wips.common.layer.WipsImsOutput;
import com.ford.purchasing.wips.common.layer.util.TransactionOutputUtil;
import com.ford.purchasing.wips.common.layer.util.WipsUtil;

public class PriceClaimRemarks extends WipsImsOutput {

    private ApprovalDetail approvalDetails;
    private Cr1xTransactionInput wipsImsCR51Input;
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

    public ApprovalDetail getApprovalDetails() {
        return this.approvalDetails;
    }

    public void setApprovalDetails(final ApprovalDetail approvalDetails) {
        this.approvalDetails = approvalDetails;
    }

    /**
     * @return Returns the wipsImsCR5xInput.
     */
    public Cr1xTransactionInput getWipsImsCR5xInput() {
        return this.wipsImsCR51Input;
    }

    /**
     * @param wipsImsCR5xInput The wipsImsCR5xInput to set.
     */
    public void setWipsImsCR5xInput(final Cr1xTransactionInput wipsImsCR51Input) {

        this.wipsImsCR51Input = wipsImsCR51Input;

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

    public PriceClaimRemarks from(final DataArea outputDataArea)
            throws ConnectorException {
        final ApprovalDetail detail = new ApprovalDetail();
        final PriceClaimRemarks remarks = new PriceClaimRemarks();
        detail.setMoreRemarksPage((TransactionOutputUtil.getString(outputDataArea,"TP-OUTPUT-BUFFER-FIELDS.TPO-MYMORE")) == null ? "" : TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-MYMORE"));
        detail.setApproverName(TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-BUYTTLE"));
        detail.setDate(WipsUtil.convertDateStringToFormattedDateString(getStringWithUnderScoreTrimmed(
                outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-EFFDATE")));
        detail.setJobCode(TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-SORBUYER"));
        detail.setRemarks(buildRemarks(outputDataArea));
        remarks.setHasMorePages(detail.getMoreRemarksPage());
        remarks.setApprovalDetails(detail);
        return remarks;
    }

    private List<String> buildRemarks(final DataArea outputDataArea)
            throws ConnectorException {
        final List<String> remarks = new ArrayList<String>();
        for (int i = 1; i <= 13; i++) {
            final String remark =
                    TransactionOutputUtil.getStringWithUnderScoreTrimmed(outputDataArea,
                            "TP-OUTPUT-BUFFER-FIELDS.TPO-SORMK" + i);
            if (remark != null && !"".equalsIgnoreCase(remark)) {
                    remarks.add(remark);
                }
        }
        return remarks;

    }

}
