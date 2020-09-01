package com.xianmao.crypt;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName AesUtil
 * @Description: TODO
 * @Author wjh
 * @Data 2020-05-04 23:44
 * @Version 1.0
 */
public class Aes {

    private static final String defaultCharset = "UTF-8";
    private static final String KEY_AES = "AES";
    private static final String KEY_MD5 = "MD5";
    private static MessageDigest md5Digest;

    static {
        try {
            md5Digest = MessageDigest.getInstance(KEY_MD5);
        } catch (NoSuchAlgorithmException e) {

        }
    }

    /**
     * 加密
     */
    public static String encrypt(String data, String key) {
        return doAES(data, key, Cipher.ENCRYPT_MODE);
    }

    /**
     * 解密
     */
    public static String decrypt(String data, String key) {
        return doAES(data, key, Cipher.DECRYPT_MODE);
    }


    /**
     * 加解密
     */
    private static String doAES(String data, String key, int mode) {
        try {
            boolean encrypt = mode == Cipher.ENCRYPT_MODE;
            byte[] content;
            if (encrypt) {
                content = data.getBytes(defaultCharset);
            } else {
                content = Base64.decodeBase64(data.getBytes());
            }
            SecretKeySpec keySpec = new SecretKeySpec(md5Digest.digest(key.getBytes(defaultCharset))
                    , KEY_AES);
            Cipher cipher = Cipher.getInstance(KEY_AES);
            cipher.init(mode, keySpec);
            byte[] result = cipher.doFinal(content);
            if (encrypt) {
                return new String(Base64.encodeBase64(result));
            } else {
                return new String(result, defaultCharset);
            }
        } catch (Exception e) {
        }
        return null;
    }
}
