package com.tian.db.handler;

import com.tian.db.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class MapListHandler extends AbstractListHandler<Map<String, Object>> {
    protected Map<String, Object> getRow(ResultSet rs) throws SQLException {
        return RowProcessor.toMap(rs);
    }
}
