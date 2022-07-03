package com.tian.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetHandler<T> {
    T handle(ResultSet paramResultSet) throws SQLException;
}
