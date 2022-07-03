package com.tian.db;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlExecutor {
    private Connection conn;

    private boolean commit = true;

    public SqlExecutor(Connection conn) {
        this.conn = conn;
    }

    public SqlExecutor(DataSource ds) {
        try {
            this.conn = ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void beginTransaction() {
        try {
            this.conn.setAutoCommit(false);
            this.commit = false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public <T> T executeQuery(String sql, ResultSetHandler<T> handler, Object... params) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        T t = null;
        try {
            ps = this.conn.prepareStatement(sql);
            setParameters(ps, params);
            rs = ps.executeQuery();
            t = handler.handle(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(ps);
            close(this.conn);
        }
        return t;
    }

    public int executeUpdate(String sql, Object... params) {
        PreparedStatement ps = null;
        int i = 0;
        try {
            ps = this.conn.prepareStatement(sql);
            setParameters(ps, params);
            i = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(ps);
            close(this.conn);
        }
        return i;
    }

    public int[] executeBatch(String sql, Object[][] params) throws SQLException {
        PreparedStatement ps = null;
        int[] rows = null;
        try {
            ps = this.conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                setParameters(ps, params[i]);
                ps.addBatch();
            }
            rows = ps.executeBatch();
        } catch (SQLException e) {
            throw e;
        } finally {
            close(ps);
            close(this.conn);
        }
        return rows;
    }

    private void setParameters(PreparedStatement ps, Object[] parameters) throws SQLException {
        for (int i = 1; i <= parameters.length; i++)
            ps.setObject(i, parameters[i - 1]);
    }

    public void commit() {
        try {
            this.conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.commit = true;
            close(this.conn);
        }
    }

    public void rollback() {
        try {
            this.conn.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(this.conn);
        }
    }

    private void close(ResultSet rs) {
        if (rs != null)
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    private void close(Statement st) {
        if (st != null)
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    private void close(Connection conn) {
        if (this.commit && conn != null)
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
}
