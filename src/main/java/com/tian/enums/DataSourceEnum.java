package com.tian.enums;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum DataSourceEnum {
    MYSQL_5("mysql", "5.1.46", "com.mysql.jdbc.Driver", "jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=utf8&useSSL=false", new String[] { "5.1", "5.2", "5.3", "5.4", "5.5", "5.6", "5.7" }, true, "mysql 8 数据库驱动信息配置" ),

    MYSQL_8("mysql", "8.0.13", "com.mysql.cj.jdbc.Driver", "jdbc:mysql://%s:%s/%s?serverTimeZone=Asia/Shanghai&useUnicode=true&characterEncoding=utf8&useSSL=false&rewriteBatchedStatements=true", new String[] { "6", "7", "8" }, false, "mysql 8 数据库驱动信息配置"),

    ORACLE_8("oracle", "8", "oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@%s:%s/%s", new String[] { "10g", "11g", "12c" }, true, "oracle 8 数据库驱动信息配置"),

    POSTGRESQL_42("postgresql", "42.2.8", "org.postgresql.Driver", "jdbc:postgresql://%s:%s/%s", new String[] { "10" }, true, "postgresql 42 数据库驱动信息配置");

    String code;

    String version;

    String drivers;

    String driverUrl;

    String[] supportVersion;

    boolean defaults;

    String desc;

    DataSourceEnum(String code, String version, String drivers, String driverUrl, String[] supportVersion, boolean defaults, String desc) {
        this.code = code;
        this.version = version;
        this.drivers = drivers;
        this.driverUrl = driverUrl;
        this.supportVersion = supportVersion;
        this.defaults = defaults;
        this.desc = desc;
    }

    public String getCode() {
        return this.code;
    }

    public String getVersion() {
        return this.version;
    }

    public String getDrivers() {
        return this.drivers;
    }

    public String getDriverUrl() {
        return this.driverUrl;
    }

    public String getDesc() {
        return this.desc;
    }

    public String[] getSupportVersion() {
        return this.supportVersion;
    }

    public boolean isDefaults() {
        return this.defaults;
    }

    public static DataSourceEnum getDataSourceEnum(String code, String dbVersion) {
        List<DataSourceEnum> configDse = (List<DataSourceEnum>) Arrays.stream(DataSourceEnum.class.getEnumConstants()).filter(dse -> dse.getCode().equalsIgnoreCase(code)).collect(Collectors.toList());
        if (ObjectUtils.isEmpty(configDse))
            return null;
        if (!StringUtils.isEmpty(dbVersion))
            return getDataSourceEnum(configDse, dbVersion);
        return configDse.stream().filter(dse -> dse.isDefaults()).findFirst().get();
    }

    private static DataSourceEnum getDataSourceEnum(List<DataSourceEnum> configDse, String dbVersion) {
        for (DataSourceEnum dse : configDse) {
            String[] supportVersion = dse.getSupportVersion();
            if (!ObjectUtils.isEmpty((Object[])supportVersion))
                for (String sv : supportVersion) {
                    if (dbVersion.toLowerCase().startsWith(sv))
                        return dse;
                }
        }
        return null;
    }
}
