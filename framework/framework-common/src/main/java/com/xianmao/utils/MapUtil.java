package com.xianmao.utils;

import java.util.*;

/**
 * @ClassName MapUtil
 * @Description: TODO
 * @Author guyi
 * @Data 2019-12-15 17:25
 * @Version 1.0
 */
public class MapUtil {

    private MapUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * 判断Map是否为空
     *
     * @param map 待判断的Map
     * @return 是否为空，true：空；false：不为空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        if (map == null) {
            return true;
        }
        return map.isEmpty();
    }

    /**
     * 判断Map是否不为空
     *
     * @param map 待判断的Map
     * @return 是否不为空，true：不为空；false：空
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * 获取value
     *
     * @param map          源
     * @param key          字段
     * @param defaultValue 默认值
     * @return
     */
    public static String getString(Map map, Object key, String defaultValue) {
        return Converter.toString(map.get(key), defaultValue);
    }

    /**
     * 获取value
     *
     * @param map          源
     * @param key          字段
     * @param defaultValue 默认值
     * @return
     */
    public static <K> Long getLong(Map map, K key, Long defaultValue) {
        return Converter.toLong(map.get(key), defaultValue);
    }

    /**
     * 获取value
     *
     * @param map          源
     * @param key          字段
     * @param defaultValue 默认值
     * @return
     */
    public static <K> Integer getInteger(Map map, K key, Integer defaultValue) {
        return Converter.toInt(map.get(key), defaultValue);
    }

    /**
     * 获取value
     *
     * @param map          源
     * @param key          字段
     * @param defaultValue 默认值
     * @return
     */
    public static <K> Float getFloat(Map map, K key, Float defaultValue) {
        return Converter.toFloat(map.get(key), defaultValue);
    }

    /**
     * 获取value
     *
     * @param map          源
     * @param key          字段
     * @param defaultValue 默认值
     * @return
     */
    public static <K> Double getDouble(Map map, K key, Double defaultValue) {
        return Converter.toDouble(map.get(key), defaultValue);
    }

    /**
     * 排序已有Map，Key有序的Map，使用默认Key排序方式（字母顺序）
     *
     * @param <K> key的类型
     * @param <V> value的类型
     * @param map Map
     * @return TreeMap
     * @since 4.0.1
     */
    public static <K, V> TreeMap<K, V> sort(Map<K, V> map) {
        return sort(map, null);
    }

    /**
     * 排序已有Map，Key有序的Map
     *
     * @param <K>        key的类型
     * @param <V>        value的类型
     * @param map        Map，为null返回null
     * @param comparator Key比较器
     * @return TreeMap，map为null返回null
     */
    public static <K, V> TreeMap<K, V> sort(Map<K, V> map, Comparator<? super K> comparator) {
        if (null == map) {
            return null;
        }

        TreeMap<K, V> result;
        if (map instanceof TreeMap) {
            result = (TreeMap<K, V>) map;
            if (null == comparator || comparator.equals(result.comparator())) {
                return result;
            }
        } else {
            result = newTreeMap(map, comparator);
        }

        return result;
    }

    /**
     * 新建TreeMap，Key有序的Map
     *
     * @param <K>        key的类型
     * @param <V>        value的类型
     * @param map        Map
     * @param comparator Key比较器
     * @return TreeMap
     * @since 3.2.3
     */
    public static <K, V> TreeMap<K, V> newTreeMap(Map<K, V> map, Comparator<? super K> comparator) {
        final TreeMap<K, V> treeMap = new TreeMap<>(comparator);
        if (!isEmpty(map)) {
            treeMap.putAll(map);
        }
        return treeMap;
    }
}
