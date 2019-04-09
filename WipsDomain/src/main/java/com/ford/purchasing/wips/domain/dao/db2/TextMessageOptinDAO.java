package com.ford.purchasing.wips.domain.dao.db2;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.ford.it.properties.PropertyManager;
import com.ford.purchasing.wips.common.layer.Mapper;
import com.ford.purchasing.wips.common.layer.TextMessageOptinResponse;
import com.ford.purchasing.wips.domain.dao.DatabaseConstants;
import com.ford.purchasing.wips.domain.layer.exception.WipsDb2DatabaseException;
import com.ford.purchasing.wips.domain.resource.Database;

public class TextMessageOptinDAO {
    private static final String NAME_NOT_FOUND = "Name Not Found";
    private static final String RETRIEVE_CDSID_AND_OPTINOPTOUT_QUERY =
            "WIPSDatabase.DB2.SQLQueries.retrieveCdsidAndOptinField";
    private static final String SAVE_OPTIN_OPTOUT_QUERY =
            "WIPSDatabase.DB2.SQLQueries.updateOptinField";
    @Inject
    private Database database;

    public TextMessageOptinResponse retrieveCdsIdAndOptinOptout(final String racfId)
            throws WipsDb2DatabaseException {
        final String sqlQuery =
                PropertyManager.getInstance().getString(RETRIEVE_CDSID_AND_OPTINOPTOUT_QUERY);
        final List<TextMessageOptinResponse> resultsList =
                this.database.query(sqlQuery, new Mapper<TextMessageOptinResponse>() {
                    @Override
                    public TextMessageOptinResponse map(final ResultSet resultSet)
                            throws SQLException {
                        final TextMessageOptinResponse response =
                                new TextMessageOptinResponse();
                        response.setCdsId(StringUtils
                                .trim(resultSet.getString(DatabaseConstants.CDSID)));
                        response.setCdsName(StringUtils
                                .trim(resultSet.getString(DatabaseConstants.CDS_NAME)));
                        response.setOptedForSms((StringUtils.trim(resultSet.getString(
                                DatabaseConstants.SMS_OPTIN_OPTOUT)).equals("")
                                                 || (StringUtils.trim(resultSet.getString(
                                                         DatabaseConstants.SMS_OPTIN_OPTOUT)) == null)) ? "N"
                                                                                                        : StringUtils
                                                                                                                .trim(resultSet
                                                                                                                        .getString(
                                                                                                                                DatabaseConstants.SMS_OPTIN_OPTOUT)));
                        return response;
                    }
                }, racfId);
        return resultsList.get(0);
    }

    public int updateOptinOptout(final String updateValue, final String racfId)
            throws WipsDb2DatabaseException {
        final String sqlQuery =
                PropertyManager.getInstance().getString(SAVE_OPTIN_OPTOUT_QUERY);
        final int resultsList =
                this.database.update(sqlQuery, updateValue, racfId);
        return resultsList;
    }
}
