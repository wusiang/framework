package com.xianmao.common.crypt;

import com.github.pagehelper.util.StringUtil;
import com.xianmao.common.exception.BussinessException;
import com.xianmao.common.utils.AssertUtil;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName AesUtil
 * @Description: TODO
 * @Author wjh
 * @Data 2020-05-04 23:44
 * @Version 1.0
 */
public class Aes {

    public static String encrypt(String sSrc, String sKey) {
        return encrypt(sSrc, sKey, null);
    }

    public static String encrypt(String sSrc, String sKey, String sIv) {
        AssertUtil.isNull(sKey, "key must not been null");
        byte[] raw = sKey.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        try {
            String type = "AES/ECB/PKCS5Padding";
            if (StringUtil.isNotEmpty(sIv)) {
                type = "AES/CBC/PKCS5Padding";
            }
            Cipher cipher = Cipher.getInstance(type);
            if (StringUtil.isNotEmpty(sIv)) {
                byte[] bytes = sIv.getBytes();

                IvParameterSpec iv = new IvParameterSpec(bytes);
                cipher.init(1, skeySpec, iv);
            } else {
                cipher.init(1, skeySpec);
            }
            byte[] encrypted = cipher.doFinal(sSrc.getBytes(StandardCharsets.UTF_8));

            return new Base64().encodeToString(encrypted);
        } catch (Exception e) {
            throw new BussinessException("encrypt error");
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
            if (StringUtil.isNotEmpty(sIv)) {
                type = "AES/CBC/PKCS5Padding";
            }
            Cipher cipher = Cipher.getInstance(type);
            if (StringUtil.isNotEmpty(sIv)) {
                byte[] bytes = sIv.getBytes();

                IvParameterSpec iv = new IvParameterSpec(bytes);
                cipher.init(2, keySpec, iv);
            } else {
                cipher.init(2, keySpec);
            }
            byte[] encrypted1 = new Base64().decode(sSrc);
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            throw new BussinessException("decrypt error");
        }
    }
}
