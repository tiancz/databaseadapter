package com.tian.adapter.meta.scheme;

import java.util.Arrays;

public class Unique {
    private String name;

    private String[] columnList;

    public Unique() {}

    public Unique(String name) {
        this.name = name;
    }

    public Unique newCopy() {
        Unique result = new Unique();
        result.name = this.name;
        result.columnList = this.columnList;
        return result;
    }

    public Unique columns(String... columns) {
        this.columnList = columns;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getColumnList() {
        return this.columnList;
    }

    public void setColumnList(String[] columnList) {
        this.columnList = columnList;
    }

    public String toString() {
        return "Unique{name='" + this.name + '\'' + ", columnList=" +

                Arrays.toString((Object[])this.columnList) + '}';
    }
}
