package com.ford.purchasing.wips.domain.dao.db2;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import com.ford.purchasing.wips.domain.dao.DatabaseConstants;
import com.ford.purchasing.wips.domain.layer.exception.WipsDb2DatabaseException;
import com.ford.purchasing.wips.domain.resource.Database;

public class PcSupplierNameDAO {

    private static final String RETRIEVE_SUPPLIER_DETAILS_QUERY = "WIPSDatabase.DB2.SQLQueries.retrieveSupplierCodesAndNamesForPriceClaims";

    @Inject
    private Database database;

    public Map<String, String> retrieveSupplierNames(final Set<String> siteCodes, final Set<String> supplierCodes)
            throws WipsDb2DatabaseException {
        final Set<String> siteCodesParam = checkForEmptyParams(siteCodes);
        final Set<String> supplierCodesParam = checkForEmptyParams(supplierCodes);
        final String sqlQuery = PropertyManager.getInstance().getString(RETRIEVE_SUPPLIER_DETAILS_QUERY);
        final String supplierDetailsQuery = WipsUtil.populatePlaceholdersInSql(siteCodesParam.size(),
                supplierCodesParam.size(), sqlQuery);
        final List<SupplierDetail> supplierDetailsList = this.database.query(supplierDetailsQuery,
                new Mapper<SupplierDetail>() {
                    @Override
                    public SupplierDetail map(final ResultSet resultSet) throws SQLException {
                        return toSupplierDetail(resultSet);
                    }
                }, paramList(siteCodesParam, supplierCodesParam));
        final Map<String, String> supplierNamesMap = new HashMap<String, String>();
        for (final SupplierDetail supplierDetail : supplierDetailsList) {
            supplierNamesMap.put(supplierDetail.getSiteCode(), supplierDetail.getSiteName());
        }
        return supplierNamesMap;
    }

    private Set<String> checkForEmptyParams(final Set<String> codes) {
        final Set<String> codesParam = codes;
        if (codesParam.size() == 0) {
            codesParam.add("");
        }
        return codesParam;
    }

    private String[] paramList(final Set<String> siteCodes, final Set<String> supplierCodes) {
        final List<String> paramList = new ArrayList<String>();
        paramList.addAll(siteCodes);
        paramList.addAll(supplierCodes);
        return paramList.toArray(new String[siteCodes.size() + supplierCodes.size()]);
    }

    private SupplierDetail toSupplierDetail(final ResultSet resultSet) throws SQLException {
        final SupplierDetail supplierDetail = new SupplierDetail();
        supplierDetail.setSiteCode(StringUtils.trim(resultSet.getString(DatabaseConstants.SITE_CODE)));
        supplierDetail.setSiteName(StringUtils.trim(resultSet.getString(DatabaseConstants.SITE_NAME)));
        return supplierDetail;
    }
}
