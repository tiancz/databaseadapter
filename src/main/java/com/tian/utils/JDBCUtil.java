package com.tian.utils;

import com.sun.rowset.CachedRowSetImpl;

import javax.sql.rowset.CachedRowSet;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.List;

public class JDBCUtil {
    public static String makePix(int num) {
        String s = "";
        for (int i = 0; i < num; i++)
            s = s + "?,";
        if (num > 0)
            return s.substring(0, s.length() - 1);
        return s;
    }

    public static void setStmtPara(PreparedStatement stmt, int i, Object para) throws SQLException {
        if (para == null) {
            stmt.setObject(i, (Object)null);
        } else if (para instanceof Integer) {
            stmt.setInt(i, ((Integer)para).intValue());
        } else if (para instanceof Double) {
            stmt.setDouble(i, ((Double)para).doubleValue());
        } else if (para instanceof InputStream) {
            stmt.setBinaryStream(i, (InputStream)para);
        } else if (para instanceof Boolean) {
            stmt.setBoolean(i, ((Boolean)para).booleanValue());
        } else if (para instanceof Long) {
            stmt.setLong(i, ((Long)para).longValue());
        } else if (para instanceof Float) {
            stmt.setFloat(i, ((Float)para).floatValue());
        } else if (para instanceof Byte) {
            stmt.setByte(i, ((Byte)para).byteValue());
        } else if (para instanceof Short) {
            stmt.setShort(i, ((Short)para).shortValue());
        } else if (para instanceof Date) {
            stmt.setDate(i, (Date)para);
        } else if (para instanceof Date) {
            stmt.setDate(i, DateUtil.toSqlDate((Date)para));
        } else if (para instanceof BigDecimal) {
            stmt.setLong(i, ((BigDecimal)para).longValue());
        } else if (para instanceof Blob) {
            stmt.setBlob(i, (Blob)para);
        } else if (para instanceof Clob) {
            stmt.setClob(i, (Clob)para);
        } else if (para instanceof String) {
            stmt.setString(i, (String)para);
        } else {
            try {
                stmt.setString(i, (String)para);
            } catch (Exception e) {
                stmt.setObject(i, para);
            }
        }
    }

    public static ResultSet queryData(Connection conn, String sql, Object[] paras) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(sql);
        if (paras != null)
            for (int i = 1; i <= paras.length; i++)
                setStmtPara(stmt, i, paras[i - 1]);
        ResultSet rs = stmt.executeQuery();
        ResultSet localResultSet = rs;
        try {
            CachedRowSet localCachedRowSet = new CachedRowSetImpl();
            localCachedRowSet.populate(localResultSet);
            return localCachedRowSet;
        } finally {
            close(localResultSet);
            close(stmt);
        }
    }

    public static int executeUpdate(Connection conn, String sql, Object[] paras) throws SQLException {
        if (sql != null)
            sql = sql.toLowerCase();
        PreparedStatement stmt = conn.prepareStatement(sql);
        if (paras != null)
            for (int i = 1; i <= paras.length; i++)
                setStmtPara(stmt, i, paras[i - 1]);
        int result = -1;
        try {
            result = stmt.executeUpdate();
        } catch (Exception e) {
            throw new SQLException(e);
        } finally {
            close(stmt);
        }
        return result;
    }

    public static int[] executeUpdateBatch(Connection conn, String sql, List<Object[]> paras) throws SQLException {
        int[] result = null;
        if (sql != null)
            sql = sql.toLowerCase();
        PreparedStatement stmt = conn.prepareStatement(sql);
        if (paras != null)
            for (Object[] para : paras) {
                for (int i = 1; i <= para.length; i++)
                    setStmtPara(stmt, i, para[i - 1]);
                stmt.addBatch();
            }
        try {
            result = stmt.executeBatch();
        } catch (Exception e) {
            throw new SQLException(e);
        } finally {
            close(stmt);
        }
        return result;
    }

    public static void executeProduce(Connection conn, String produceName, Object[] paras) throws SQLException {
        String sql = " call " + produceName + "(" + makePix(paras.length) + ") ";
        PreparedStatement stmt = conn.prepareStatement(sql);
        for (int i = 1; i <= paras.length; i++)
            setStmtPara(stmt, i, paras[i - 1]);
        try {
            stmt.execute();
        } catch (Exception e) {
            throw new SQLException(e);
        } finally {
            close(stmt);
        }
    }

    public static void close(ResultSet rs) throws SQLException {
        rs.close();
    }

    public static void close(PreparedStatement rs) throws SQLException {
        rs.close();
    }

    public static void close(Connection rs) throws SQLException {
        if (rs != null && rs.getAutoCommit())
            rs.close();
    }
}
