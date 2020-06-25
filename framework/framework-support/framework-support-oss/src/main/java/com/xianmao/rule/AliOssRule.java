package com.xianmao.rule;

import org.springframework.util.Assert;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @ClassName AliOssRule
 * @Description: TODO
 * @Author wjh
 * @Data 2020-06-25 10:13
 * @Version 1.0
 */
public class AliOssRule implements Rule {

    @Override
    public String bucketName(String bucketName) {
        return bucketName;
    }

    @Override
    public String fileName(String originalFilename) {

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String today = format.format(new Date());
        return "upload" + "/" + today + "/" + randomUUID() + "." + getFileExtension(originalFilename);
    }

    private String randomUUID() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return new UUID(random.nextLong(), random.nextLong()).toString().replace("-", "");
    }

    private String getFileExtension(String fullName) {
        Assert.notNull(fullName, "file fullName is null.");
        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }
}
