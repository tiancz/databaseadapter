package com.tian.db.handler;

import com.tian.db.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractListHandler<T> implements ResultSetHandler<List<T>> {

    protected abstract T getRow(ResultSet paramResultSet) throws SQLException;

    public List<T> handle(ResultSet rs) throws SQLException {
        List<T> rows = new ArrayList<>();
        while (rs.next())
            rows.add(getRow(rs));
        return rows;
    }
}