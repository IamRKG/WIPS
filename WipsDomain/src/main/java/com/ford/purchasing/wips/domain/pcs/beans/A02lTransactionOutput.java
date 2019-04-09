//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.pcs.beans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.atp.Approvers;
import com.ford.purchasing.wips.common.connector.BiFunctionThrowsException;
import com.ford.purchasing.wips.common.layer.ApprovalDetail;
import com.ford.purchasing.wips.common.layer.WipsImsOutput;
import com.ford.purchasing.wips.common.layer.exception.WipsImsTransactionException;
import com.ford.purchasing.wips.common.layer.util.TransactionOutputUtil;
import com.ford.purchasing.wips.common.layer.util.WipsUtil;
import com.ford.purchasing.wips.domain.atp.beans.AtpRecapHelper;
import com.ford.purchasing.wips.domain.layer.WipsTransactionConstant;

public class A02lTransactionOutput extends WipsImsOutput {

    private List<Approvers> approvers;
    private String hasMorePages;
    private List<ApprovalDetail> approverDetail;

    public List<ApprovalDetail> getApproverDetail() {
        return this.approverDetail;
    }

    public void setApproverDetail(final List<ApprovalDetail> approverDetail) {

        this.approverDetail = approverDetail;
    }

    public List<Approvers> getApprovers() {
        return this.approvers;
    }

    public void setApprovers(final List<Approvers> approvers) {
        this.approvers = approvers;
    }

    public String getHasMorePages() {
        return this.hasMorePages;
    }

    public void setHasMorePages(final String hasMorePages) {

        this.hasMorePages = hasMorePages;
    }

    public A02lTransactionOutput from(final DataArea outputDataArea, final String rawOutput)
            throws ConnectorException, WipsImsTransactionException {
        final String transactionName = TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-PROGRAM");
        final A02lTransactionOutput output = new A02lTransactionOutput();
        if (!"".equals(transactionName) && "A02L".contains(transactionName)) {
            output.setApproverDetail(getAtpRecapApproversFromA02L(outputDataArea));
            final String hasTpoMorePages =
                    TransactionOutputUtil.getString(outputDataArea,
                            "TP-OUTPUT-BUFFER-FIELDS.TPO-MORE") == null ? ""
                                                                        : TransactionOutputUtil
                                                                                .getString(
                                                                                        outputDataArea,
                                                                                        "TP-OUTPUT-BUFFER-FIELDS.TPO-MORE");
            output.setHasMorePages(hasTpoMorePages);
        } else {
            final String error =
                    rawOutput.length() > 61 && rawOutput.length() < 142 ? rawOutput
                            .substring(62, 141) : "";
            throw new WipsImsTransactionException("A02L", error);
        }
        return output;
    }

    public List<ApprovalDetail> getAtpRecapApproversFromA02L(final DataArea outputDataArea)
            throws ConnectorException {
        final List<DataArea> approversDataList =
                TransactionOutputUtil.getSegmentLoop(outputDataArea);
        DataArea parameterList;
        ApprovalDetail approverBean = new ApprovalDetail();
        final List<ApprovalDetail> approverDetailList = new ArrayList<ApprovalDetail>();
        for (final Iterator<DataArea> i = approversDataList.iterator(); i.hasNext();) {
            approverBean = new ApprovalDetail();
            parameterList = i.next();
            final String jobCode =
                    TransactionOutputUtil.getString(parameterList, "TPO-USRCDE");
            if (StringUtils.isNotEmpty(jobCode)) {
                approverBean.setJobCode(jobCode);
                approverBean.setApproverName(
                        TransactionOutputUtil.getString(parameterList, "TPO-USRNME"));
                approverBean.setDate(WipsUtil
                        .convertDateStringToFormattedDateString(
                                TransactionOutputUtil.getString(parameterList,
                                        "TPO-APPRDT")));
                final List<String> remarks = new ArrayList<String>();
                remarks.add(
                        TransactionOutputUtil.getString(parameterList, "TPO-REMARK"));
                approverBean.setStatus(
                        TransactionOutputUtil.getString(parameterList, "TPO-STATUS"));
                approverBean.setRemarks(remarks);
                approverDetailList.add(approverBean);
            }
        }
        return approverDetailList;
    }

    public static BiFunctionThrowsException<DataArea, String, A02lTransactionOutput, Exception> getA02lOutput(
            final A02lTransactionOutput output,
            final Map<String, ApprovalDetail> remarksMap) {
        return new BiFunctionThrowsException<DataArea, String, A02lTransactionOutput, Exception>() {

            @Override
            public A02lTransactionOutput apply(
                    final DataArea outputDataArea,
                    final String rawOutput) throws Exception {
                A02lTransactionOutput approversA02lOutput =
                        new A02lTransactionOutput();
                approversA02lOutput =
                        approversA02lOutput.from(outputDataArea,
                                rawOutput);
                getApprovers(remarksMap, approversA02lOutput);
                output.setApproverDetail(approversA02lOutput.getApproverDetail());
                return approversA02lOutput;
            }

        };
    }

    private static void getApprovers(
            final Map<String, ApprovalDetail> remarksMap,
            final A02lTransactionOutput approvers) {
        for (int count = 0; count < approvers.getApproverDetail().size(); count++) {
            final ApprovalDetail approver =
                    approvers.getApproverDetail().get(count);
            remarksMap.put(approver.getJobCode(),
                    approver);
        }
    }

    public static BiFunctionThrowsException<DataArea, String, A02lTransactionOutput, Exception> getA02lOutputWithRemarks(
            final A02lTransactionOutput output,
            final Map<String, ApprovalDetail> remarksMap) {
        return new BiFunctionThrowsException<DataArea, String, A02lTransactionOutput, Exception>() {

            @Override
            public A02lTransactionOutput apply(
                    final DataArea outputDataArea,
                    final String rawOutput) throws Exception {
                A02lTransactionOutput approversA02lOutput =
                        new A02lTransactionOutput();
                final List<ApprovalDetail> approversList =
                        output.getApproverDetail();
                approversA02lOutput = approversA02lOutput.from(outputDataArea, rawOutput);
                getApprovers(remarksMap, approversA02lOutput);
                approversList.addAll(approversA02lOutput.getApproverDetail());
                output.setApproverDetail(approversList);
                return approversA02lOutput;
            }

        };
    }

    public BiFunctionThrowsException<DataArea, String, A02lTransactionOutput, Exception> getA02lOutput() {
        return new BiFunctionThrowsException<DataArea, String, A02lTransactionOutput, Exception>() {

            @Override
            public A02lTransactionOutput apply(final DataArea outputDataArea,
                    final String rawOutput) throws Exception {
                final A02lTransactionOutput output = new A02lTransactionOutput();
                final String transactionName =
                        TransactionOutputUtil.getString(outputDataArea,
                                "TP-OUTPUT-BUFFER-FIELDS.TPO-PROGRAM");
                if (WipsTransactionConstant.A02L_TRANSACTION_NAME.equals(
                        transactionName)) {
                    output.setApprovers(new AtpRecapHelper().getAtpRecapApproversFromA02L(outputDataArea));
                } /*else {
                    throw new WipsImsTransactionException(
                            transactionName, rawOutput.substring(38, 113));
                  }*/
                return output;
            }
        };
    }
}
