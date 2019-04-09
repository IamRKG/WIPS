//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.layer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ford.it.connector.ConnectorException;
import com.ford.it.connector.record.DataArea;
import com.ford.purchasing.wips.common.atp.WipsAtpOutput;
import com.ford.purchasing.wips.common.layer.Category;
import com.ford.purchasing.wips.common.layer.WipsLumsumOutput;
import com.ford.purchasing.wips.common.layer.WipsPcsOutput;
import com.ford.purchasing.wips.common.layer.WipsPendingApprovalOutput;
import com.ford.purchasing.wips.common.layer.util.TransactionOutputUtil;
import com.ford.purchasing.wips.common.layer.util.WipsUtil;

public class PendingApprItemsHelper {
    public static final String SEGMENT_LOOP = "TP-OUTPUT-BUFFER-FIELDS.TPO-BUFFER-SEGLOOP";

    public List<WipsPendingApprovalOutput> populatePendingApprItems(final DataArea outputDataArea,
            final Category expectedCategory) throws ConnectorException {
        List<WipsPendingApprovalOutput> pendingApprList = new ArrayList<WipsPendingApprovalOutput>();
        switch (expectedCategory) {
        case ATP:
            pendingApprList = populatePendingAtps(outputDataArea);
            break;
        case LUMPSUM:
            pendingApprList = populatePendingLumsums(outputDataArea);
            break;
        case PRICE_CLAIMS:
            pendingApprList = populatePendingClaims(outputDataArea);
        default:
            break;
        }

        return pendingApprList;
    }

    private List<WipsPendingApprovalOutput> populatePendingClaims(final DataArea outputDataArea)
            throws ConnectorException {
        final List<WipsPendingApprovalOutput> wipsPendingPcsListOutput = new ArrayList<WipsPendingApprovalOutput>();
        final List<DataArea> outputDetailList = TransactionOutputUtil.getArray(outputDataArea, SEGMENT_LOOP);
        for (final Iterator<?> i = outputDetailList.iterator(); i.hasNext();) {
            final DataArea q01xDataArea = (DataArea) i.next();
            final WipsPcsOutput wipsPcsOutput = getWipsPcsOutput(q01xDataArea);
            if (!("".equalsIgnoreCase(wipsPcsOutput.getEntityNumber()))) {
                wipsPendingPcsListOutput.add(wipsPcsOutput);
            }
        }
        return wipsPendingPcsListOutput;
    }

    private List<WipsPendingApprovalOutput> populatePendingAtps(final DataArea outputDataArea)
            throws ConnectorException {
        final List<WipsPendingApprovalOutput> wipsPendingAtpListOutput = new ArrayList<WipsPendingApprovalOutput>();
        final List<DataArea> outputDetailList = TransactionOutputUtil.getArray(outputDataArea, SEGMENT_LOOP);
        for (final Iterator<?> i = outputDetailList.iterator(); i.hasNext();) {
            final DataArea q01xDataArea = (DataArea) i.next();
            final WipsAtpOutput wipsAtpOutput = getWipsAtpOutput(q01xDataArea);
            if (!("".equalsIgnoreCase(wipsAtpOutput.getEntityNumber()))) {
                wipsPendingAtpListOutput.add(wipsAtpOutput);
            }
        }
        return wipsPendingAtpListOutput;
    }

    private WipsAtpOutput getWipsAtpOutput(final DataArea q01xDataArea) throws ConnectorException {
        final WipsAtpOutput wipsAtpOutput = new WipsAtpOutput();
        wipsAtpOutput.setBuyerCode(TransactionOutputUtil.getString(q01xDataArea, "TPO-DETAIL.TPO-RESP-JOB"));
        wipsAtpOutput.setEntityNumber(TransactionOutputUtil.getString(q01xDataArea, "TPO-DETAIL.TPO-ATP-NUMBER"));
        wipsAtpOutput.setPartNumber(getPartNumber(q01xDataArea));
        wipsAtpOutput.setReadOrUnreadFlag(TransactionOutputUtil.getString(q01xDataArea, "TPO-DETAIL-ATTR"));
        wipsAtpOutput.setSubsequentProgram(TransactionOutputUtil.getString(q01xDataArea, "TPO-DETAIL.TPO-FILLER1"));
        return wipsAtpOutput;
    }

    private WipsPcsOutput getWipsPcsOutput(final DataArea q01xDataArea) throws ConnectorException {
        final WipsPcsOutput wipsPcsOutput = new WipsPcsOutput();
        wipsPcsOutput.setEntityNumber(TransactionOutputUtil.getString(q01xDataArea, "TPO-EPPSNO"));
        wipsPcsOutput.setSupplier(TransactionOutputUtil.getString(q01xDataArea, "TPO-SUPPNO"));
        wipsPcsOutput.setSupplierName(TransactionOutputUtil.getString(q01xDataArea, "TPO-SNAME"));
        wipsPcsOutput.setEffectiveDate(WipsUtil
                .convertDateStringToFormattedDateString(TransactionOutputUtil.getString(q01xDataArea, "TPO-EDATE")));
        wipsPcsOutput.setClaimTitle(TransactionOutputUtil.getString(q01xDataArea, "TPO-RMRKS"));
        final String readOrUnread = TransactionOutputUtil.getString(q01xDataArea, "TPO-LINENO-ATTR");
        wipsPcsOutput.setReadOrUnreadFlag(("9").equals(readOrUnread) ? readOrUnread : "0");
        return wipsPcsOutput;
    }

    private String getPartNumber(final DataArea q01xDataArea) throws ConnectorException {
        final String partNumberPrefix = TransactionOutputUtil.getString(q01xDataArea, "TPO-DETAIL.TPO-PART-PREFIX");
        final String partNumberBase = TransactionOutputUtil.getString(q01xDataArea, "TPO-DETAIL.TPO-PART-BASE");
        final String partNumberSuffix = TransactionOutputUtil.getString(q01xDataArea, "TPO-DETAIL.TPO-PART-SUFFIX");
        return partNumberPrefix + "-" + partNumberBase + "-" + partNumberSuffix;
    }

    private List<WipsPendingApprovalOutput> populatePendingLumsums(final DataArea outputDataArea)
            throws ConnectorException {
        final List<WipsPendingApprovalOutput> wipsPendingLumsumListOutput = new ArrayList<WipsPendingApprovalOutput>();
        final List<DataArea> outputDetailList = TransactionOutputUtil.getArray(outputDataArea, SEGMENT_LOOP);
        for (final Iterator<?> i = outputDetailList.iterator(); i.hasNext();) {
            final DataArea q01xDataArea = (DataArea) i.next();
            final WipsLumsumOutput wipslumsumOutput = getWipslumsumOutput(q01xDataArea);
            if (!("".equalsIgnoreCase(wipslumsumOutput.getEntityNumber()))) {
                wipsPendingLumsumListOutput.add(wipslumsumOutput);
            }
        }
        return wipsPendingLumsumListOutput;
    }

    private WipsLumsumOutput getWipslumsumOutput(final DataArea q01xDataArea) throws ConnectorException {
        final WipsLumsumOutput wipsLumsumOutput = new WipsLumsumOutput();
        wipsLumsumOutput.setBuyerCode(TransactionOutputUtil.getString(q01xDataArea, "TPO-DETAIL.TPO-RESP-JOB"));
        wipsLumsumOutput.setEntityNumber(TransactionOutputUtil.getString(q01xDataArea, "TPO-DETAIL.TPO-ATP-NUMBER"));
        wipsLumsumOutput.setCause(TransactionOutputUtil.getString(q01xDataArea, "TPO-DETAIL.TPO-CAUSAL"));
        wipsLumsumOutput.setSupplierCode(TransactionOutputUtil.getString(q01xDataArea, "TPO-DETAIL.TPO-SUPPLIER"));
        wipsLumsumOutput.setReadOrUnreadFlag(TransactionOutputUtil.getString(q01xDataArea, "TPO-DETAIL-ATTR"));
        wipsLumsumOutput.setSubsequentProgram(TransactionOutputUtil.getString(q01xDataArea, "TPO-DETAIL.TPO-FILLER1"));
        return wipsLumsumOutput;
    }
}
