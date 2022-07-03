package com.tian.db.handler;

import com.tian.db.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BeanListHandler<T> extends AbstractListHandler<T> {
    private Class<T> type;

    public BeanListHandler(Class<T> type) {
        this.type = type;
    }

    protected T getRow(ResultSet rs) throws SQLException {
        return (T) RowProcessor.toBean(rs, this.type);
    }
}
