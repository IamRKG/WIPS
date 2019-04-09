package com.ford.purchasing.wips.domain.atp.beans;

import java.util.ArrayList;
import java.util.List;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.connector.BiFunctionThrowsException;
import com.ford.purchasing.wips.common.layer.Category;
import com.ford.purchasing.wips.common.layer.PendingApprovalRequest;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.WipsImsOutput;
import com.ford.purchasing.wips.common.layer.WipsPendingApprovalOutput;
import com.ford.purchasing.wips.common.layer.exception.WipsImsTransactionException;
import com.ford.purchasing.wips.common.layer.util.TransactionOutputUtil;
import com.ford.purchasing.wips.domain.layer.PendingApprItemsHelper;
import com.ford.purchasing.wips.domain.layer.WipsTransactionConstant;

@SuppressWarnings("javadoc")
public class Q01xTransactionOutput extends WipsImsOutput {

    private String output;
    private String userJobCode;
    private String userJobName;
    private String containMorePages;
    private String pageNo;
    private List<WipsPendingApprovalOutput> pendingApprItemsList =
            new ArrayList<WipsPendingApprovalOutput>();

    String tpiCat;

    public String getTpiCat() {
        return this.tpiCat;
    }

    public void setTpiCat(final String tpiCat) {

        this.tpiCat = tpiCat;
    }

    public String getUserJobCode() {
        return this.userJobCode;
    }

    public void setUserJobCode(final String userJobCode) {
        this.userJobCode = userJobCode;
    }

    public String getUserJobName() {
        return this.userJobName;
    }

    public void setUserJobName(final String userJobName) {
        this.userJobName = userJobName;
    }

    public String getContainMorePages() {
        return this.containMorePages;
    }

    public void setContainMorePages(final String containMorePages) {
        this.containMorePages = containMorePages;
    }

    public String getOutput() {
        return this.output;
    }

    public void setOutput(final String output) {
        this.output = output;
    }

    public String getPageNo() {
        return this.pageNo;
    }

    public void setPageNo(final String pageNo) {
        this.pageNo = pageNo;
    }

    public List<WipsPendingApprovalOutput> getPendingApprItemsList() {
        return this.pendingApprItemsList;
    }

    public void setPendingApprItemsList(
            final List<WipsPendingApprovalOutput> pendingApprItemsList) {
        this.pendingApprItemsList = pendingApprItemsList;
    }

    public BiFunctionThrowsException<DataArea, String, Q01xTransactionOutput, Exception> getQ01xOutput(
            final PendingApprovalRequest pendingApprovalRequest) {
        return new BiFunctionThrowsException<DataArea, String, Q01xTransactionOutput, Exception>() {

            @Override
            public Q01xTransactionOutput apply(
                    final DataArea outputDataArea,
                    final String rawOutput) throws Exception {
                Q01xTransactionOutput q01xOutput = new Q01xTransactionOutput();
                q01xOutput = from(outputDataArea, rawOutput);
                return q01xOutput;
            }

        };
    }

    public Q01xTransactionOutput from(final DataArea outputDataArea, final String rawOutput)
            throws ConnectorException, WipsImsTransactionException {
        Q01xTransactionOutput q01xTransactionOutputBean = null;
        final String transactionName = TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-PGMNAME");
        if (WipsTransactionConstant.Q01X_TRANSACTION_NAME.equals(transactionName)) {
            if (!rawOutput.substring(62, 113).contains("MSG-3204")) {
                q01xTransactionOutputBean = new Q01xTransactionOutput();
                q01xTransactionOutputBean
                        .setUserJobCode(TransactionOutputUtil.getString(outputDataArea,
                                "TP-OUTPUT-BUFFER-FIELDS.TPO-JOBCODE"));
                q01xTransactionOutputBean
                        .setUserJobName(TransactionOutputUtil.getString(outputDataArea,
                                "TP-OUTPUT-BUFFER-FIELDS.TPO-EMPNAME"));
                q01xTransactionOutputBean.setContainMorePages(TransactionOutputUtil.getString(
                        outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-MORE"));
                q01xTransactionOutputBean
                        .setPageNo(TransactionOutputUtil.getString(outputDataArea,
                                "TP-OUTPUT-BUFFER-FIELDS.TPO-PAGENO"));
                q01xTransactionOutputBean.setTpiCat(TransactionOutputUtil.getString(
                        outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-SELCAT"));
                q01xTransactionOutputBean.setPendingApprItemsList(
                        new PendingApprItemsHelper().populatePendingApprItems(outputDataArea,
                                q01xTransactionOutputBean.getTpiCat()
                                        .equals(WipsConstant.SELCAT) ? Category.ATP
                                                                     : Category.LUMPSUM));
                q01xTransactionOutputBean.setErrorFlag(false);
            } else {
                throw new WipsImsTransactionException(transactionName,
                        rawOutput.substring(62, 113));
            }
        } else {
            throw new WipsImsTransactionException("",
                    rawOutput.substring(108, 184));
        }
        return q01xTransactionOutputBean;
    }

    public BiFunctionThrowsException<DataArea, String, Q01xTransactionOutput, Exception> getQ01xOutput(
            final List<WipsPendingApprovalOutput> priceClaims) {
        return new BiFunctionThrowsException<DataArea, String, Q01xTransactionOutput, Exception>() {

            @Override
            public Q01xTransactionOutput apply(
                    final DataArea outputDataArea,
                    final String rawOutput) throws Exception {
                Q01xTransactionOutput q01xOutput = new Q01xTransactionOutput();
                q01xOutput = from(outputDataArea, rawOutput);
                priceClaims.addAll(q01xOutput.getPendingApprItemsList());
                return q01xOutput;
            }

        };
    }

}
