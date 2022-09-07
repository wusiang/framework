package com.xianmao.common.mybatis.utils;

import cn.hutool.core.util.StrUtil;
import com.xianmao.common.mybatis.annoation.Crypto;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CryptoUtils {
    private static final String secretKey = "#FA0S12&DF9QEX6!";

    /**
     * 加密数据
     */
    public static String encryptStr(String plaintext) {
        if (StrUtil.isBlank(plaintext)) {
            return plaintext;
        }
        return AesUtils.encrypt(plaintext, secretKey);
    }

    /**
     * 解密数据
     */
    public static String decryptStr(String ciphertext) {
        if (StrUtil.isBlank(ciphertext)) {
            return ciphertext;
        }
        return AesUtils.decrypt(ciphertext, secretKey);
    }

    /**
     * 对含加密注解的字段进行加密
     */
    public static <T> void encryptObject(final T t) {
        List<Field> cryptoFields = fetchCryptoFields(t, true);
        if (CollectionUtils.isEmpty(cryptoFields)) {
            return;
        }

        cryptoFields.forEach(f -> {
            String value = (String) ReflectionUtils.getField(f, t);
            String ciphertext = CryptoUtils.encryptStr(value);
            ReflectionUtils.setField(f, t, ciphertext);
        });
    }

    /**
     * 对含加密注解的字段进行批量加密
     */
    public static <T> void encryptList(final List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        list.forEach(o -> {
            encryptObject(o);
        });
    }

    /**
     * 对含加密注解的字段进行加密
     */
    public static <T> void decryptObject(final T t) {
        List<Field> cryptoFields = fetchCryptoFields(t, false);
        if (CollectionUtils.isEmpty(cryptoFields)) {
            return;
        }

        cryptoFields.forEach(f -> {
            String value = (String) ReflectionUtils.getField(f, t);
            String ciphertext = CryptoUtils.decryptStr(value);
            ReflectionUtils.setField(f, t, ciphertext);
        });
    }

    /**
     * 对含加密注解的字段进行批量加密
     */
    public static <T> void decryptList(final List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        list.forEach(o -> {
            decryptObject(o);
        });
    }

    /**
     * 获取加密字段
     */
    private static <T> List<Field> fetchCryptoFields(final T t, final boolean encrypt) {
        final List<Field> fields = new ArrayList<>();

        ReflectionUtils.doWithFields(t.getClass(), fields::add, field -> {
            Crypto crypto = AnnotationUtils.getAnnotation(field, Crypto.class);
            boolean matches = crypto != null && "String".equals(field.getType().getSimpleName());
            if (!encrypt) {
                matches = matches && crypto.autoDecrypt();
            }
            if (!matches) {
                return false;
            }
            ReflectionUtils.makeAccessible(field);
            String value = (String) ReflectionUtils.getField(field, t);
            return StringUtils.hasLength(value);
        });
        return fields;
    }
}
