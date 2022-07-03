package com.tian.load.param.req;

import org.apache.commons.lang3.StringUtils;

public class TradeExecReqModel {
    private String databaseType;

    private String version;

    private String ip;

    private String port;

    private String databaseName;

    private String username;

    private String password;

    private String dbaUser;

    public String getDatabaseType() {
        return this.databaseType;
    }

    public String getVersion() {
        return this.version;
    }

    public String getIp() {
        return this.ip;
    }

    public String getPort() {
        return this.port;
    }

    public String getDatabaseName() {
        return this.databaseName;
    }

    public String getUsername() {
        if (!StringUtils.isEmpty(this.dbaUser))
            return this.username + " " + this.dbaUser;
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDbaUser(String dbaUser) {
        this.dbaUser = dbaUser;
    }

    public String getDbaUser() {
        return this.dbaUser;
    }
}
