package com.tian.load.content;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

public class DriverAgent implements Driver {
    private Driver driver;

    DriverAgent(Driver d) {
        this.driver = d;
    }

    public boolean acceptsURL(String u) throws SQLException {
        return this.driver.acceptsURL(u);
    }

    public Connection connect(String u, Properties p) throws SQLException {
        return this.driver.connect(u, p);
    }

    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return this.driver.getPropertyInfo(url, info);
    }

    public int getMajorVersion() {
        return this.driver.getMajorVersion();
    }

    public int getMinorVersion() {
        return this.driver.getMinorVersion();
    }

    public boolean jdbcCompliant() {
        return this.driver.jdbcCompliant();
    }

    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return this.driver.getParentLogger();
    }

    public Driver getDriver() {
        return this.driver;
    }
}
