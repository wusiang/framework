package com.xianmao.model;

/**
 * @ClassName XianmaoFile
 * @Description: TODO
 * @Author wjh
 * @Data 2020-06-25 10:31
 * @Version 1.0
 */
public class OssFile {
    /**
     * 文件地址
     */
    private String link;
    /**
     * 文件名
     */
    private String name;
    /**
     * 原始文件名
     */
    private String originalName;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }
}
