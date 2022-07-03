package com.tian.load.content;

import com.tian.enums.DataSourceEnum;
import com.tian.utils.ResourceUtil;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DynamicLoadContent {
    private static final String JAR_PATH = "lib";

    private static final String JAR_NAME_FORMAT = "%s-%s.jar";

    private DynamicLoadContent() {}

    private static class SingletonHolder {
        private static final DynamicLoadContent INSTANCE = new DynamicLoadContent();
    }

    public static final DynamicLoadContent getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static final Map<DataSourceEnum, File> DB_JAR_MAP = new HashMap<>();

    private static final Map<DataSourceEnum, Driver> DB_DRIVER_MAP = new HashMap<>();

    private static final Map<DataSourceEnum, MyURLClassLoader> URL_CLASS_LOADER_MAP = new HashMap<>();

    private static final Map<DataSourceEnum, DriverCounter> DB_COUNTER = new HashMap<>();

    static {
        initJar();
    }

    public static void initJar() {
        for (DataSourceEnum dbEnum : (DataSourceEnum[])DataSourceEnum.class.getEnumConstants()) {
            String jarName = String.format("%s-%s.jar", new Object[] { dbEnum.getCode(), dbEnum.getVersion() });
            try {
                File file = ResourceUtil.getJarByFile("lib/" + jarName, jarName);
                if (file != null)
                    DB_JAR_MAP.put(dbEnum, file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void dynamicLoad(DataSourceEnum dataSource) throws Exception {
        File f = DB_JAR_MAP.get(dataSource);
        if (f == null)
            throw new Exception("找不到数据库驱动jar");
        DriverCounter init = new DriverCounter(1);
        DriverCounter last = DB_COUNTER.put(dataSource, init);
        if (last != null) {
            init.set(last.get() + 1);
        } else {
            (new URL[1])[0] = new URL("jar:" + f.toURI().toURL().toExternalForm() + "!/");

            MyURLClassLoader ucl = new MyURLClassLoader(new URL[1], findParentClassLoader());
            Class<Driver> clazz = (Class)Class.forName(dataSource.getDrivers(), true, (ClassLoader)ucl);
            Driver d = clazz.newInstance();
            DriverAgent da = new DriverAgent(d);
            DriverManager.registerDriver(da);
            DB_DRIVER_MAP.put(dataSource, da);
            URL_CLASS_LOADER_MAP.put(dataSource, ucl);
        }
    }

    private static ClassLoader findParentClassLoader() {
        ClassLoader parent = DynamicLoadContent.class.getClassLoader();
        if (parent == null)
            parent = DynamicLoadContent.class.getClassLoader();
        if (parent == null)
            parent = ClassLoader.getSystemClassLoader();
        return parent;
    }

    public synchronized Connection getConnection(DataSourceEnum dataSource, String url, String user, String pass) throws Exception {
        DriverAgent da = (DriverAgent)DB_DRIVER_MAP.get(dataSource);
        if (da == null)
            throw new Exception("找不到数据库驱动");
        Properties info = new Properties();
        if (user != null)
            info.put("user", user);
        if (pass != null)
            info.put("password", pass);
        return da.getDriver().connect(url, info);
    }

    public synchronized void dynamicUnLoad(DataSourceEnum dataSource) throws SQLException {
        DriverCounter last = DB_COUNTER.get(dataSource);
        if (last.get() - 1 < 1) {
            DriverAgent da = (DriverAgent)DB_DRIVER_MAP.get(dataSource);
            DriverManager.deregisterDriver(da);
            DB_DRIVER_MAP.remove(dataSource);
            DB_COUNTER.remove(dataSource);
            MyURLClassLoader urlClassLoader = URL_CLASS_LOADER_MAP.get(dataSource);
            urlClassLoader.unloadJarFile();
            URL_CLASS_LOADER_MAP.remove(dataSource);
        } else {
            last.set(last.get() - 1);
        }
    }

    public static final class DriverCounter {
        private int val;

        public DriverCounter(int val) {
            this.val = val;
        }

        public int get() {
            return this.val;
        }

        public void set(int val) {
            this.val = val;
        }

        public String toString() {
            return Integer.toString(this.val);
        }
    }

    public static void main(String[] args) {
        DynamicLoadContent dynamicLoadContent = getInstance();
        try {
            DataSourceEnum dataSourceEnum = DataSourceEnum.getDataSourceEnum("mysql", "5.7");
            dynamicLoadContent.dynamicLoad(dataSourceEnum);
            String url = String.format(dataSourceEnum.getDriverUrl(), new Object[] { "127.0.0.1", "3306", "dm_sandbox" });
            Connection conn = dynamicLoadContent.getConnection(dataSourceEnum, url, "root", "root");
            System.out.println(conn);
            dynamicLoadContent.dynamicUnLoad(dataSourceEnum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
