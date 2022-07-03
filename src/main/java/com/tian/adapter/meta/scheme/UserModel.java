package com.tian.adapter.meta.scheme;

public class UserModel {
    private String userName;

    private String password;

    private String tableSpace;

    private String space;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTableSpace(String tableSpace) {
        this.tableSpace = tableSpace;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getPassword() {
        return this.password;
    }

    public String getTableSpace() {
        return this.tableSpace;
    }

    public String getSpace() {
        return this.space;
    }
}
