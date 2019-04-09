//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.dao.db2;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.ford.it.properties.PropertyManager;
import com.ford.purchasing.wips.common.layer.BuyerDetail;
import com.ford.purchasing.wips.common.layer.JobDetail;
import com.ford.purchasing.wips.common.layer.JobTitleDetail;
import com.ford.purchasing.wips.common.layer.Mapper;
import com.ford.purchasing.wips.common.layer.UserProfile;
import com.ford.purchasing.wips.common.layer.util.WipsUtil;
import com.ford.purchasing.wips.domain.dao.DatabaseConstants;
import com.ford.purchasing.wips.domain.layer.exception.WipsDb2DatabaseException;
import com.ford.purchasing.wips.domain.resource.Database;

public class JobDetailDAO {

    private static final String NAME_NOT_FOUND = "Name Not Found";
    private static final String RETRIEVE_JOB_DETAILS_QUERY =
            "WIPSDatabase.DB2.SQLQueries.retrieveJobCodesAndNames";
    private static final String RETRIEVE_BUYER_JOB_DETAILS_QUERY =
            "WIPSDatabase.DB2.SQLQueries.retrieveBuyerJobCodesAndNames";
    @Inject
    private Database database;

    public Map<String, String> retrieveBuyerCodesAndNames(final Set<String> buyerCodes)
            throws WipsDb2DatabaseException {
        final String query =
                PropertyManager.getInstance().getString(RETRIEVE_BUYER_JOB_DETAILS_QUERY);
        final String sqlQueryWithParams =
                WipsUtil.populatePlaceholdersInSql(buyerCodes.size(), query);
        final List<String> params = createParamListForBuyerCodeQuery(buyerCodes);
        final List<BuyerDetail> buyerDetails = this.database.query(sqlQueryWithParams,
                buyerDetailsMapper(), params.toArray(new String[params.size()]));
        return createBuyerCodeMap(buyerDetails);
    }

    public UserProfile retrieveUserJobCodesProfile(final String racfId)
            throws WipsDb2DatabaseException {
        final String currentDate = WipsUtil.getDefaultFormattedCurrentDate();
        final String retrieveJobDetailsQuery =
                PropertyManager.getInstance().getString(RETRIEVE_JOB_DETAILS_QUERY);
        final List<JobTitleDetail> jobTitleDetails = this.database.query(
                retrieveJobDetailsQuery, jobTitleDetailsMapper(), currentDate, currentDate,
                racfId.toUpperCase(), currentDate, currentDate);
        final UserProfile UserProfile =
                createUserProfile(createJobTitleDetailsMap(jobTitleDetails));
        UserProfile.setUserRacfId(racfId);
        return UserProfile;
    }

    private Mapper<JobTitleDetail> jobTitleDetailsMapper() {
        return new Mapper<JobTitleDetail>() {
            @Override
            public JobTitleDetail map(final ResultSet resultSet) throws SQLException {
                return toJobTitleDetail(resultSet);
            }
        };
    }

    private Mapper<BuyerDetail> buyerDetailsMapper() {
        return new Mapper<BuyerDetail>() {
            @Override
            public BuyerDetail map(final ResultSet resultSet) throws SQLException {
                return toBuyerDetail(resultSet);
            }
        };
    }

    private Map<String, String> createBuyerCodeMap(final List<BuyerDetail> buyerCodeList) {
        final Map<String, String> buyerJobDetails = new HashMap<String, String>();
        for (final BuyerDetail buyerDetail : buyerCodeList) {
            buyerJobDetails.put(buyerDetail.getBuyerCode(),
                    buyerDetail.getBuyerCodeAndName());
        }
        return buyerJobDetails;
    }

    private List<String> createParamListForBuyerCodeQuery(final Set<String> buyerCodes) {
        final List<String> params = new ArrayList<String>(buyerCodes);
        final String currentDate = WipsUtil.getDefaultFormattedCurrentDate();
        final String[] currentDateParams = {currentDate, currentDate, currentDate};
        params.addAll(Arrays.asList(currentDateParams));
        return params;
    }

    private UserProfile createUserProfile(final Map<String, JobTitleDetail> map) {
        final UserProfile userProfile = new UserProfile();
        userProfile.setJobTitleDetails(map);
        return userProfile;
    }

    private JobTitleDetail toJobTitleDetail(final ResultSet resultSet)
            throws SQLException {
        final String jobCode =
                StringUtils.trim(resultSet.getString(DatabaseConstants.JOB_CODE));
        String jobEmplName =
                StringUtils.trim(resultSet.getString(DatabaseConstants.JOB_EMPL_NAME));
        if (StringUtils.isEmpty(jobEmplName)) {
            jobEmplName = NAME_NOT_FOUND;
        }
        final String jobTitle =
                StringUtils.trim(resultSet.getString(DatabaseConstants.JOB_TITLE));
        return new JobTitleDetail(new JobDetail(jobCode, jobEmplName), jobTitle);
    }

    private Map<String, JobTitleDetail> createJobTitleDetailsMap(
            final List<JobTitleDetail> jobCodeDetail) {
        final Map<String, JobTitleDetail> map = new HashMap<String, JobTitleDetail>();
        for (final JobTitleDetail jobTitleDetail : jobCodeDetail) {
            map.put(jobTitleDetail.getJobCodeDetail().getJobCode(), jobTitleDetail);
        }
        return map;
    }

    private BuyerDetail toBuyerDetail(final ResultSet resultSet)
            throws SQLException {
        final String buyerCode = StringUtils.trim(resultSet.getString(1));
        final String buyerName = StringUtils.trim(resultSet.getString(2));
        return new BuyerDetail(buyerCode, buyerName);
    }

}
