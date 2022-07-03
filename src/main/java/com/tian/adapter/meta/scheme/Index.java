package com.tian.adapter.meta.scheme;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Index implements Serializable {
    private static final long serialVersionUID = -4194272041788122320L;

    private String name;

    private boolean unique;

    private IndexType indexType;

    private int pages;

    private StringBuilder definition;

    private List<String> columnList;

    public Index newCopy() {
        Index result = new Index();
        result.name = this.name;
        result.indexType = this.indexType;
        result.pages = this.pages;
        result.definition = this.definition;
        result.columnList = this.columnList;
        result.unique = this.unique;
        return result;
    }

    public Index columns(String... columns) {
        this.columnList = Arrays.asList(columns);
        return this;
    }

    public Index unique() {
        this.unique = true;
        return this;
    }

    public boolean getUnique() {
        return this.unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public IndexType getIndexType() {
        return this.indexType;
    }

    public void setIndexType(IndexType indexType) {
        this.indexType = indexType;
    }

    public int getPages() {
        return this.pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public StringBuilder getDefinition() {
        return this.definition;
    }

    public void setDefinition(StringBuilder definition) {
        this.definition = definition;
    }

    public List<String> getColumnList() {
        return this.columnList;
    }

    public void setColumnList(List<String> columnList) {
        this.columnList = columnList;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "Index [isUnique=" + this.unique + ", indexType=" + this.indexType + ", pages=" + this.pages + ", definition=" + this.definition + ", columnList=" + this.columnList + "]";
    }
}
