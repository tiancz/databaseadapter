package com.tian.db.handler;

import com.tian.db.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ArrayListHandler extends AbstractListHandler<Object[]> {
    protected Object[] getRow(ResultSet rs) throws SQLException {
        return RowProcessor.toArray(rs);
    }
}
