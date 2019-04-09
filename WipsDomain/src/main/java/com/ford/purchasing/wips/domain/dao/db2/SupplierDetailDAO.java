//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.dao.db2;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.ford.it.properties.PropertyManager;
import com.ford.purchasing.wips.common.layer.Mapper;
import com.ford.purchasing.wips.common.layer.SupplierDetail;
import com.ford.purchasing.wips.common.layer.util.WipsUtil;
import com.ford.purchasing.wips.domain.layer.exception.WipsDb2DatabaseException;
import com.ford.purchasing.wips.domain.resource.Database;

/**
 * This class deals with DB2 table GBV_A02_SITE to fetch supplier site details.
 */
public class SupplierDetailDAO {

    private static final String RETRIEVE_SUPPLIER_DETAILS_QUERY =
            "WIPSDatabase.DB2.SQLQueries.retrieveSupplierCodesAndNames";

    @Inject
    private Database database;

    /**
     * This method fetched the supplier site code & name form the DB2 database.
     */
    public Map<String, String> retrieveSupplierCodesAndNames(final Set<String> supplierCodes)
            throws WipsDb2DatabaseException {
        final String sqlQuery =
                PropertyManager.getInstance().getString(RETRIEVE_SUPPLIER_DETAILS_QUERY);
        final String retrieveSupplierDetailsSql =
                WipsUtil.populatePlaceholdersInSql(supplierCodes.size(), sqlQuery);
        final List<SupplierDetail> supplierDetails =
                this.database.query(retrieveSupplierDetailsSql, new Mapper<SupplierDetail>() {
                    @Override
                    public SupplierDetail map(final ResultSet resultSet) throws SQLException {
                        return toSupplierDetail(resultSet);
                    }
                }, supplierCodes.toArray(new String[supplierCodes.size()]));
        return createSupplierDetailsMap(supplierDetails);
    }

    private Map<String, String> createSupplierDetailsMap(
            final List<SupplierDetail> supplierDetails) {
        final Map<String, String> supplierSiteDetails = new HashMap<String, String>();
        for (final SupplierDetail supplierDetail : supplierDetails) {
            if (!StringUtils.isEmpty(supplierDetail.getSiteCode())) {
                supplierSiteDetails.put(supplierDetail.getSiteCode(),
                        supplierDetail.getSiteCode() + " - " + supplierDetail.getSiteName());
            }
        }
        return supplierSiteDetails;
    }

    private SupplierDetail toSupplierDetail(final ResultSet resultSet)
            throws SQLException {
        final SupplierDetail supplierDetail = new SupplierDetail();
        final String supplierSiteCode = StringUtils.trim(resultSet.getString(2));
        String supplierSiteName = StringUtils.trim(resultSet.getString(1));
        if (StringUtils.isEmpty(supplierSiteName)) {
            supplierSiteName = "***";
        }
        supplierDetail.setSiteCode(supplierSiteCode);
        supplierDetail.setSiteName(supplierSiteName);
        return supplierDetail;
    }
}
