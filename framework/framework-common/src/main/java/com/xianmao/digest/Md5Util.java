package com.xianmao.digest;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @ClassName MD5Util
 * @Description: TODO
 * @Author guyi
 * @Data 2019-08-14 10:05
 * @Version 1.0
 */
public class Md5Util {

    /**
     * 私用构造主法.因为此类是工具类
     */
    private Md5Util() {
    }

    /**
     * MD5加密工具类
     *
     * @param s
     * @return
     */
    public static String md5(String s) {

        try {
            java.security.MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(s.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 使用MD5 对两端加密后的密文进行比较
     *
     * @param strOne 未加密的字符串
     * @param strTwo 已加密的字符串
     * @return boolean
     */
    public static boolean check(String strOne, String strTwo) {
        if (md5(strOne).equals(strTwo)) {
            return true;
        } else {
            return false;
        }
    }
}
