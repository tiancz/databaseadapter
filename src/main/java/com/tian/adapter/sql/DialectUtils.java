package com.tian.adapter.sql;

import com.tian.adapter.exception.DialectException;
import com.tian.adapter.meta.scheme.Type;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DialectUtils {
    private static final Map<DataSource, Dialect> dataSourceDialectCache = new ConcurrentHashMap<>();

    public static Dialect guessDialect(Connection jdbcConnection) {
        String databaseName;
        int majorVersion;
        int minorVersion;
        try {
            DatabaseMetaData meta = jdbcConnection.getMetaData();
            databaseName = meta.getDatabaseProductName();
            majorVersion = meta.getDatabaseMajorVersion();
            minorVersion = meta.getDatabaseMinorVersion();
        } catch (SQLException e) {
            return (Dialect) DialectException.throwEX(e);
        }
        return guessDialect(databaseName, new Object[] { Integer.valueOf(majorVersion), Integer.valueOf(minorVersion) });
    }

    public static Dialect guessDialect(DataSource dataSource) {
        Dialect result = dataSourceDialectCache.get(dataSource);
        if (result != null)
            return result;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            result = guessDialect(con);
            if (result == null)
                return
                        (Dialect)DialectException.throwEX("Can not get dialect from DataSource, please submit this bug.");
            dataSourceDialectCache.put(dataSource, result);
            return result;
        } catch (SQLException e) {
            return (Dialect)DialectException.throwEX(e);
        } finally {
            try {
                if (con != null && !con.isClosed())
                    try {
                        con.close();
                    } catch (SQLException e) {
                        DialectException.throwEX(e);
                    }
            } catch (SQLException e) {
                DialectException.throwEX(e);
            }
        }
    }

    public static Dialect guessDialect(String databaseName, Object... majorVersionMinorVersion) {
        if ("MySQL".equalsIgnoreCase(databaseName))
            return Dialect.MySQL;
        if ("Oracle".equalsIgnoreCase(databaseName))
            return Dialect.Oracle;
        if ("PostgreSQL".equalsIgnoreCase(databaseName))
            return Dialect.PostgreSQL;
        return null;
    }

    private static void copyTo(Map<? extends Type, ? extends String> lastMap, Dialect d) {
        Map<Type, String> target = d.typeMappings;
        target.clear();
        target.putAll(lastMap);
    }

    protected static void initTypeMappings() {
        Map<Type, String> mp = new HashMap<>();
        mp.put(Type.BIGINT, "bigint($l)");
        mp.put(Type.BINARY, "binary($l)");
        mp.put(Type.BIT, "bit");
        mp.put(Type.BLOB, "longblob");
        mp.put(Type.BOOLEAN, "bit");
        mp.put(Type.CHAR, "NCHAR(1)");
        mp.put(Type.CLOB, "longtext");
        mp.put(Type.DATE, "date");
        mp.put(Type.DECIMAL, "N/A");
        mp.put(Type.DOUBLE, "double precision");
        mp.put(Type.FLOAT, "float($l)");
        mp.put(Type.INTEGER, "integer($l)");
        mp.put(Type.JAVA_OBJECT, "json");
        mp.put(Type.LONGNVARCHAR, "nvarchar($l)");
        mp.put(Type.LONGVARBINARY, "mediumblob<16777215|longblob");
        mp.put(Type.LONGVARCHAR, "longtext");
        mp.put(Type.NCHAR, "nchar($l)");
        mp.put(Type.NCLOB, "longtext");
        mp.put(Type.NUMERIC, "decimal($p,$s)");
        mp.put(Type.NVARCHAR, "nvarchar($l)");
        mp.put(Type.OTHER, "N/A");
        mp.put(Type.REAL, "real");
        mp.put(Type.SMALLINT, "smallint($l)");
        mp.put(Type.TIME, "time");
        mp.put(Type.TIMESTAMP, "datetime(6)");
        mp.put(Type.TINYINT, "tinyint($l)");
        mp.put(Type.VARBINARY, "tinyblob<255|blob<65535|mediumblob<16777215|longblob");
        mp.put(Type.VARCHAR, "varchar($l)<65535|longtext");
        copyTo(mp, Dialect.MySQL);
        mp.put(Type.DATE, "date");
        mp.put(Type.TIME, "time");
        mp.put(Type.TIMESTAMP, "timestamp");
        mp.put(Type.VARCHAR, "varchar($l)");
        mp.put(Type.BIGINT, "int8");
        mp.put(Type.BINARY, "bytea");
        mp.put(Type.BIT, "bool");
        mp.put(Type.BLOB, "oid");
        mp.put(Type.CLOB, "text");
        mp.put(Type.DOUBLE, "float8");
        mp.put(Type.FLOAT, "float4");
        mp.put(Type.INTEGER, "int4");
        mp.put(Type.LONGVARBINARY, "bytea");
        mp.put(Type.LONGVARCHAR, "text");
        mp.put(Type.NUMERIC, "numeric($p, $s)");
        mp.put(Type.SMALLINT, "int2");
        mp.put(Type.TINYINT, "int2");
        mp.put(Type.VARBINARY, "bytea");
        copyTo(mp, Dialect.PostgreSQL);
        mp.put(Type.JAVA_OBJECT, "N/A");
        mp.put(Type.BIGINT, "number(19,0)");
        mp.put(Type.BIT, "number(1,0)");
        mp.put(Type.BLOB, "blob");
        mp.put(Type.CLOB, "clob");
        mp.put(Type.DECIMAL, "number($p,$s)");
        mp.put(Type.INTEGER, "number(10,0)");
        mp.put(Type.NCLOB, "nclob");
        mp.put(Type.NUMERIC, "number($p,$s)");
        mp.put(Type.SMALLINT, "number(5,0)");
        mp.put(Type.TIME, "date");
        mp.put(Type.TINYINT, "number(3,0)");
        mp.put(Type.VARBINARY, "raw($l)<2000|long raw");
        mp.put(Type.BINARY, "raw($l)<2000|long raw");
        mp.put(Type.BOOLEAN, "number(1,0)");
        mp.put(Type.CHAR, "char(1 char)");
        mp.put(Type.LONGNVARCHAR, "nvarchar2($l)");
        mp.put(Type.LONGVARBINARY, "long raw");
        mp.put(Type.LONGVARCHAR, "long");
        mp.put(Type.NVARCHAR, "nvarchar2($l)");
        mp.put(Type.TIMESTAMP, "timestamp");
        mp.put(Type.VARCHAR, "varchar2($l char)<4000|long");
        copyTo(mp, Dialect.Oracle);
    }

    protected static String initializePaginSQLTemplate(Dialect d) {
        switch (d) {
            case MySQL:
                return "select $BODY limit $SKIP_ROWS, $PAGESIZE";
            case Oracle:
                return "select * from ( select row_.*, rownum rownum_ from ( select $BODY ) row_ where rownum <= $TOTAL_ROWS) where rownum_ > $SKIP_ROWS";
            case PostgreSQL:
                return "select $BODY limit $PAGESIZE offset $SKIP_ROWS";
        }
        return "NOT_SUPPORT";
    }

    protected static String initializeTopLimitSqlTemplate(Dialect d) {
        switch (d) {
            case MySQL:
                return "select $BODY limit $PAGESIZE";
            case Oracle:
                return "select * from ( select $BODY ) where rownum <= $PAGESIZE";
            case PostgreSQL:
                return "select $BODY limit $PAGESIZE";
        }
        return "NOT_SUPPORT";
    }
}
