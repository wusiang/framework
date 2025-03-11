package com.xianmao.common.core.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.regex.Pattern;

/**
 * 密码工具类：加密、验证、生成随机密码、强度校验
 * 1.安全算法：采用SHA-256哈希算法，结合随机盐值抵御彩虹表攻击。
 * 2.不可逆加密：加密后结果无法逆向解密，验证时需重新计算哈希值。3
 * 3.密码复杂度：强制包含大小写字母、数字、特殊字符，最小长度8位。
 * 4.工具类规范：私有化构造函数防止实例化，符合阿里巴巴开发规范。
 */
public class PasswordUtils {

    // 常量定义
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16;
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final Pattern UPPER_CASE = Pattern.compile("[A-Z]");
    private static final Pattern LOWER_CASE = Pattern.compile("[a-z]");
    private static final Pattern DIGIT = Pattern.compile("\\d");
    private static final Pattern SPECIAL_CHAR = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");

    // 私有构造函数
    private PasswordUtils() {
        throw new IllegalStateException("工具类不可实例化");
    }

    /**
     * 生成随机盐值（Base64编码）
     */
    public static String generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * 生成随机密码（包含数字、大小写字母、特殊字符）
     */
    public static String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_-+=<>?";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    /**
     * 密码加密（盐值 + SHA-256）
     */
    public static String hashPassword(String password, String salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
        digest.update(salt.getBytes());
        byte[] hashedBytes = digest.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedBytes);
    }

    /**
     * 验证密码是否匹配
     */
    public static boolean verifyPassword(String inputPassword, String storedHash, String salt)
            throws NoSuchAlgorithmException {
        String hashedInput = hashPassword(inputPassword, salt);
        return hashedInput.equals(storedHash);
    }

    /**
     * 检查密码强度（复杂度）
     */
    public static boolean isPasswordStrong(String password) {
        return password.length() >= MIN_PASSWORD_LENGTH &&
                UPPER_CASE.matcher(password).find() &&
                LOWER_CASE.matcher(password).find() &&
                DIGIT.matcher(password).find() &&
                SPECIAL_CHAR.matcher(password).find();
    }

//    // 测试主方法
//    public static void main(String[] args) throws NoSuchAlgorithmException {
//        String salt = generateSalt();
//        String password = "SecureP@ss123";
//        String hashedPassword = hashPassword(password, salt);
//
//        System.out.println("盐值: " + salt);
//        System.out.println("哈希后密码: " + hashedPassword);
//        System.out.println("验证结果: " + verifyPassword(password, hashedPassword, salt));
//        System.out.println("密码强度: " + isPasswordStrong(password));
//    }
}
