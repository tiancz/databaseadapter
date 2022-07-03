package com.tian.db.handler;

import com.tian.db.ResultSetHandler;
import com.tian.db.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BeanHandler<T> implements ResultSetHandler<T> {
    private Class<T> type;

    public BeanHandler(Class<T> type) {
        this.type = type;
    }

    public T handle(ResultSet rs) throws SQLException {
        return rs.next() ? (T) RowProcessor.toBean(rs, this.type) : null;
    }
}
