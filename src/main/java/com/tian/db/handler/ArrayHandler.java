package com.tian.db.handler;

import com.tian.db.ResultSetHandler;
import com.tian.db.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ArrayHandler implements ResultSetHandler<Object[]> {

    public Object[] handle(ResultSet rs) throws SQLException {

        return rs.next() ? RowProcessor.toArray(rs) : null;

    }
}