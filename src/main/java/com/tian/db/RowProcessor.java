package com.tian.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class RowProcessor {
    public static <T> T toColumnValue(ResultSet rs, int columnIndex, Class<?> type) throws SQLException {
        Object value = rs.getObject(columnIndex);
        if (value != null) {
            String columnName = rs.getMetaData().getColumnLabel(columnIndex);
            value = BeanUtil.processValue(rs, columnName, type);
            value = BeanUtil.processDate(value, type);
        }
        return (T)value;
    }

    public static <T> T toBean(ResultSet rs, Class<T> type) throws SQLException {
        return (T)BeanUtil.createBean(rs, type);
    }

    public static Object[] toArray(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        Object[] result = new Object[meta.getColumnCount()];
        for (int i = 0; i < result.length; i++)
            result[i] = rs.getObject(i + 1);
        return result;
    }

    public static Map<String, Object> toMap(ResultSet rs) throws SQLException {
        Map<String, Object> result = new HashMap<>();
        ResultSetMetaData metaData = rs.getMetaData();
        for (int i = 1; i <= metaData.getColumnCount(); i++)
            result.put(metaData.getColumnLabel(i), rs.getObject(i));
        return result;
    }
}
