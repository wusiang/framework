package com.xianmao.file;

import java.io.File;

/**
 * @ClassName FileProxyManager
 * @Description: TODO
 * @Author wjh
 * @Data 2020-05-29 21:56
 * @Version 1.0
 */
public class FileProxyManager {

    private IFileProxy defaultFileProxyFactory = new FileProxyFactory();

    private static FileProxyManager me = new FileProxyManager();

    public static FileProxyManager me() {
        return me;
    }

    public IFileProxy getDefaultFileProxyFactory() {
        return defaultFileProxyFactory;
    }

    public void setDefaultFileProxyFactory(IFileProxy defaultFileProxyFactory) {
        this.defaultFileProxyFactory = defaultFileProxyFactory;
    }

    public String[] path(File file, String dir) {
        return defaultFileProxyFactory.path(file, dir);
    }

    public File rename(File file, String path) {
        return defaultFileProxyFactory.rename(file, path);
    }
}
