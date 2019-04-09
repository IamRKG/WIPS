//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.domain.resource;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Factory class creates the data source and returns the appropriate data source to the
 * client.
 */
public class WipsDataSourceFactory {

    @Resource(name = "WIPS_DB2")
    private DataSource wipsDb2DataSource;

    private WipsDataSourceFactory() {
    }

    /**
     * Returns the DB2 DataSource
     */
    public DataSource getDb2DataSource() {
        return this.wipsDb2DataSource;
    }

}
