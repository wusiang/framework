package com.xianmao.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.cglib.core.Converter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName BeanUtil
 * @Description: 实体工具类
 * @Author wjh
 * @Data 2020-04-28 11:39
 * @Version 1.0
 */
public class BeanUtil {

    /**
     * Bean copier cache
     */
    private static final Map<String, BeanCopier> BEAN_COPIER_CACHE = new ConcurrentHashMap<>();

    /**
     * 深复制
     * <p>
     * 注意：不支持链式Bean
     *
     * @param source 源对象
     * @param <T>    泛型标记
     * @return T
     */
    @SuppressWarnings("unchecked")
    public static <T> T clone(T source) {
        return (T) BeanUtil.copy(source, source.getClass());
    }


    /**
     * 对象转换
     *
     * @param source           原对象
     * @param target           目标对象
     * @param ignoreProperties 排除要copy的属性
     */
    public static <T> T copy(Object source, T target, String... ignoreProperties) {
        if (ArrayUtils.isEmpty(ignoreProperties)) {
            BeanUtils.copyProperties(source, target);
        } else {
            BeanUtils.copyProperties(source, target, ignoreProperties);
        }
        return target;
    }

    /**
     * 属性拷贝
     *
     * @param source the source
     * @param target the target
     */
    public static void copyProperties(Object source, Object target) {
        if (source == null) {
            return;
        }
        BeanCopier copier = getBeanCopier(source.getClass(), target.getClass());
        copier.copy(source, target, null);
    }

    /**
     * 属性拷贝
     *
     * @param source    the source
     * @param target    the target
     * @param converter the converter
     */
    public static void copyProperties(Object source, Object target, Converter converter) {
        if (source == null) {
            return;
        }
        BeanCopier copier = getBeanCopier(source.getClass(), target.getClass());
        copier.copy(source, target, converter);
    }

    /**
     * 属性拷贝
     *
     * @param <T>         the type parameter
     * @param source      the source
     * @param targetClass the target class
     * @return the t
     */
    public static <T> T copyProperties(Object source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        T t;
        try {
            t = targetClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Create new instance of " + targetClass + " failed: " + e.getMessage());
        }
        copyProperties(source, t);
        return t;
    }

    /**
     * 将对象装成map形式
     *
     * @param bean 源对象
     * @return {Map}
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(Object bean) {
        return BeanMap.create(bean);
    }

    /**
     * 将对象装成map形式
     *
     * @param bean
     * @param null2String null是否转空字符串
     * @param <T>
     * @return
     */
    public static <T> Map<String, Object> toMap(T bean, Boolean null2String) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (bean != null) {
                BeanMap beanMap = BeanMap.create(bean);
                map.putAll(beanMap);
                if (null2String) {
                    map.replaceAll((k, v) -> v == null ? "" : v);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 将对象装成map形式
     *
     * @param bean
     * @param null2String      null是否转空字符串
     * @param ignoreProperties 忽略字符串
     * @param <T>
     * @return
     */
    public static <T> Map<String, Object> toMap(T bean, Boolean null2String, String... ignoreProperties) {
        Map<String, Object> map = toMap(bean, null2String);
        for (String ignoreProperty : ignoreProperties) {
            map.remove(ignoreProperty);
        }
        return map;
    }

    /**
     * 将map 转为 bean
     *
     * @param beanMap   map
     * @param targetClass 对象类型
     * @param <T>       泛型标记
     * @return {T}
     */
    public static <T> T toBean(Map<String, Object> beanMap, Class<T> targetClass) {
        T bean;
        try {
            bean = targetClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Create new instance of " + targetClass + " failed: " + e.getMessage());
        }
        BeanMap.create(bean).putAll(beanMap);
        return bean;
    }

    /**
     * 查询对象中空的元素
     *
     * @param source 查询对象
     */
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    private static BeanCopier getBeanCopier(Class sourceClass, Class targetClass) {
        String beanKey = generateKey(sourceClass, targetClass);
        BeanCopier copier;
        if (!BEAN_COPIER_CACHE.containsKey(beanKey)) {
            copier = BeanCopier.create(sourceClass, targetClass, false);
            BEAN_COPIER_CACHE.put(beanKey, copier);
        } else {
            copier = BEAN_COPIER_CACHE.get(beanKey);
        }
        return copier;
    }

    /**
     * Generate key
     * Generate key description.
     *
     * @param class1 the class 1
     * @param class2 the class 2
     * @return string the string
     */
    private static String generateKey(Class<?> class1, Class<?> class2) {
        return class1.toString() + class2.toString();
    }
}
