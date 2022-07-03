package com.tian.db.handler;

import com.tian.db.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ColumnListHandler<T> extends AbstractListHandler<T> {
    private int columnIndex;

    private Class<T> type;

    public ColumnListHandler(int columnIndex, Class<T> type) {
        this.columnIndex = columnIndex;
        this.type = type;
    }

    protected T getRow(ResultSet rs) throws SQLException {
        return (T) RowProcessor.toColumnValue(rs, this.columnIndex, this.type);
    }
}
