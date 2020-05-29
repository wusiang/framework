package com.xianmao.file;

/**
 * @ClassName FileConstant
 * @Description: TODO
 * @Author wjh
 * @Data 2020-05-29 21:54
 * @Version 1.0
 */
public class FileConstant {

    /**
     * 开发模式
     */
    private boolean devMode = false;

    /**
     * 远程上传模式
     */
    private boolean remoteMode = false;

    /**
     * 外网地址
     */
    private String domain = "http://localhost:8888";

    /**
     * 上传下载路径(物理路径)
     */
    private String remotePath = System.getProperty("user.dir") + "/target/blade";

    /**
     * 上传路径(相对路径)
     */
    private String uploadPath = "/upload";

    /**
     * 下载路径
     */
    private String downloadPath = "/download";

    /**
     * 图片压缩
     */
    private boolean compress = false;

    /**
     * 图片压缩比例
     */
    private Double compressScale = 2.00;

    /**
     * 图片缩放选择:true放大;false缩小
     */
    private boolean compressFlag = false;

    /**
     * 项目物理路径
     */
    private String realPath = System.getProperty("user.dir");

    /**
     * 项目相对路径
     */
    private String contextPath = "/";

    private static final FileConstant ME = new FileConstant();

    private FileConstant() {

    }

    public static FileConstant me() {
        return ME;
    }

    public String getUploadRealPath() {
        return (remoteMode ? remotePath : realPath) + uploadPath;
    }

    public String getUploadCtxPath() {
        return contextPath + uploadPath;
    }

    public boolean isDevMode() {
        return devMode;
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

    public boolean isRemoteMode() {
        return remoteMode;
    }

    public void setRemoteMode(boolean remoteMode) {
        this.remoteMode = remoteMode;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public boolean isCompress() {
        return compress;
    }

    public void setCompress(boolean compress) {
        this.compress = compress;
    }

    public Double getCompressScale() {
        return compressScale;
    }

    public void setCompressScale(Double compressScale) {
        this.compressScale = compressScale;
    }

    public boolean isCompressFlag() {
        return compressFlag;
    }

    public void setCompressFlag(boolean compressFlag) {
        this.compressFlag = compressFlag;
    }

    public String getRealPath() {
        return realPath;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public static FileConstant getME() {
        return ME;
    }
}
