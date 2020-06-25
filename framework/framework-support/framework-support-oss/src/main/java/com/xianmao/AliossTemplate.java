package com.xianmao;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;
import com.xianmao.model.OssFile;
import com.xianmao.props.OssProperties;
import com.xianmao.rule.Rule;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @ClassName AliossTemplate
 * @Description: TODO
 * @Author wjh
 * @Data 2020-06-25 10:28
 * @Version 1.0
 */
public class AliossTemplate {

    private OSSClient ossClient;
    private OssProperties ossProperties;
    private Rule rule;

    public AliossTemplate(OSSClient ossClient, OssProperties ossProperties, Rule rule) {
        this.ossClient = ossClient;
        this.ossProperties = ossProperties;
        this.rule = rule;
    }

    private static final String SLASH = "/";

    public void makeBucket(String bucketName) {
        if (!bucketExists(bucketName)) {
            ossClient.createBucket(getBucketName(bucketName));
        }
    }

    public void removeBucket(String bucketName) {
        ossClient.deleteBucket(getBucketName(bucketName));
    }

    public boolean bucketExists(String bucketName) {
        return ossClient.doesBucketExist(getBucketName(bucketName));
    }

    public void copyFile(String bucketName, String fileName, String destBucketName) {
        ossClient.copyObject(getBucketName(bucketName), fileName, getBucketName(destBucketName), fileName);
    }

    public void copyFile(String bucketName, String fileName, String destBucketName, String destFileName) {
        ossClient.copyObject(getBucketName(bucketName), fileName, getBucketName(destBucketName), destFileName);
    }

    public String filePath(String fileName) {
        return getOssHost().concat(SLASH).concat(fileName);
    }

    public String filePath(String bucketName, String fileName) {
        return getOssHost(bucketName).concat(SLASH).concat(fileName);
    }

    public String fileLink(String fileName) {
        return getOssHost().concat(SLASH).concat(fileName);
    }

    public String fileLink(String bucketName, String fileName) {
        return getOssHost(bucketName).concat(SLASH).concat(fileName);
    }

    /**
     * 文件对象
     *
     * @param file 上传文件类
     * @return
     */

    public OssFile putFile(MultipartFile file) throws IOException {
        return putFile(ossProperties.getBucketName(), file.getOriginalFilename(), file);
    }

    /**
     * @param fileName 上传文件名
     * @param file     上传文件类
     * @return
     */
    public OssFile putFile(String fileName, MultipartFile file) throws IOException {
        return putFile(ossProperties.getBucketName(), fileName, file);
    }

    public OssFile putFile(String bucketName, String fileName, MultipartFile file) throws IOException {
        return putFile(bucketName, fileName, file.getInputStream());
    }

    public OssFile putFile(String fileName, InputStream stream) {
        return putFile(ossProperties.getBucketName(), fileName, stream);
    }

    public OssFile putFile(String bucketName, String fileName, InputStream stream) {
        return put(bucketName, stream, fileName, false);
    }

    public OssFile put(String bucketName, InputStream stream, String key, boolean cover) {
        makeBucket(bucketName);
        String originalName = key;
        key = getFileName(key);
        // 覆盖上传
        if (cover) {
            ossClient.putObject(getBucketName(bucketName), key, stream);
        } else {
            PutObjectResult response = ossClient.putObject(getBucketName(bucketName), key, stream);
            int retry = 0;
            int retryCount = 5;
            while (StringUtils.isEmpty(response.getETag()) && retry < retryCount) {
                response = ossClient.putObject(getBucketName(bucketName), key, stream);
                retry++;
            }
        }
        OssFile file = new OssFile();
        file.setOriginalName(originalName);
        file.setName(key);
        file.setLink(fileLink(bucketName, key));
        return file;
    }

    public void removeFile(String fileName) {
        ossClient.deleteObject(getBucketName(), fileName);
    }

    public void removeFile(String bucketName, String fileName) {
        ossClient.deleteObject(getBucketName(bucketName), fileName);
    }

    public void removeFiles(List<String> fileNames) {
        fileNames.forEach(this::removeFile);
    }

    public void removeFiles(String bucketName, List<String> fileNames) {
        fileNames.forEach(fileName -> removeFile(getBucketName(bucketName), fileName));
    }

    /**
     * 根据规则生成存储桶名称规则
     *
     * @return String
     */
    private String getBucketName() {
        return getBucketName(ossProperties.getBucketName());
    }

    /**
     * 根据规则生成存储桶名称规则
     *
     * @param bucketName 存储桶名称
     * @return String
     */
    private String getBucketName(String bucketName) {
        return rule.bucketName(bucketName);
    }

    /**
     * 根据规则生成文件名称规则
     *
     * @param originalFilename 原始文件名
     * @return string
     */
    private String getFileName(String originalFilename) {
        return rule.fileName(originalFilename);
    }

    public String getOssHost(String bucketName) {
        String prefix = ossProperties.getEndpoint().contains("https://") ? "https://" : "http://";
        return prefix + getBucketName(bucketName) + "." + ossProperties.getEndpoint().replaceFirst(prefix, "");
    }

    public String getOssHost() {
        return getOssHost(ossProperties.getBucketName());
    }
}
