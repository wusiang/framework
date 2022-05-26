package com.xianmao.common.core.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * AES加解密工具类
 */
public class AesUtils {

    public static String encrypt(String sSrc, String sKey) {
        return encrypt(sSrc, sKey, null);
    }

    public static String encrypt(String sSrc, String sKey, String sIv) {
        AssertUtils.isNull(StrUtil.isBlank(sKey), "key must not been null");
        byte[] raw = sKey.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        try {
            String type = "AES/ECB/PKCS5Padding";
            if (StrUtil.isNotEmpty(sIv)) {
                type = "AES/CBC/PKCS5Padding";
            }
            Cipher cipher = Cipher.getInstance(type);
            if (StrUtil.isNotEmpty(sIv)) {
                byte[] bytes = sIv.getBytes();

                IvParameterSpec iv = new IvParameterSpec(bytes);
                cipher.init(1, skeySpec, iv);
            } else {
                cipher.init(1, skeySpec);
            }
            byte[] encrypted = cipher.doFinal(sSrc.getBytes(StandardCharsets.UTF_8));

            return Base64.encode(encrypted);
        } catch (Exception e) {
            throw ExceptionUtil.wrapRuntime(e);
        }
    }

    public static String decrypt(String sSrc, String sKey) {
        return decrypt(sSrc, sKey, null);
    }

    public static String decrypt(String sSrc, String sKey, String sIv) {
        try {
            byte[] raw = sKey.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");

            String type = "AES/ECB/PKCS5Padding";
            if (StrUtil.isNotEmpty(sIv)) {
                type = "AES/CBC/PKCS5Padding";
            }
            Cipher cipher = Cipher.getInstance(type);
            if (StrUtil.isNotEmpty(sIv)) {
                byte[] bytes = sIv.getBytes();

                IvParameterSpec iv = new IvParameterSpec(bytes);
                cipher.init(2, keySpec, iv);
            } else {
                cipher.init(2, keySpec);
            }
            byte[] encrypted1 = Base64.decode(sSrc);
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            throw ExceptionUtil.wrapRuntime(ex);
        }
    }

    public static void main(String[] args) {
        System.out.println(decrypt(
                "pFNcv3XOazyxD5rjvS+Z0UmLBr99L3ySU9F6x/ZkweuOY/xdsKQxl6zEFND4TkI6rC0hTNOZMXhRDQg0W6PRyuXVYnx7p8KzrO7d7ow2YngHZ2Ezsy7SYcJXK4QUwz6IqhAZbWZt9mdrC6E5ZLxMEtoIpM9r72omp6aUNPuVE9y++eU4GE6zucCtAxcamt/3hCVAJN8SRKoMbubVXaD6/vyKoLAJVIXggh2TEmPOc1MD9mTu+pzDCr79o8t2f2YKLzjalxxraH/Q+Auh8T+SorHZ99XlCjPSnLGztaHD7sLcciZt+Nn86vk1ioRqSaaEM2Th5OuizdYyi1+67bimpQ==",
                "1234567812345679", "1243567812346579"));
    }
}
