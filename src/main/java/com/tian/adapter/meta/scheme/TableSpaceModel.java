package com.tian.adapter.meta.scheme;

public class TableSpaceModel {
    private String tableSpaceName;

    private String tableSpaceFile;

    private String tableSpaceSize;

    private boolean autoextend;

    private String autoextendSize;

    private String maxSize;

    public void setTableSpaceName(String tableSpaceName) {
        this.tableSpaceName = tableSpaceName;
    }

    public void setTableSpaceFile(String tableSpaceFile) {
        this.tableSpaceFile = tableSpaceFile;
    }

    public void setTableSpaceSize(String tableSpaceSize) {
        this.tableSpaceSize = tableSpaceSize;
    }

    public void setAutoextend(boolean autoextend) {
        this.autoextend = autoextend;
    }

    public void setAutoextendSize(String autoextendSize) {
        this.autoextendSize = autoextendSize;
    }

    public void setMaxSize(String maxSize) {
        this.maxSize = maxSize;
    }

    public String getTableSpaceName() {
        return this.tableSpaceName;
    }

    public String getTableSpaceFile() {
        return this.tableSpaceFile;
    }

    public String getTableSpaceSize() {
        return this.tableSpaceSize;
    }

    public boolean isAutoextend() {
        return this.autoextend;
    }

    public String getAutoextendSize() {
        return this.autoextendSize;
    }

    public String getMaxSize() {
        return this.maxSize;
    }
}
