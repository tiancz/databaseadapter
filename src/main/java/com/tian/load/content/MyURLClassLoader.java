package com.tian.load.content;

import com.tian.adapter.sql.Dialect;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.loader.LaunchedURLClassLoader;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class MyURLClassLoader extends LaunchedURLClassLoader {
    private JarURLConnection cachedJarFile = null;

    protected static final Logger logger = LoggerFactory.getLogger(Dialect.class);

    public MyURLClassLoader(URL[] urls, ClassLoader parent) throws IOException {
        super(urls, parent);
        if (!ObjectUtils.isEmpty((Object[])urls)) {
            URL file = urls[0];
            URLConnection uc = file.openConnection();
            if (uc instanceof JarURLConnection) {
                uc.setUseCaches(true);
                ((JarURLConnection)uc).getManifest();
                this.cachedJarFile = (JarURLConnection)uc;
                logger.info("加载JAR文件:"+ this.cachedJarFile.getJarFile().getName());
            }
        }
    }

    public void unloadJarFile() {
        JarURLConnection jarURLConnection = this.cachedJarFile;
        if (ObjectUtils.isEmpty(jarURLConnection))
            return;
        try {
            logger.info("卸载JAR文件:"+ jarURLConnection.getJarFile().getName());
                    jarURLConnection.getJarFile().close();
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
