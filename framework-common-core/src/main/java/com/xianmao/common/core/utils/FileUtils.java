package com.xianmao.common.core.utils;

import cn.hutool.core.text.CharPool;
import org.springframework.util.Assert;

import java.io.File;

public class FileUtils {

    /**
     * 获取文件后缀名
     * @param fullName 文件全名
     * @return {String}
     */
    public static String getFileExtension(String fullName) {
        Assert.notNull(fullName, "file fullName is null.");
        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    /**
     * 获取文件名，去除后缀名
     * @param file 文件
     * @return {String}
     */
    public static String getNameWithoutExtension(String file) {
        Assert.notNull(file, "file is null.");
        String fileName = new File(file).getName();
        int dotIndex = fileName.lastIndexOf(CharPool.DOT);
        return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
    }
}
