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
import com.ford.purchasing.wips.common.layer.Mapper;
import com.ford.purchasing.wips.common.lumpsum.SupplierInformation;
import com.ford.purchasing.wips.domain.layer.exception.WipsDb2DatabaseException;
import com.ford.purchasing.wips.domain.resource.Database;

@SuppressWarnings("javadoc")
public class LsSuppliersDAO {

    private static final String RETRIEVE_SUPPLIERS_LIST_QUERY =
            "WIPSDatabase.DB2.SQLQueries.retrieveLumpSumSuppliers";

    @Inject
    private Database database;

    public List<SupplierInformation> retrieveSuppliers(final String lumpsumNumber,
            final String lumpsumVersion) throws WipsDb2DatabaseException {
        final String query =
                PropertyManager.getInstance().getString(RETRIEVE_SUPPLIERS_LIST_QUERY);
        return this.database.query(query, supplierInfoMapper(), lumpsumNumber, lumpsumVersion,
                lumpsumNumber);
    }

    private Mapper<SupplierInformation> supplierInfoMapper() {
        return new Mapper<SupplierInformation>() {
            @Override
            public SupplierInformation map(final ResultSet resultSet) throws SQLException {
                return toSupplierInformation(resultSet);
            }
        };
    }

    private SupplierInformation toSupplierInformation(final ResultSet resultSet)
            throws SQLException {
        final SupplierInformation supplierInformation = new SupplierInformation();
        supplierInformation.setSiteCode(resultSet.getString(SITE_CODE));
        supplierInformation.setSiteName(resultSet.getString(SITE_NAME));
        supplierInformation.setParentCode(resultSet.getString(PARENT_CODE));
        return supplierInformation;
    }

}
