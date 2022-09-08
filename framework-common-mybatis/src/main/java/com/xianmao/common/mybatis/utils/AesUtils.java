package com.xianmao.common.mybatis.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.util.Assert;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class AesUtils {
    public static String encrypt(String sSrc, String sKey) {
        return encrypt(sSrc, sKey, null);
    }

    public static String encrypt(String sSrc, String sKey, String sIv) {
        Assert.isNull(sKey, "key must not been null");
        byte[] raw = sKey.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        try {
            String type = "AES/ECB/PKCS5Padding";
            if (StringUtils.isNotEmpty(sIv)) {
                type = "AES/CBC/PKCS5Padding";
            }
            Cipher cipher = Cipher.getInstance(type);
            if (StringUtils.isNotEmpty(sIv)) {
                byte[] bytes = sIv.getBytes();

                IvParameterSpec iv = new IvParameterSpec(bytes);
                cipher.init(1, skeySpec, iv);
            } else {
                cipher.init(1, skeySpec);
            }
            byte[] encrypted = cipher.doFinal(sSrc.getBytes(StandardCharsets.UTF_8));

            return Base64Utils.encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("AesUtils Encrypt Error", e);
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
            if (StringUtils.isNotEmpty(sIv)) {
                type = "AES/CBC/PKCS5Padding";
            }
            Cipher cipher = Cipher.getInstance(type);
            if (StringUtils.isNotEmpty(sIv)) {
                byte[] bytes = sIv.getBytes();

                IvParameterSpec iv = new IvParameterSpec(bytes);
                cipher.init(2, keySpec, iv);
            } else {
                cipher.init(2, keySpec);
            }
            byte[] encrypted1 = Base64Utils.decode(sSrc.getBytes());
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            throw new RuntimeException("AesUtils Decrypt Error", ex);
        }
    }
}
