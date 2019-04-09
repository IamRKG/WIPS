//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.dao.db2;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.ford.it.properties.PropertyManager;
import com.ford.purchasing.wips.common.layer.ApprovalDetail;
import com.ford.purchasing.wips.common.layer.Mapper;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.common.layer.util.WipsUtil;
import com.ford.purchasing.wips.domain.dao.DatabaseConstants;
import com.ford.purchasing.wips.domain.layer.exception.WipsDb2DatabaseException;
import com.ford.purchasing.wips.domain.resource.Database;

@SuppressWarnings("javadoc")
public class LsApproversDAO {

    private static final String RETRIEVE_APPROVERS_LIST_QUERY =
            "WIPSDatabase.DB2.SQLQueries.retrieveLumpSumApprovers";

    @Inject
    private Database database;

    public List<ApprovalDetail> retrieveApprovers(final String lumpSumNumber,
            final String lumpSumVersion, final Map<String, List<String>> remarksMap,
            final String currentJobCode)
            throws WipsDb2DatabaseException {
        final String sqlStatement =
                PropertyManager.getInstance().getString(RETRIEVE_APPROVERS_LIST_QUERY);
        final String currentDate = WipsUtil.getDefaultFormattedCurrentDate();
        final List<ApprovalDetail> approvalsList =
                this.database.query(sqlStatement, new Mapper<ApprovalDetail>() {
                    @Override
                    public ApprovalDetail map(final ResultSet resultSet) throws SQLException {
                        return toApprovalDetail(remarksMap, resultSet, currentJobCode);
                    }
                }, currentDate, currentDate, lumpSumNumber, lumpSumVersion);
        if (remarksMap.get(DatabaseConstants.SYST) != null) {
            approvalsList.add(0, getSystemRemarks(remarksMap));
        }
        return approvalsList;
    }

    private ApprovalDetail getSystemRemarks(final Map<String, List<String>> remarksMap) {
        ApprovalDetail approverDetail = null;
        approverDetail = new ApprovalDetail();
        approverDetail.setJobCode(DatabaseConstants.SYSTEM);
        approverDetail.setApproverName("");
        approverDetail.setDate("");
        approverDetail.setStatus("");
        approverDetail.setRemarks(remarksMap.get(DatabaseConstants.SYST));
        return approverDetail;
    }

    private ApprovalDetail toApprovalDetail(final Map<String, List<String>> remarksMap,
            final ResultSet resultSet, final String currentJobCode) throws SQLException {
        final ApprovalDetail approverDetail = new ApprovalDetail();
        final String jobCode = resultSet.getString(DatabaseConstants.JOBCODE);
        approverDetail.setJobCode(jobCode);
        approverDetail.setApproverName(resultSet.getString(DatabaseConstants.NAME));
        approverDetail.setDate(resultSet.getString(DatabaseConstants.APPRL_DATE));
        approverDetail.setStatus(resultSet.getString(DatabaseConstants.STATUS));
        if (WipsConstant.YES
                .equalsIgnoreCase(resultSet.getString(DatabaseConstants.REMARKS))) {
            approverDetail.setRemarks(remarksMap.get(jobCode));
        } else {
            final List<String> remarks = new ArrayList<String>();
            if (!jobCode.equals(currentJobCode))
                remarks.add("No");
            approverDetail.setRemarks(remarks);

        }
        return approverDetail;
    }
}
