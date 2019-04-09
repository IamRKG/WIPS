//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.resource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.ford.it.exception.FordExceptionAttributes;
import com.ford.it.logging.ILogger;
import com.ford.it.logging.LogFactory;
import com.ford.it.properties.PropertyManager;
import com.ford.purchasing.wips.common.layer.Mapper;
import com.ford.purchasing.wips.common.layer.WipsConstant;
import com.ford.purchasing.wips.domain.layer.exception.WipsDb2DatabaseException;

@SuppressWarnings("javadoc")
public class Database {

    private static final String CLASS_NAME = Database.class.getName();
    private static ILogger log = LogFactory.getInstance().getLogger(CLASS_NAME);

    private static String userId = PropertyManager.getInstance().getString(
            WipsConstant.WIPS_PROXY_USERNAME);

    private static String passWord = PropertyManager.getInstance().getString(
            WipsConstant.WIPS_PROXY_PWD);

    @Inject
    private WipsDataSourceFactory wipsDataSourceFactory;

    public <T> List<T> query(final String sql, final Mapper<T> mapper,
            final String... params) throws WipsDb2DatabaseException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        final List<T> items = new ArrayList<T>();
        try {
            connection = createConnection();
            statement = createStatement(sql, connection, params);
            resultSet = statement.executeQuery();
            if (resultSet != null) {
                while (resultSet.next()) {
                    final T item = mapper.map(resultSet);
                    items.add(item);
                }
            }
        } catch (final SQLException sqlException) {
            throw new WipsDb2DatabaseException(new FordExceptionAttributes.Builder(
                    CLASS_NAME, "query").build(), sqlException.getMessage(),
                    sqlException);
        } finally {
            closeConnection(resultSet, statement, connection);
        }
        return items;
    }

    public int update(final String sql,
            final String... params) throws WipsDb2DatabaseException {
        Connection connection = null;
        PreparedStatement statement = null;
        final ResultSet resultSet = null;
        int status = 0;
        try {
            connection = createConnection();
            statement = createStatement(sql, connection, params);
            status = statement.executeUpdate();

        } catch (final SQLException sqlException) {
            throw new WipsDb2DatabaseException(new FordExceptionAttributes.Builder(
                    CLASS_NAME, "query").build(), sqlException.getMessage(),
                    sqlException);
        } finally {
            closeConnection(resultSet, statement, connection);
        }
        return status;
    }

    private Connection createConnection() throws SQLException {
        return this.wipsDataSourceFactory.getDb2DataSource().getConnection(userId,
                passWord);
    }

    private void closeConnection(final ResultSet resultSet, final Statement statement,
            final Connection connection) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (final SQLException sqlException) {
            log.throwing(CLASS_NAME, "closeConnection", sqlException);
        }
    }

    private PreparedStatement createStatement(final String sql, final Connection connection,
            final String... params) throws SQLException {
        final PreparedStatement statement = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            statement.setString(i + 1, params[i]);
        }
        return statement;
    }
}
