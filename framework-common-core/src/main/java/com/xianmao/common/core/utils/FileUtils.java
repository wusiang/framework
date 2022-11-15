package com.xianmao.common.core.utils;

import cn.hutool.core.text.CharPool;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class FileUtils {

    /**
     * 读取文件内容
     */
    public static String readFileContent(File file, String encoding)
            throws Exception {
        FileInputStream fis = new FileInputStream(file);
        return readStreamContent(fis, encoding);
    }

    /**
     * 读取文件内容
     */
    public static String readStreamContent(InputStream stream, String encoding)
            throws Exception {
        StringBuilder content = new StringBuilder("");
        byte[] bytearray = new byte[stream.available()];
        int bytetotal = stream.available();
        while (stream.read(bytearray, 0, bytetotal) != -1) {
            String temp = new String(bytearray, 0, bytetotal, encoding);
            content.append(temp);
        }
        return content.toString();
    }

    /**
     * 获取文件后缀名
     * @param fileName 文件名称
     */
    public static String getExtension(String fileName) {
        Assert.notNull(fileName, "file fileName is null.");
        return getExtension(fileName, null, false);
    }

    /**
     * 获取文件后缀名
     * @param fileName 文件名称
     * @param nullExt 文件名为空名称
     */
    public static String getExtension(String fileName, String nullExt) {
        return getExtension(fileName, nullExt, false);
    }

    /**
     * 获取文件名，去除后缀名
     */
    public static String getNameWithoutExtension(String file) {
        Assert.notNull(file, "file is null.");
        String fileName = new File(file).getName();
        int dotIndex = fileName.lastIndexOf(CharPool.DOT);
        return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
    }

    public static String getExtension(String fileName, String nullExt, boolean toLowerCase) {
        if (fileName == null) {
            return null;
        }
        fileName = fileName.trim();
        fileName = fileName.replace('\\', '/');
        fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
        int index = fileName.lastIndexOf(".");
        String ext = null;
        if (index >= 0) {
            ext = fileName.substring(index + 1).trim();
        }
        if (ext == null) {
            return nullExt;
        } else {
            return toLowerCase ? ext.toLowerCase() : ext;
        }
    }

    public static class FileNameAndExtension {
        private final String fileName;
        private final String extension;

        private FileNameAndExtension(String fileName, String extension, boolean extensionToLowerCase) {
            this.fileName = fileName;
            this.extension = extensionToLowerCase ? extension.toLowerCase() : extension;
        }

        public String getFileName() {
            return fileName;
        }
        public String getExtension() {
            return extension;
        }
        public String toString() {
            return extension == null ? fileName : fileName + "." + extension;
        }
    }
}
