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
import com.ford.purchasing.wips.common.layer.GsaAudit;
import com.ford.purchasing.wips.common.layer.Mapper;
import com.ford.purchasing.wips.domain.dao.DatabaseConstants;
import com.ford.purchasing.wips.domain.layer.exception.WipsDb2DatabaseException;
import com.ford.purchasing.wips.domain.resource.Database;

@SuppressWarnings("javadoc")
public class LsGsaAuditDAO {

    private static final String GSA_AUDIT_VALUES_QUERY_NAME =
            "WIPSDatabase.DB2.SQLQueries.retrieveGsaAuditValues";

    @Inject
    private Database database;

    public List<GsaAudit> retrieveLumpsumGsaAudit() throws WipsDb2DatabaseException {
        final String gsaAuditValuesQuery =
                PropertyManager.getInstance().getString(GSA_AUDIT_VALUES_QUERY_NAME);
        return this.database.query(gsaAuditValuesQuery, gsaAuditMapper());
    }

    private Mapper<GsaAudit> gsaAuditMapper() {
        return new Mapper<GsaAudit>() {
            @Override
            public GsaAudit map(final ResultSet resultSet) throws SQLException {
                return toGsaAudit(resultSet);
            }
        };
    }

    private GsaAudit toGsaAudit(final ResultSet resultSet) throws SQLException {
        final String code =
                StringUtils.trim(resultSet.getString(DatabaseConstants.GSA_AUDIT_CODE));
        final String description =
                StringUtils.trim(resultSet.getString(DatabaseConstants.GSA_AUDIT_DESC));
        return new GsaAudit(code, description);
    }
}
