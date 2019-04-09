//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.dao.db2;

import static com.ford.purchasing.wips.domain.dao.DatabaseConstants.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

import com.ford.it.properties.PropertyManager;
import com.ford.purchasing.wips.common.layer.LsClassification;
import com.ford.purchasing.wips.common.layer.Mapper;
import com.ford.purchasing.wips.domain.layer.exception.WipsDb2DatabaseException;
import com.ford.purchasing.wips.domain.resource.Database;

@SuppressWarnings("javadoc")
public class LsClassificationDAO {

    private static final String RETRIEVE_LUMPSUM_CLASSIFICATION =
            "WIPSDatabase.DB2.SQLQueries.retrieveLumpSumClassifications";

    @Inject
    private Database database;

    public List<LsClassification> retrieveLumpSumClassifications(
            final String lumpSumNumber,
            final String lumpSumVersion) throws WipsDb2DatabaseException {
        final String sql =
                PropertyManager.getInstance().getString(RETRIEVE_LUMPSUM_CLASSIFICATION);
        return this.database.query(sql, classificationMapper(), lumpSumNumber,
                lumpSumVersion);
    }

    private Mapper<LsClassification> classificationMapper() {
        return new Mapper<LsClassification>() {
            @Override
            public LsClassification map(final ResultSet resultSet) throws SQLException {
                return toLsClassification(resultSet);
            }
        };
    }

    private LsClassification toLsClassification(final ResultSet resultSet)
            throws SQLException {
        final LsClassification classification = new LsClassification();
        classification.setCode(resultSet.getString(CLASS_CODE));
        classification.setDescription(resultSet.getString(CLASS_DESC));
        classification.setAccount(resultSet.getString(ACCOUNT));
        classification.setSubDivision(resultSet.getString(SUB_DIVISION));
        classification.setFinanceAnalyst(resultSet.getString(FINANCE_ANALYST));
        classification.setPrePayInd(resultSet.getString(PRE_PAY_IND));
        classification.setPrePayAccType(resultSet.getString(PREPAY_ACCOUNT_TYPE));
        classification.setNationalCompany(resultSet.getString(NATIONAL_COMPANY));
        classification.setPlant(resultSet.getString(PLANT));
        return classification;
    }

}
