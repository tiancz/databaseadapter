package com.tian.adapter.meta.scheme;

public class UserGrantModel {
    private String userName;

    private String databaseName;

    private String tableSpace;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public void setTableSpace(String tableSpace) {
        this.tableSpace = tableSpace;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getDatabaseName() {
        return this.databaseName;
    }

    public String getTableSpace() {
        return this.tableSpace;
    }
}
