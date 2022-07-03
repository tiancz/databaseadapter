package com.tian.db.handler;

import com.tian.db.ResultSetHandler;
import com.tian.db.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class MapHandler implements ResultSetHandler<Map<String, Object>> {
    public Map<String, Object> handle(ResultSet rs) throws SQLException {
        return rs.next() ? RowProcessor.toMap(rs) : null;
    }
}
