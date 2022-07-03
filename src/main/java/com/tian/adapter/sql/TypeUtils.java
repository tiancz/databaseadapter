package com.tian.adapter.sql;

import com.tian.adapter.exception.DialectException;
import com.tian.adapter.meta.scheme.Column;
import com.tian.adapter.meta.scheme.Type;
import com.tian.enums.FieldTypeEnum;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class TypeUtils {
    public static final String BIGINT = "BIGINT";

    public static final String BINARY = "BINARY";

    public static final String BIT = "BIT";

    public static final String BLOB = "BLOB";

    public static final String BOOLEAN = "BOOLEAN";

    public static final String CHAR = "CHAR";

    public static final String CLOB = "CLOB";

    public static final String DATE = "DATE";

    public static final String DECIMAL = "DECIMAL";

    public static final String DOUBLE = "DOUBLE";

    public static final String FLOAT = "FLOAT";

    public static final String INTEGER = "INTEGER";

    public static final String JAVA_OBJECT = "JAVA_OBJECT";

    public static final String LONGNVARCHAR = "LONGNVARCHAR";

    public static final String LONGVARBINARY = "LONGVARBINARY";

    public static final String LONGVARCHAR = "LONGVARCHAR";

    public static final String NCHAR = "NCHAR";

    public static final String NCLOB = "NCLOB";

    public static final String NUMERIC = "NUMERIC";

    public static final String NVARCHAR = "NVARCHAR";

    public static final String OTHER = "OTHER";

    public static final String REAL = "REAL";

    public static final String SMALLINT = "SMALLINT";

    public static final String TIME = "TIME";

    public static final String TIMESTAMP = "TIMESTAMP";

    public static final String TINYINT = "TINYINT";

    public static final String VARBINARY = "VARBINARY";

    public static final String VARCHAR = "VARCHAR";

    private static final Map<Class<?>, Type> SQL_MAP_ABLE_TYPES = new HashMap<>();

    static {
        SQL_MAP_ABLE_TYPES.put(BigDecimal.class, Type.NUMERIC);
        SQL_MAP_ABLE_TYPES.put(BigInteger.class, Type.BIGINT);
        SQL_MAP_ABLE_TYPES.put(Boolean.class, Type.BOOLEAN);
        SQL_MAP_ABLE_TYPES.put(Byte.class, Type.TINYINT);
        SQL_MAP_ABLE_TYPES.put(Character.class, Type.CHAR);
        SQL_MAP_ABLE_TYPES.put(Date.class, Type.DATE);
        SQL_MAP_ABLE_TYPES.put(Date.class, Type.DATE);
        SQL_MAP_ABLE_TYPES.put(Time.class, Type.TIME);
        SQL_MAP_ABLE_TYPES.put(Timestamp.class, Type.TIMESTAMP);
        SQL_MAP_ABLE_TYPES.put(Clob.class, Type.CLOB);
        SQL_MAP_ABLE_TYPES.put(Blob.class, Type.BLOB);
        SQL_MAP_ABLE_TYPES.put(Double.class, Type.DOUBLE);
        SQL_MAP_ABLE_TYPES.put(Float.class, Type.FLOAT);
        SQL_MAP_ABLE_TYPES.put(Integer.class, Type.INTEGER);
        SQL_MAP_ABLE_TYPES.put(Long.class, Type.BIGINT);
        SQL_MAP_ABLE_TYPES.put(Short.class, Type.SMALLINT);
        SQL_MAP_ABLE_TYPES.put(String.class, Type.VARCHAR);
    }

    public static boolean canMapToSqlType(Class<?> clazz) {
        if (clazz == null)
            return false;
        return SQL_MAP_ABLE_TYPES.containsKey(clazz);
    }

    public static Type toType(Class<?> clazz) {
        Type t = SQL_MAP_ABLE_TYPES.get(clazz);
        if (t == null)
            return Type.OTHER;
        return t;
    }

    public static FieldTypeEnum toFieldType(String columnDef) {
        Type type = toType(columnDef);
        return FieldTypeEnum.getFieldTypeEnum(type);
    }

    public static Type toType(String columnDef) {
        if ("BIGINT".equalsIgnoreCase(columnDef))
            return Type.BIGINT;
        if ("BINARY".equalsIgnoreCase(columnDef))
            return Type.BINARY;
        if ("BIT".equalsIgnoreCase(columnDef))
            return Type.BIT;
        if ("BLOB".equalsIgnoreCase(columnDef))
            return Type.BLOB;
        if ("BOOLEAN".equalsIgnoreCase(columnDef))
            return Type.BOOLEAN;
        if ("CHAR".equalsIgnoreCase(columnDef))
            return Type.CHAR;
        if ("CLOB".equalsIgnoreCase(columnDef))
            return Type.CLOB;
        if ("DATE".equalsIgnoreCase(columnDef) || "DATETIME".equalsIgnoreCase(columnDef))
            return Type.DATE;
        if ("DECIMAL".equalsIgnoreCase(columnDef) || "DOUBLE".equalsIgnoreCase(columnDef) || "FLOAT".equalsIgnoreCase(columnDef))
            return Type.NUMERIC;
        if ("INTEGER".equalsIgnoreCase(columnDef) || "INT".equalsIgnoreCase(columnDef))
            return Type.INTEGER;
        if ("JAVA_OBJECT".equalsIgnoreCase(columnDef))
            return Type.JAVA_OBJECT;
        if ("LONGNVARCHAR".equalsIgnoreCase(columnDef))
            return Type.LONGNVARCHAR;
        if ("LONGVARBINARY".equalsIgnoreCase(columnDef))
            return Type.LONGVARBINARY;
        if ("LONGVARCHAR".equalsIgnoreCase(columnDef))
            return Type.LONGVARCHAR;
        if ("NCHAR".equalsIgnoreCase(columnDef))
            return Type.NCHAR;
        if ("NCLOB".equalsIgnoreCase(columnDef))
            return Type.NCLOB;
        if ("NUMERIC".equalsIgnoreCase(columnDef))
            return Type.NUMERIC;
        if ("NVARCHAR".equalsIgnoreCase(columnDef))
            return Type.NVARCHAR;
        if ("OTHER".equalsIgnoreCase(columnDef))
            return Type.OTHER;
        if ("REAL".equalsIgnoreCase(columnDef))
            return Type.REAL;
        if ("SMALLINT".equalsIgnoreCase(columnDef))
            return Type.SMALLINT;
        if ("TIME".equalsIgnoreCase(columnDef))
            return Type.TIME;
        if ("TIMESTAMP".equalsIgnoreCase(columnDef))
            return Type.TIMESTAMP;
        if ("TINYINT".equalsIgnoreCase(columnDef))
            return Type.TINYINT;
        if ("VARBINARY".equalsIgnoreCase(columnDef))
            return Type.VARBINARY;
        if ("VARCHAR".equalsIgnoreCase(columnDef))
            return Type.VARCHAR;
        if ("VARCHAR2".equalsIgnoreCase(columnDef))
            return Type.VARCHAR;
        if ("FLOAT4".equalsIgnoreCase(columnDef))
            return Type.NUMERIC;
        if ("FLOAT8".equalsIgnoreCase(columnDef))
            return Type.NUMERIC;
        if ("INT4".equalsIgnoreCase(columnDef))
            return Type.INTEGER;
        if ("INT8".equalsIgnoreCase(columnDef))
            return Type.INTEGER;
        throw new DialectException("'" + columnDef + "' is not a legal SQL column definition name");
    }

    public static Type javaSqlTypeToDialectType(int javaSqlType) {
        switch (javaSqlType) {
            case -7:
                return Type.BIT;
            case -6:
                return Type.TINYINT;
            case 5:
                return Type.SMALLINT;
            case 4:
                return Type.INTEGER;
            case -5:
                return Type.BIGINT;
            case 6:
                return Type.FLOAT;
            case 7:
                return Type.REAL;
            case 8:
                return Type.DOUBLE;
            case 2:
                return Type.NUMERIC;
            case 3:
                return Type.DECIMAL;
            case 1:
                return Type.CHAR;
            case 12:
                return Type.VARCHAR;
            case -1:
                return Type.LONGVARCHAR;
            case 91:
                return Type.DATE;
            case 92:
                return Type.TIME;
            case 93:
                return Type.TIMESTAMP;
            case -2:
                return Type.BINARY;
            case -3:
                return Type.VARBINARY;
            case -4:
                return Type.LONGVARBINARY;
            case 1111:
                return Type.OTHER;
            case 2000:
                return Type.JAVA_OBJECT;
            case 2004:
                return Type.BLOB;
            case 2005:
                return Type.CLOB;
            case 16:
                return Type.BOOLEAN;
            case -15:
                return Type.NCHAR;
            case -9:
                return Type.NVARCHAR;
            case -16:
                return Type.LONGNVARCHAR;
            case 2011:
                return Type.NCLOB;
        }
        throw new DialectException("Not supported java.sql.Types value:" + javaSqlType);
    }

    public static Column instanceColumn(String columnName, String columnType, Integer columnLength) {
        switch (toType(columnType)) {
            case BIGINT:
                return (new Column(columnName)).BIGINT(columnLength);
            case INTEGER:
                return (new Column(columnName)).INTEGER(columnLength);
            case VARCHAR:
                return (new Column(columnName)).STRING(columnLength);
            case DOUBLE:
                return (new Column(columnName)).DOUBLE();
        }
        return (new Column(columnName)).STRING(columnLength);
    }
}
