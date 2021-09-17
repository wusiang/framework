package com.xianmao.common.crypt;

import org.apache.commons.codec.Charsets;

/**
 * @ClassName Base64Util
 * @Description: TODO
 * @Author wjh
 * @Data 2020-04-28 13:18
 * @Version 1.0
 */
public class Base64 extends org.springframework.util.Base64Utils  {

    /**
     * 编码
     *
     * @param value 字符串
     * @return {String}
     */
    public static String encode(String value) {
        return Base64.encode(value, Charsets.UTF_8);
    }

    /**
     * 编码
     *
     * @param value   字符串
     * @param charset 字符集
     * @return {String}
     */
    public static String encode(String value, java.nio.charset.Charset charset) {
        byte[] val = value.getBytes(charset);
        return new String(Base64.encode(val), charset);
    }

    /**
     * 编码URL安全
     *
     * @param value 字符串
     * @return {String}
     */
    public static String encodeUrlSafe(String value) {
        return Base64.encodeUrlSafe(value, Charsets.UTF_8);
    }

    /**
     * 编码URL安全
     *
     * @param value   字符串
     * @param charset 字符集
     * @return {String}
     */
    public static String encodeUrlSafe(String value, java.nio.charset.Charset charset) {
        byte[] val = value.getBytes(charset);
        return new String(Base64.encodeUrlSafe(val), charset);
    }

    /**
     * 解码
     *
     * @param value 字符串
     * @return {String}
     */
    public static String decode(String value) {
        return Base64.decode(value, Charsets.UTF_8);
    }

    /**
     * 解码
     *
     * @param value   字符串
     * @param charset 字符集
     * @return {String}
     */
    public static String decode(String value, java.nio.charset.Charset charset) {
        byte[] val = value.getBytes(charset);
        byte[] decodedValue = Base64.decode(val);
        return new String(decodedValue, charset);
    }

    /**
     * 解码URL安全
     *
     * @param value 字符串
     * @return {String}
     */
    public static String decodeUrlSafe(String value) {
        return Base64.decodeUrlSafe(value, Charsets.UTF_8);
    }

    /**
     * 解码URL安全
     *
     * @param value   字符串
     * @param charset 字符集
     * @return {String}
     */
    public static String decodeUrlSafe(String value, java.nio.charset.Charset charset) {
        byte[] val = value.getBytes(charset);
        byte[] decodedValue = Base64.decodeUrlSafe(val);
        return new String(decodedValue, charset);
    }
}
