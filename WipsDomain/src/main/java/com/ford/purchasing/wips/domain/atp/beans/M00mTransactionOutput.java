package com.ford.purchasing.wips.domain.atp.beans;

import java.util.List;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.connector.BiFunctionThrowsException;
import com.ford.purchasing.wips.common.connector.Predicate;
import com.ford.purchasing.wips.common.layer.PendingApproval;
import com.ford.purchasing.wips.common.layer.WipsBaseRequest;
import com.ford.purchasing.wips.common.layer.WipsImsOutput;
import com.ford.purchasing.wips.common.layer.exception.WipsImsTransactionException;
import com.ford.purchasing.wips.common.layer.util.TransactionOutputUtil;
import com.ford.purchasing.wips.domain.connector.WipsTransactionHelper;
import com.ford.purchasing.wips.domain.layer.WipsTransactionConstant;

@SuppressWarnings("javadoc")
public class M00mTransactionOutput extends WipsImsOutput {

    private String userJobCode;
    private String userJobName;
    private List<PendingApproval> pendingApprovalList;

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

    public List<PendingApproval> getPendingApprovalList() {
        return this.pendingApprovalList;
    }

    public void setPendingApprovalList(final List<PendingApproval> pendingApprovalList) {
        this.pendingApprovalList = pendingApprovalList;
    }

    public M00mTransactionOutput from(final DataArea outputDataArea,
            final String rawOutput) throws ConnectorException, WipsImsTransactionException {

        final String transactionName = TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-PROGRAM");
        if (!WipsTransactionConstant.M00M_TRANSACTION_NAME.equals(transactionName)) {
            throw new WipsImsTransactionException(
                    "", rawOutput.substring(38, 113));
        }
        return new WipsTransactionHelper().populateM00mOutput(outputDataArea);

    }

    public BiFunctionThrowsException<DataArea, String, M00mTransactionOutput, Exception> getEppsOutput() {
        return new BiFunctionThrowsException<DataArea, String, M00mTransactionOutput, Exception>() {

            @Override
            public M00mTransactionOutput apply(
                    final DataArea outputDataArea,
                    final String rawOutput) throws Exception {
                return new M00mTransactionOutput()
                        .from(outputDataArea, rawOutput);
            }

        };
    }

    public Predicate<M00mTransactionOutput> notUserJobCode(
            final WipsBaseRequest atpRequest) {
        return new Predicate<M00mTransactionOutput>() {
            @Override
            public boolean test(
                    final M00mTransactionOutput m00mTransactionOutput) {
                return !m00mTransactionOutput.getUserJobCode()
                        .equals(atpRequest
                                .getCurrentJobCode());
            }
        };
    }

    public BiFunctionThrowsException<DataArea, String, M00mTransactionOutput, Exception> getM00mSwitchJobOutput() {
        return new BiFunctionThrowsException<DataArea, String, M00mTransactionOutput, Exception>() {

            @Override
            public M00mTransactionOutput apply(
                    final DataArea outputDataArea,
                    final String rawOutput) throws Exception {
                return new M00mTransactionOutput()
                        .from(outputDataArea, rawOutput);
            }

        };
    }

}
