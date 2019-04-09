package com.ford.purchasing.wips.domain.pcs.beans;

import java.util.ArrayList;
import java.util.List;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.connector.BiFunctionThrowsException;
import com.ford.purchasing.wips.common.layer.Category;
import com.ford.purchasing.wips.common.layer.WipsImsOutput;
import com.ford.purchasing.wips.common.layer.WipsPendingApprovalOutput;
import com.ford.purchasing.wips.common.layer.exception.WipsImsTransactionException;
import com.ford.purchasing.wips.common.layer.util.TransactionOutputUtil;
import com.ford.purchasing.wips.domain.layer.PendingApprItemsHelper;
import com.ford.purchasing.wips.domain.layer.WipsTransactionConstant;

public class Q43lTransactionOutput extends WipsImsOutput {

    private String userJobCode;
    private String userJobName;
    private String containMorePages;
    private String pageNo;
    private String selLine;
    private String pageSkip;
    private List<WipsPendingApprovalOutput> pendingApprItemsList =
            new ArrayList<WipsPendingApprovalOutput>();

    String tpiCat;

    public String getSelLine() {
        return selLine;
    }

    public void setSelLine(final String selLine) {
        this.selLine = selLine;
    }

    public String getPageSkip() {
        return pageSkip;
    }

    public void setPageSkip(final String pageSkip) {
        this.pageSkip = pageSkip;
    }

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

    public Q43lTransactionOutput from(final DataArea outputDataArea, final String rawOutput)
            throws ConnectorException, WipsImsTransactionException {

        Q43lTransactionOutput q43lTransactionOutput = null;
        final String transactionName = TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-PGMNAME");
        if (WipsTransactionConstant.Q43L_TRANSACTION_NAME.equals(transactionName)) {
            if (!rawOutput.substring(38, 114).contains("MSG-0772")) {
                q43lTransactionOutput = new Q43lTransactionOutput();
                q43lTransactionOutput
                        .setUserJobCode(TransactionOutputUtil.getString(outputDataArea,
                                "TP-OUTPUT-BUFFER-FIELDS.TPO-BCODE"));
                q43lTransactionOutput
                        .setUserJobName(TransactionOutputUtil.getString(outputDataArea,
                                "TP-OUTPUT-BUFFER-FIELDS.TPO-BNAME"));
                q43lTransactionOutput.setContainMorePages(TransactionOutputUtil.getString(
                        outputDataArea,
                        "TP-OUTPUT-BUFFER-FIELDS.TPO-MORE") == null ? ""
                                                                    : TransactionOutputUtil
                                                                            .getString(
                                                                                    outputDataArea,
                                                                                    "TP-OUTPUT-BUFFER-FIELDS.TPO-MORE"));
                q43lTransactionOutput
                        .setPageNo(TransactionOutputUtil.getString(outputDataArea,
                                "TP-OUTPUT-BUFFER-FIELDS.TPO-PAGENO-CHAR"));
                q43lTransactionOutput.setSelLine(TransactionOutputUtil.getString(
                        outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-SELLINE"));
                q43lTransactionOutput.setPageSkip(TransactionOutputUtil.getString(
                        outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-PAGESKIP"));
                q43lTransactionOutput.setPendingApprItemsList(
                        new PendingApprItemsHelper().populatePendingApprItems(outputDataArea,
                                Category.PRICE_CLAIMS));
                q43lTransactionOutput.setErrorFlag(false);
            } else {
                throw new WipsImsTransactionException(transactionName,
                        rawOutput.substring(38, 114));
            }

        } else {
            throw new WipsImsTransactionException("",
                    rawOutput.substring(108, 184));
        }
        return q43lTransactionOutput;

    }

    public BiFunctionThrowsException<DataArea, String, Q43lTransactionOutput, Exception> getQ43lOutput(
            final List<WipsPendingApprovalOutput> priceClaims) {
        return new BiFunctionThrowsException<DataArea, String, Q43lTransactionOutput, Exception>() {

            @Override
            public Q43lTransactionOutput apply(
                    final DataArea outputDataArea,
                    final String rawOutput) throws Exception {
                Q43lTransactionOutput output = new Q43lTransactionOutput();
                output = from(outputDataArea, rawOutput);
                priceClaims.addAll(output.getPendingApprItemsList());
                return output;
            }

        };
    }

    public BiFunctionThrowsException<DataArea, String, Q43lTransactionOutput, Exception> getQ43lOutput() {
        return new BiFunctionThrowsException<DataArea, String, Q43lTransactionOutput, Exception>() {

            @Override
            public Q43lTransactionOutput apply(
                    final DataArea outputDataArea,
                    final String rawOutput) throws Exception {
                return new Q43lTransactionOutput().from(outputDataArea, rawOutput);
            }
        };
    }

}
