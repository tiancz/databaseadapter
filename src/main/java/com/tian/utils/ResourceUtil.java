package com.tian.utils;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ResourceUtil {
    private static final String PATH_WINDOWS = "\\\\";

    private static final String PATH_LINUX = "/";

    public static URL getJarURL(String pathName) throws FileNotFoundException {
        try {
            ClassPathResource classPathResource = new ClassPathResource(pathName);
            return classPathResource.getURL();
        } catch (IOException e) {
            throw new FileNotFoundException(pathName + "文件不存在");
        }
    }

    public static InputStream getJarInputStream(String pathName) throws FileNotFoundException {
        try {
            ClassPathResource classPathResource = new ClassPathResource(pathName);
            return classPathResource.getInputStream();
        } catch (IOException e) {
            throw new FileNotFoundException(pathName + "文件不存在");
        }
    }

    public static File getJarByFile(String pathName, String fileName) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(pathName);
        InputStream input = classPathResource.getInputStream();
        File somethingFile = new File(fileName);
        try {
            FileUtils.copyInputStreamToFile(input, somethingFile);
        } finally {
            input.close();
        }
        return somethingFile;
    }
}
