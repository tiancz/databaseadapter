package com.tian.db.handler;

import com.tian.db.ResultSetHandler;
import com.tian.db.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ColumnHandler<T> implements ResultSetHandler<T> {
    private int columnIndex;

    private Class<T> type;

    public ColumnHandler(int columnIndex, Class<T> type) {
        this.columnIndex = columnIndex;
        this.type = type;
    }

    public T handle(ResultSet rs) throws SQLException {
        return rs.next() ? (T) RowProcessor.toColumnValue(rs, this.columnIndex, this.type) : null;
    }
}
