package com.xianmao.common.obj;

import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * @ClassName CollectionUtil
 * @Description: TODO
 * @Author guyi
 * @Data 2019-12-30 21:08
 * @Version 1.0
 */
public class CollectionUtil extends org.springframework.util.CollectionUtils {

    /**
     * Check whether the given Array contains the given element.
     *
     * @param array   the Array to check
     * @param element the element to look for
     * @param <T>     The generic tag
     * @return {@code true} if found, {@code false} else
     */
    public static <T> boolean contains(@Nullable T[] array, final T element) {
        if (array == null) {
            return false;
        }
        return Arrays.stream(array).anyMatch(x -> ObjectUtil.nullSafeEquals(x, element));
    }

    /**
     * 对象是否为数组对象
     *
     * @param obj 对象
     * @return 是否为数组对象，如果为{@code null} 返回false
     */
    public static boolean isArray(Object obj) {
        if (null == obj) {
            return false;
        }
        return obj.getClass().isArray();
    }

    /**
     * 如果提供的Collection为{@code null}或为空，则返回{@code true}。
     * 否则，请返回{@code false}。
     *
     * @param collection 集合
     * @return boolean 给定的Collection是否为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    /**
     * 如果提供的Collection不为{@code null}或不为空，则返回{@code true}。
     * 否则，请返回{@code false}。
     *
     * @param collection 集合
     * @return boolean 给定的Collection是否不为空
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 如果提供的数组对象为为{@code null}或为空，则返回{@code true}。
     * 否则，请返回{@code false}。
     *
     * @param obj 检查这个数组
     * @return 给定的数组是否为空
     */
    public static boolean isEmpty(Object[] obj) {
        return obj == null || obj.length == 0;
    }

    /**
     * 如果提供的数组不为{@code null}或不为空，则返回{@code true}。
     * 否则，请返回{@code false}。
     *
     * @param obj 检查这个obj
     * @return 给定的数组是否不为空
     */
    public static boolean isNotEmpty(Object[] obj) {
        return !isEmpty(obj);
    }

    /**
     * 如果提供的Map为{@code null}或为空，则返回{@code true}。
     * 否则，请返回{@code false}。
     *
     * @param map 检查这个map
     * @return 给定的Map是否为空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }

    /**
     * 如果提供的Map不为{@code null}或不为空，则返回{@code true}。
     * 否则，请返回{@code false}。
     *
     * @param map 检查这个map
     * @return 给定的Map是否不为空
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }
}
