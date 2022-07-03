package com.tian.adapter.meta.scheme;

import com.tian.adapter.exception.DialectException;
import com.tian.adapter.utils.StrUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Column  implements Serializable {
    private static final long serialVersionUID = 7510687871404901852L;

    private String columnName;

    private Type columnType;

    private int type;

    private String typeName;

    private String defaultValue;

    private TableModel tableModel;

    private Boolean pkey = Boolean.valueOf(false);

    private Boolean nullable = Boolean.valueOf(true);

    private String check;

    private String tail;

    private String comment;

    private int length = 255;

    private int precision = 0;

    private int scale = 0;

    private Integer[] lengths = new Integer[0];

    private String entityField;

    private Boolean insertable = Boolean.valueOf(true);

    private Boolean updatable = Boolean.valueOf(true);

    private Boolean transientable = Boolean.valueOf(false);

    public Column() {}

    public Column(String columnName) {
        if (StrUtils.isEmpty(columnName))
            DialectException.throwEX("columnName is not allowed empty");
        this.columnName = columnName;
    }

    public Column notNull() {
        this.nullable = Boolean.valueOf(false);
        return this;
    }

    public Column check(String check) {
        this.check = check;
        return this;
    }

    public Column newCopy() {
        Column col = new Column(this.columnName);
        col.columnType = this.columnType;
        col.pkey = Boolean.valueOf(this.pkey.booleanValue());
        col.nullable = this.nullable;
        col.check = this.check;
        col.defaultValue = this.defaultValue;
        col.tail = this.tail;
        col.comment = this.comment;
        col.lengths = this.lengths;
        col.entityField = this.entityField;
        col.length = this.length;
        col.precision = this.precision;
        col.scale = this.scale;
        col.insertable = this.insertable;
        col.updatable = this.updatable;
        col.transientable = this.transientable;
        return col;
    }

    public Column singleIndex(String indexName) {
        makeSureTableModelExist();
        DialectException.assureNotEmpty(indexName, new String[] { "indexName can not be empty" });
        this.tableModel.index(indexName).columns(new String[] { getColumnName() });
        return this;
    }

    public Column singleIndex() {
        makeSureTableModelExist();
        this.tableModel.index().columns(new String[] { getColumnName() });
        return this;
    }

    public Column singleUnique(String uniqueName) {
        makeSureTableModelExist();
        DialectException.assureNotEmpty(uniqueName, new String[] { "indexName can not be empty" });
        this.tableModel.unique(uniqueName).columns(new String[] { getColumnName() });
        return this;
    }

    public Column singleUnique() {
        makeSureTableModelExist();
        this.tableModel.unique().columns(new String[] { getColumnName() });
        return this;
    }

    private void makeSureTableModelExist() {
        DialectException.assureNotNull(this.tableModel, new String[] { "Column should belong to a TableModel, please call tableModel.column() method first." });
    }

    public Column defaultValue(String value) {
        this.defaultValue = value;
        return this;
    }

    public Column comment(String comment) {
        this.comment = comment;
        return this;
    }

    public Column pkey() {
        this.pkey = Boolean.valueOf(true);
        return this;
    }

    public Column id() {
        this.pkey = Boolean.valueOf(true);
        return this;
    }

    public Column tail(String tail) {
        this.tail = tail;
        return this;
    }

    public Column entityField(String entityFieldName) {
        DialectException.assureNotEmpty(entityFieldName, new String[] { "entityFieldName can not be empty" });
        this.entityField = entityFieldName;
        if (this.tableModel != null) {
            List<Column> oldColumns = this.tableModel.getColumns();
            Iterator<Column> columnIter = oldColumns.iterator();
            while (columnIter.hasNext()) {
                Column column = columnIter.next();
                if (entityFieldName.equals(column.getEntityField()) && !getColumnName().equals(column.getColumnName()))
                    columnIter.remove();
            }
        }
        return this;
    }

    public Column insertable(Boolean insertable) {
        this.insertable = insertable;
        return this;
    }

    public Column updatable(Boolean updatable) {
        this.updatable = updatable;
        return this;
    }

    public Column LONG(Integer length) {
        this.columnType = Type.BIGINT;
        this.lengths = new Integer[] { length };
        return this;
    }

    public Column BOOLEAN() {
        this.columnType = Type.BOOLEAN;
        return this;
    }

    public Column DOUBLE() {
        this.columnType = Type.DOUBLE;
        return this;
    }

    public Column FLOAT(Integer... lengths) {
        this.columnType = Type.FLOAT;
        this.lengths = lengths;
        return this;
    }

    public Column INTEGER(Integer length) {
        this.columnType = Type.INTEGER;
        this.lengths = new Integer[] { length };
        return this;
    }

    public Column SHORT(Integer length) {
        this.columnType = Type.SMALLINT;
        this.lengths = new Integer[] { length };
        return this;
    }

    public Column BIGDECIMAL(Integer precision, Integer scale) {
        this.columnType = Type.NUMERIC;
        this.lengths = new Integer[] { precision, scale };
        return this;
    }

    public Column STRING(Integer length) {
        this.columnType = Type.VARCHAR;
        this.lengths = new Integer[] { length };
        return this;
    }

    public Column DATE() {
        this.columnType = Type.DATE;
        return this;
    }

    public Column TIME() {
        this.columnType = Type.TIME;
        return this;
    }

    public Column TIMESTAMP() {
        this.columnType = Type.TIMESTAMP;
        return this;
    }

    public Column BIGINT(Integer length) {
        this.columnType = Type.BIGINT;
        this.lengths = new Integer[] { length };
        return this;
    }

    public Column BINARY(Integer... lengths) {
        this.columnType = Type.BINARY;
        this.lengths = lengths;
        return this;
    }

    public Column BIT() {
        this.columnType = Type.BIT;
        return this;
    }

    public Column BLOB(Integer... lengths) {
        this.columnType = Type.BLOB;
        this.lengths = lengths;
        return this;
    }

    public Column CHAR(Integer... lengths) {
        this.columnType = Type.CHAR;
        this.lengths = lengths;
        return this;
    }

    public Column CLOB(Integer... lengths) {
        this.columnType = Type.CLOB;
        this.lengths = lengths;
        return this;
    }

    public Column DECIMAL(Integer... lengths) {
        this.columnType = Type.DECIMAL;
        this.lengths = lengths;
        return this;
    }

    public Column JAVA_OBJECT() {
        this.columnType = Type.JAVA_OBJECT;
        return this;
    }

    public Column LONGNVARCHAR(Integer length) {
        this.columnType = Type.LONGNVARCHAR;
        this.lengths = new Integer[] { length };
        return this;
    }

    public Column LONGVARBINARY(Integer... lengths) {
        this.columnType = Type.LONGVARBINARY;
        this.lengths = lengths;
        return this;
    }

    public Column LONGVARCHAR(Integer... lengths) {
        this.columnType = Type.LONGVARCHAR;
        this.lengths = lengths;
        return this;
    }

    public Column NCHAR(Integer length) {
        this.columnType = Type.NCHAR;
        this.lengths = new Integer[] { length };
        return this;
    }

    public Column NCLOB() {
        this.columnType = Type.NCLOB;
        return this;
    }

    public Column NUMERIC(Integer... lengths) {
        this.columnType = Type.NUMERIC;
        this.lengths = lengths;
        return this;
    }

    public Column NVARCHAR(Integer length) {
        this.columnType = Type.NVARCHAR;
        this.lengths = new Integer[] { length };
        return this;
    }

    public Column OTHER(Integer... lengths) {
        this.columnType = Type.OTHER;
        this.lengths = lengths;
        return this;
    }

    public Column REAL() {
        this.columnType = Type.REAL;
        return this;
    }

    public Column SMALLINT(Integer length) {
        this.columnType = Type.SMALLINT;
        this.lengths = new Integer[] { length };
        return this;
    }

    public Column TINYINT(Integer length) {
        this.columnType = Type.TINYINT;
        this.lengths = new Integer[] { length };
        return this;
    }

    public Column VARBINARY(Integer... lengths) {
        this.columnType = Type.VARBINARY;
        this.lengths = lengths;
        return this;
    }

    public Column VARCHAR(Integer length) {
        this.columnType = Type.VARCHAR;
        this.lengths = new Integer[] { length };
        return this;
    }

    public String getColumnName() {
        return this.columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public TableModel getTableModel() {
        return this.tableModel;
    }

    public void setTableModel(TableModel tableModel) {
        this.tableModel = tableModel;
    }

    public Type getColumnType() {
        return this.columnType;
    }

    public void setColumnType(Type columnType) {
        this.columnType = columnType;
    }

    public Boolean getPkey() {
        return this.pkey;
    }

    public void setPkey(Boolean pkey) {
        this.pkey = pkey;
    }

    public Boolean getNullable() {
        return this.nullable;
    }

    public void setNullable(Boolean nullable) {
        this.nullable = nullable;
    }

    public String getCheck() {
        return this.check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getTail() {
        return this.tail;
    }

    public void setTail(String tail) {
        this.tail = tail;
    }

    public Integer[] getLengths() {
        return this.lengths;
    }

    public void setLengths(Integer[] lengths) {
        this.lengths = lengths;
    }

    public String getEntityField() {
        return this.entityField;
    }

    public void setEntityField(String entityField) {
        this.entityField = entityField;
    }

    public Boolean getInsertable() {
        return this.insertable;
    }

    public void setInsertable(Boolean insertable) {
        this.insertable = insertable;
    }

    public Boolean getUpdatable() {
        return this.updatable;
    }

    public void setUpdatable(Boolean updatable) {
        this.updatable = updatable;
    }

    public Boolean getTransientable() {
        return this.transientable;
    }

    public void setTransientable(Boolean transientable) {
        this.transientable = transientable;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isNullable() {
        return this.nullable.booleanValue();
    }

    public void setNullable(boolean nullable) {
        this.nullable = Boolean.valueOf(nullable);
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getPrecision() {
        return this.precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public int getScale() {
        return this.scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String toString() {
        return "Column{columnName='" + this.columnName + '\'' + ", columnType=" + this.columnType + ", type=" + this.type + ", typeName='" + this.typeName + '\'' + ", defaultValue='" + this.defaultValue + '\'' + ", tableModel=" + this.tableModel + ", pkey=" + this.pkey + ", nullable=" + this.nullable + ", check='" + this.check + '\'' + ", tail='" + this.tail + '\'' + ", comment='" + this.comment + '\'' + ", length=" + this.length + ", precision=" + this.precision + ", scale=" + this.scale + ", lengths=" +

                Arrays.toString((Object[])this.lengths) + ", entityField='" + this.entityField + '\'' + ", insertable=" + this.insertable + ", updatable=" + this.updatable + ", transientable=" + this.transientable + '}';
    }
}
