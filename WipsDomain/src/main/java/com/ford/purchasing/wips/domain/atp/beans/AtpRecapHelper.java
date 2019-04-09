//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.atp.beans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.atp.Approvers;
import com.ford.purchasing.wips.common.atp.AtpRemark;
import com.ford.purchasing.wips.common.atp.AtpSupplierDetail;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.util.StringUtil;
import com.ford.purchasing.wips.common.layer.util.TransactionOutputUtil;
import com.ford.purchasing.wips.common.layer.util.WipsUtil;
import com.ford.purchasing.wips.domain.layer.WipsTransactionConstant;

public class AtpRecapHelper {

    public void getAtpRecapDetails(final DataArea outputDataArea,
            final G53xTransactionOutput g53xOutput)
            throws ConnectorException {
        g53xOutput.setStrategy(TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-STRTYPE"));
        g53xOutput.setPart(getPartNumber(outputDataArea));
        g53xOutput.setEffectiveDate(WipsUtil
                .convertDateStringToFormattedDateString(effectiveDate(outputDataArea)));
        g53xOutput.setRawEffectiveDate(
                effectiveDate(outputDataArea));
        g53xOutput
                .setReasonCode(getRecapReason(outputDataArea) + " " + TransactionOutputUtil
                        .getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-RSNDSC"));
        final String autoPo = getAutoPo(outputDataArea);
        if ("Y".equals(autoPo))
            g53xOutput.setAutoPo("Yes");
        else
            g53xOutput.setAutoPo("No");
        g53xOutput.setAtpNumber(
                TransactionOutputUtil.getString(outputDataArea,
                        "TP-OUTPUT-BUFFER-FIELDS.TPO-ATP"));
        g53xOutput.setAttachmentsExist(
            StringUtils.isNotEmpty(TransactionOutputUtil.getString(outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-ATPURL")));
    }

    private String getRecapReason(final DataArea outputDataArea) throws ConnectorException {
        return TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-RECPRSN");
    }

    private String getAutoPo(final DataArea outputDataArea) throws ConnectorException {
        return TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-AUTOPO");
    }

    private String effectiveDate(final DataArea outputDataArea) throws ConnectorException {
        return TransactionOutputUtil.getString(
                outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-EFFDATE");
    }

    public String getPartNumber(final DataArea outputDataArea) throws ConnectorException {
        final String prefix = TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-PREFIX");
        final String base = TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-BASE");
        final String suffix = TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-SUFFIX");
        return prefix + "-" + base + "-" + suffix;
    }

    public List<AtpSupplierDetail> getAtpRecapSuppliers(final DataArea outputDataArea)
            throws ConnectorException {
        final List<AtpSupplierDetail> supplierDetails = new ArrayList<AtpSupplierDetail>();
        AtpSupplierDetail supplierBean = new AtpSupplierDetail();
        int supplierCount = 1;
        while (supplierCount <= 3) {
            final String siteCode = TransactionOutputUtil.getString(outputDataArea,
                    "TP-OUTPUT-BUFFER-FIELDS.TPO-SUPPCD" + supplierCount);
            if (StringUtils.isNotEmpty(siteCode)) {
                supplierBean = new AtpSupplierDetail();
                supplierBean.setSiteCode(siteCode);
                supplierBean.setSiteName(TransactionOutputUtil.getString(outputDataArea,
                        "TP-OUTPUT-BUFFER-FIELDS.TPO-SUPPNM" + supplierCount));
                supplierBean.setNewPrice(TransactionOutputUtil.getString(outputDataArea,
                        "TP-OUTPUT-BUFFER-FIELDS.TPO-NEWPR" + supplierCount));

                final String priceChange = TransactionOutputUtil.getString(
                        outputDataArea, "TP-OUTPUT-BUFFER-FIELDS.TPO-CHANGE"
                                        + supplierCount) + TransactionOutputUtil.getString(
                                                outputDataArea,
                                                "TP-OUTPUT-BUFFER-FIELDS.TPO-X"
                                                                + supplierCount);
                supplierBean.setPriceChange(priceChange);
                final String recapType = TransactionOutputUtil.getString(outputDataArea,
                        "TP-OUTPUT-BUFFER-FIELDS.TPO-AUTHCD" + supplierCount);
                supplierBean.setRecapType(recapType);
                String annualCost = TransactionOutputUtil.getString(outputDataArea,
                        "TP-OUTPUT-BUFFER-FIELDS.TPO-ANNCST" + supplierCount);
                if (annualCost == null || "".equals(annualCost) || "00".equals(recapType)) {
                    annualCost = "";
                }
                final String costSign = TransactionOutputUtil.getString(outputDataArea,
                        "TP-OUTPUT-BUFFER-FIELDS.TPO-ASIGN" + supplierCount);
                supplierBean.setAnnualCost(annualCost + costSign);
                supplierBean.setCostSign(costSign);
                supplierBean.setAuthority(TransactionOutputUtil.getString(outputDataArea,
                        "TP-OUTPUT-BUFFER-FIELDS.TPO-ADESC" + supplierCount));
                supplierBean
                        .setSourcingPercentage(
                                TransactionOutputUtil.getString(outputDataArea,
                                        "TP-OUTPUT-BUFFER-FIELDS.TPO-SHARE" + supplierCount)
                                               + "%");
                supplierBean
                        .setWebquoteAttachment(TransactionOutputUtil.getString(outputDataArea,
                                "TP-OUTPUT-BUFFER-FIELDS.TPO-SUPATC" + supplierCount));
                supplierBean.setPpc(TransactionOutputUtil.getString(outputDataArea,
                        "TP-OUTPUT-BUFFER-FIELDS.TPO-PPCISS" + supplierCount)
                        .replaceFirst("/", ""));
                supplierBean.setUsdLiteral(TransactionOutputUtil.getString(outputDataArea,
                        "TP-OUTPUT-BUFFER-FIELDS.TPO-USD" + supplierCount));
                supplierDetails.add(supplierBean);
            }
            supplierCount++;
        }
        return supplierDetails;
    }

    public List<Approvers> getAtpRecapApprovers(final DataArea outputDataArea)
            throws ConnectorException {
        final List<Approvers> approverDetails = new ArrayList<Approvers>();
        Approvers approverBean = new Approvers();
        int approversCount = 1;
        while (approversCount <= 6) {
            final String jobCode = TransactionOutputUtil.getString(outputDataArea,
                    "TP-OUTPUT-BUFFER-FIELDS.TPO-APPCD" + approversCount);
            if (StringUtils.isNotEmpty(jobCode)) {
                approverBean = new Approvers();
                approverBean.setJobCode(jobCode);
                approverBean.setApprover(TransactionOutputUtil.getString(outputDataArea,
                        "TP-OUTPUT-BUFFER-FIELDS.TPO-APPNM" + approversCount));
                approverBean.setDate(WipsUtil.convertDateStringToFormattedDateString(
                        TransactionOutputUtil.getString(outputDataArea,
                                "TP-OUTPUT-BUFFER-FIELDS.TPO-APPDT" + approversCount)));
                final String remarks = TransactionOutputUtil.getString(outputDataArea,
                        "TP-OUTPUT-BUFFER-FIELDS.TPO-RMKSI" + approversCount);
                if (StringUtils.isNotEmpty(remarks))
                    approverBean.setRemarks("Yes");
                else
                    approverBean.setRemarks("No");
                approverDetails.add(approverBean);
            }
            approversCount++;
        }
        return approverDetails;
    }

    public G53xTransactionInput setG53xInput(final DataArea outputDataArea)
            throws ConnectorException {
        final G53xTransactionInput transactionInput = new G53xTransactionInput();
        transactionInput
                .setAshppt1(TransactionOutputUtil.getString(outputDataArea,
                        "TP-OUTPUT-BUFFER-FIELDS.TPO-ASHPPT1"));
        transactionInput
                .setPtpnId(TransactionOutputUtil.getString(outputDataArea,
                        "TP-OUTPUT-BUFFER-FIELDS.TPO-PTPNID"));
        transactionInput.setPtpnMore(
                TransactionOutputUtil.getString(outputDataArea,
                        "TP-OUTPUT-BUFFER-FIELDS.TPO-PTPNMORE"));
        transactionInput
                .setRankNo(TransactionOutputUtil.getString(outputDataArea,
                        "TP-OUTPUT-BUFFER-FIELDS.TPO-RANKNO"));
        transactionInput.setRecapReason(
                getRecapReason(outputDataArea));
        transactionInput.setEffectiveDate(
                effectiveDate(outputDataArea));
        transactionInput
                .setAuthCd1(TransactionOutputUtil.getString(outputDataArea,
                        "TP-OUTPUT-BUFFER-FIELDS.TPO-AUTHCD1"));
        transactionInput
                .setAuthCd2(TransactionOutputUtil.getString(outputDataArea,
                        "TP-OUTPUT-BUFFER-FIELDS.TPO-AUTHCD2"));
        transactionInput
                .setAuthCd3(TransactionOutputUtil.getString(outputDataArea,
                        "TP-OUTPUT-BUFFER-FIELDS.TPO-AUTHCD3"));
        transactionInput
                .setAshppt2(TransactionOutputUtil.getString(outputDataArea,
                        "TP-OUTPUT-BUFFER-FIELDS.TPO-ASHPPT2"));
        transactionInput
                .setAshppt3(TransactionOutputUtil.getString(outputDataArea,
                        "TP-OUTPUT-BUFFER-FIELDS.TPO-ASHPPT3"));
        transactionInput
                .setAutoPo(getAutoPo(outputDataArea));
        transactionInput
                .setCommand(TransactionOutputUtil.getString(outputDataArea,
                        "TP-OUTPUT-BUFFER-FIELDS.TPO-COMAND"));
        transactionInput.setTpiPageNo(pageNumber(outputDataArea));
        return transactionInput;
    }

    private String pageNumber(final DataArea outputDataArea) throws ConnectorException {
        return TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-PAGENO-CHAR");
    }

    public List<Approvers> getAtpRecapApproversFromA02L(final DataArea outputDataArea)
            throws ConnectorException {
        final List<Approvers> approverDetails = new ArrayList<Approvers>();
        final List<DataArea> approversDataList =
                TransactionOutputUtil.getSegmentLoop(outputDataArea);
        DataArea parameterList;
        for (final Iterator<DataArea> i = approversDataList.iterator(); i.hasNext();) {
            final Approvers approverBean = new Approvers();
            parameterList = i.next();
            final String jobCode =
                    TransactionOutputUtil.getString(parameterList, "TPO-USRCDE");
            if (StringUtils.isNotEmpty(jobCode)) {
                approverBean.setJobCode(jobCode);
                approverBean.setApprover(
                        TransactionOutputUtil.getString(parameterList, "TPO-USRNME"));
                approverBean.setDate(WipsUtil
                        .convertDateStringToFormattedDateString(
                                TransactionOutputUtil.getString(parameterList,
                                        "TPO-APPRDT")));
                approverBean.setRemarks(
                        TransactionOutputUtil.getString(parameterList, "TPO-REMARK"));
                approverDetails.add(approverBean);
            }
        }
        return approverDetails;
    }

    public List<AtpRemark> getAtpRemarks(final DataArea outputDataArea)
            throws ConnectorException {
        final List<AtpRemark> atpRemarksList = new ArrayList<AtpRemark>();
        final AtpRemark remark = new AtpRemark();
        remark.setUser(getUserName(outputDataArea));
        remark.setUserJobCode(getJobCode(outputDataArea));
        remark.setRemarks(TransactionOutputUtil.getListOfValues(outputDataArea, "TPO-TEXT"));
        atpRemarksList.add(remark);
        return atpRemarksList;
    }

    public String getUserName(final DataArea outputDataArea) throws ConnectorException {
        return TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-USERNAME");
    }

    public String getJobCode(final DataArea outputDataArea) throws ConnectorException {
        return TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-JOBCODE");
    }

    public String getMorePages(final DataArea outputDataArea)
            throws ConnectorException {
        return TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-MORE");
    }

    public G56xTransactionInput populateG56xInput(final DataArea outputDataArea)
            throws ConnectorException {
        final G56xTransactionInput input = new G56xTransactionInput();
        input.setTpiIoindic(WipsConstant.InputI);
        input.setTpiPfKey(WipsConstant.PFKEY8);
        input.setCopyRemark(StringUtil.createBlankSpaces(6));
        input.setTpiPageNo(pageNumber(outputDataArea));
        input.setCanRemark1(TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-CANRMK1"));
        input.setCanRemark2(TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-CANRMK2"));
        input.setCanRemark3(TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-CANRMK3"));
        input.setCanRemark4(TransactionOutputUtil.getString(outputDataArea,
                "TP-OUTPUT-BUFFER-FIELDS.TPO-CANRMK4"));
        input.setBufferedSegLoop(TransactionOutputUtil.getSegmentLoop(outputDataArea));
        final List<String> outputFromDataAreaList = new ArrayList<String>();
        for (final Iterator<DataArea> i = input.getBufferedSegLoop().iterator(); i
                .hasNext();) {
            final DataArea inputDataArea = i.next();
            final String string = TransactionOutputUtil.getString(inputDataArea, "TPO-TEXT");
            outputFromDataAreaList.add(string);
        }
        input.setRemarksText(outputFromDataAreaList);
        return input;
    }

    public boolean hasMorePlants(final DataArea outputDataArea) throws ConnectorException {
        return getMorePages(outputDataArea).contains(WipsTransactionConstant.MORE);
    }
}
