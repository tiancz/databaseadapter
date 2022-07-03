package com.tian.adapter.meta.scheme;

import com.tian.adapter.exception.DialectException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TableModel {
    private String tableName;

    private String check;

    private String comment;

    private String engineTail;

    private List<Column> columns = new ArrayList<>();

    private List<Index> indexConsts = new ArrayList<>();

    private List<Unique> uniqueConsts = new ArrayList<>();

    private Class<?> entityClass;

    private String alias;

    public TableModel() {}

    public TableModel(String tableName) {
        this.tableName = tableName;
    }

    public TableModel newCopy() {
        TableModel tb = new TableModel();
        tb.tableName = this.tableName;
        tb.check = this.check;
        tb.comment = this.comment;
        tb.engineTail = this.engineTail;
        if (!this.columns.isEmpty())
            for (Column item : this.columns) {
                Column newItem = item.newCopy();
                newItem.setTableModel(tb);
                tb.columns.add(newItem);
            }
        if (!this.indexConsts.isEmpty())
            for (Index item : this.indexConsts)
                tb.indexConsts.add(item.newCopy());
        if (!this.uniqueConsts.isEmpty())
            for (Unique item : this.uniqueConsts)
                tb.uniqueConsts.add(item.newCopy());
        return tb;
    }

    public TableModel check(String check) {
        this.check = check;
        return this;
    }

    public TableModel comment(String comment) {
        this.comment = comment;
        return this;
    }

    public TableModel addColumn(Column column) {
        DialectException.assureNotNull(column, new String[0]);
        DialectException.assureNotEmpty(column.getColumnName(), new String[] { "Column's columnName can not be empty" });
        column.setTableModel(this);
        this.columns.add(column);
        return this;
    }

    public TableModel removeColumn(String columnName) {
        List<Column> oldColumns = getColumns();
        Iterator<Column> columnIter = oldColumns.iterator();
        while (columnIter.hasNext()) {
            if (((Column)columnIter.next()).getColumnName().equalsIgnoreCase(columnName))
                columnIter.remove();
        }
        return this;
    }

    public Column column(String columnName) {
        Column col = getColumn(columnName);
        if (col != null)
            return col;
        return addColumn(columnName);
    }

    public Column addColumn(String columnName) {
        DialectException.assureNotEmpty(columnName, new String[] { "columnName can not be empty" });
        for (Column columnModel : this.columns) {
            if (columnName.equals(columnModel.getColumnName()))
                throw new DialectException("Column name '" + columnName + "' already existed");
        }
        Column column = new Column(columnName);
        addColumn(column);
        return column;
    }

    public Column getColumn(String columnName) {
        for (Column columnModel : this.columns) {
            if (columnModel.getColumnName() != null && columnModel.getColumnName().equals(columnName))
                return columnModel;
        }
        return null;
    }

    public Index index() {
        Index index = new Index();
        this.indexConsts.add(index);
        return index;
    }

    public Index index(String indexName) {
        Index index = new Index();
        index.setName(indexName);
        this.indexConsts.add(index);
        return index;
    }

    public Unique unique() {
        Unique unique = new Unique();
        this.uniqueConsts.add(unique);
        return unique;
    }

    public Unique unique(String uniqueName) {
        Unique unique = new Unique();
        unique.setName(uniqueName);
        this.uniqueConsts.add(unique);
        return unique;
    }

    public TableModel engineTail(String engineTail) {
        this.engineTail = engineTail;
        return this;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getCheck() {
        return this.check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Column> getColumns() {
        return this.columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public String getEngineTail() {
        return this.engineTail;
    }

    public void setEngineTail(String engineTail) {
        this.engineTail = engineTail;
    }

    public List<Index> getIndexConsts() {
        return this.indexConsts;
    }

    public void setIndexConsts(List<Index> indexConsts) {
        this.indexConsts = indexConsts;
    }

    public List<Unique> getUniqueConsts() {
        return this.uniqueConsts;
    }

    public void setUniqueConsts(List<Unique> uniqueConsts) {
        this.uniqueConsts = uniqueConsts;
    }

    public Class<?> getEntityClass() {
        return this.entityClass;
    }

    public void setEntityClass(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String toString() {
        return "TableModel{tableName='" + this.tableName + '\'' + ", check='" + this.check + '\'' + ", comment='" + this.comment + '\'' + ", engineTail='" + this.engineTail + '\'' + ", columns=" + this.columns + ", indexConsts=" + this.indexConsts + ", uniqueConsts=" + this.uniqueConsts + ", entityClass=" + this.entityClass + ", alias='" + this.alias + '\'' + '}';
    }
}
