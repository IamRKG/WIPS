//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.dao.db2;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.ford.it.properties.PropertyManager;
import com.ford.purchasing.wips.common.layer.Mapper;
import com.ford.purchasing.wips.common.layer.util.WipsUtil;
import com.ford.purchasing.wips.domain.dao.DatabaseConstants;
import com.ford.purchasing.wips.domain.layer.exception.WipsDb2DatabaseException;
import com.ford.purchasing.wips.domain.resource.Database;

public class PcCausalDescriptionDAO {

    private static final String RETRIEVE_CAUSAL_DESCRIPTION_QUERY =
            "WIPSDatabase.DB2.SQLQueries.retrievecausalFactorPriceClaims";

    @Inject
    private Database database;

    public String retrieveCausalDesription(final String causalFactorCode)
            throws WipsDb2DatabaseException {
        final String sqlQuery =
                PropertyManager.getInstance().getString(RETRIEVE_CAUSAL_DESCRIPTION_QUERY);
        final String causalFactorQuery =
                WipsUtil.populatePlaceholdersInSql(1, sqlQuery);
        final List<String> supplierDetailsList =
                this.database.query(causalFactorQuery, new Mapper<String>() {
                    @Override
                    public String map(final ResultSet resultSet) throws SQLException {
                        return toSupplierDetail(resultSet);
                    }
                }, causalFactorCode);
        return supplierDetailsList.get(0);
    }

    private String toSupplierDetail(final ResultSet resultSet) throws SQLException {
        return StringUtils.trim(resultSet.getString(DatabaseConstants.CAUSAL_DESCRIPTION));
    }

}
