package com.xianmao.crypt;

import com.xianmao.exception.util.ExceptionUtil;
import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Base64;
import org.springframework.lang.Nullable;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName DigestUtil
 * @Description: 加密相关工具类直接使用Spring util封装，减少jar依赖
 * @Author wjh
 * @Data 2020-04-28 13:19
 * @Version 1.0
 */
public class Digest extends org.springframework.util.DigestUtils {

    /**
     * Calculates the MD5 digest and returns the value as a 32 character hex string.
     *
     * @param data Data to digest
     * @return MD5 digest as a hex string
     */
    public static String md5Hex(final String data) {
        return Digest.md5DigestAsHex(data.getBytes(Charsets.UTF_8));
    }

    /**
     * Return a hexadecimal string representation of the MD5 digest of the given bytes.
     *
     * @param bytes the bytes to calculate the digest over
     * @return a hexadecimal digest string
     */
    public static String md5Hex(final byte[] bytes) {
        return Digest.md5DigestAsHex(bytes);
    }

    private static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();

    public static String hmacSha1(String text, SecretKeySpec sk) {
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(sk);
            byte[] rawHmac = mac.doFinal(text.getBytes());
            return new String(org.apache.commons.codec.binary.Base64.encodeBase64(rawHmac));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String hmacSha256(String text, String key) {
        SecretKeySpec sk = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        return hmacSha1(text, sk);
    }

    public static String hmacSha256(String text, SecretKeySpec sk) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(sk);
            byte[] rawHmac = mac.doFinal(text.getBytes());
            return new String(Base64.encodeBase64(rawHmac));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String sha1(String srcStr) {
        return hash("SHA-1", srcStr);
    }

    public static String sha256(String srcStr) {
        return hash("SHA-256", srcStr);
    }

    public static String sha384(String srcStr) {
        return hash("SHA-384", srcStr);
    }

    public static String sha512(String srcStr) {
        return hash("SHA-512", srcStr);
    }

    public static String hash(String algorithm, String srcStr) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] bytes = md.digest(srcStr.getBytes(Charsets.UTF_8));
            return toHex(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw ExceptionUtil.unchecked(e);
        }
    }

    public static String toHex(byte[] bytes) {
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return ret.toString();
    }

    public static boolean slowEquals(@Nullable String a, @Nullable String b) {
        if (a == null || b == null) {
            return false;
        }
        return slowEquals(a.getBytes(Charsets.UTF_8), b.getBytes(Charsets.UTF_8));
    }

    public static boolean slowEquals(@Nullable byte[] a, @Nullable byte[] b) {
        if (a == null || b == null) {
            return false;
        }
        if (a.length != b.length) {
            return false;
        }
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }

    /**
     * 自定义加密 先MD5再SHA1
     *
     * @param data 数据
     * @return String
     */
    public static String encrypt(String data) {
        return sha1(md5Hex(data));
    }
}
