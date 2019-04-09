//****************************************************************
//* Copyright (c) 2016 Ford Motor Company. All Rights Reserved.
//****************************************************************
package com.ford.purchasing.wips.common.layer;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Mapper<T> {

    public T map(ResultSet resultSet) throws SQLException;

}
