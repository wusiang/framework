package com.xianmao.file;

import com.xianmao.date.DateUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

/**
 * @ClassName FileWrap
 * @Description: 上传文件封装
 * @Author wjh
 * @Data 2020-05-29 21:55
 * @Version 1.0
 */
public class FileWrap {

    /**
     * 上传文件在附件表中的id
     */
    private Object fileId;

    /**
     * 上传文件
     */
    private MultipartFile file;

    /**
     * 上传分类文件夹
     */
    private String dir;

    /**
     * 上传物理路径
     */
    private String uploadPath;

    /**
     * 上传虚拟路径
     */
    private String uploadVirtualPath;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 真实文件名
     */
    private String originalFileName;

    public FileWrap() {

    }

    public FileWrap(MultipartFile file, String dir) {
        this.dir = dir;
        this.file = file;
        this.fileName = file.getName();
        this.originalFileName = file.getOriginalFilename();
        this.uploadPath = FileUtil.formatUrl(java.io.File.separator + FileConstant.me().getUploadRealPath() + java.io.File.separator + dir + java.io.File.separator + DateUtil.format(new Date(), "yyyyMMdd") + java.io.File.separator + this.originalFileName);
        this.uploadVirtualPath = FileUtil.formatUrl(FileConstant.me().getUploadCtxPath().replace(FileConstant.me().getContextPath(), "") + java.io.File.separator + dir + java.io.File.separator + DateUtil.format(new Date(), "yyyyMMdd") + java.io.File.separator + this.originalFileName);
    }

    public FileWrap(MultipartFile file, String dir, String uploadPath, String uploadVirtualPath) {
        this(file, dir);
        if (null != uploadPath) {
            this.uploadPath = FileUtil.formatUrl(uploadPath);
            this.uploadVirtualPath = FileUtil.formatUrl(uploadVirtualPath);
        }
    }

    /**
     * 图片上传
     */
    public void transfer() {
        transfer(FileConstant.me().isCompress());
    }

    /**
     * 图片上传
     *
     * @param compress 是否压缩
     */
    public void transfer(boolean compress) {
        IFileProxy fileFactory = FileProxyManager.me().getDefaultFileProxyFactory();
        this.transfer(fileFactory, compress);
    }

    /**
     * 图片上传
     *
     * @param fileFactory 文件上传工厂类
     * @param compress    是否压缩
     */
    public void transfer(IFileProxy fileFactory, boolean compress) {
        try {
            java.io.File file = new java.io.File(uploadPath);

            if (null != fileFactory) {
                String[] path = fileFactory.path(file, dir);
                this.uploadPath = path[0];
                this.uploadVirtualPath = path[1];
                file = fileFactory.rename(file, path[0]);
            }

            java.io.File pfile = file.getParentFile();
            if (!pfile.exists()) {
                pfile.mkdirs();
            }

            this.file.transferTo(file);

            if (compress) {
                fileFactory.compress(this.uploadPath);
            }

        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public String getUploadVirtualPath() {
        return uploadVirtualPath;
    }

    public void setUploadVirtualPath(String uploadVirtualPath) {
        this.uploadVirtualPath = uploadVirtualPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }
}
