package com.xianmao.file;

import java.io.File;

/**
 * @ClassName IFileProxy
 * @Description: 文件代理接口
 * @Author wjh
 * @Data 2020-05-29 21:55
 * @Version 1.0
 */
public interface IFileProxy {

    /**
     * 返回路径[物理路径][虚拟路径]
     *
     * @param file 文件
     * @param dir  目录
     * @return
     */
    String[] path(File file, String dir);

    /**
     * 文件重命名策略
     *
     * @param file 文件
     * @param path 路径
     * @return
     */
    File rename(File file, String path);

    /**
     * 图片压缩
     *
     * @param path 路径
     */
    void compress(String path);
}
