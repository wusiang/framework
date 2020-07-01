package com.xianmao.utils;

import java.util.*;

/**
 * @ClassName ConverterUtil
 * @Description: TODO
 * @Author wjh
 * @Data 2020/7/1 5:11 下午
 * @Version 1.0
 */
public class ConverterUtil {

    /**
     * <将obj转换为string，如果obj为null则返回defaultVal>
     *
     * @param obj        需要转换为string的对象
     * @param defaultVal 默认值
     * @return obj转换为string
     */
    public static String toString(Object obj, String defaultVal) {
        if (obj == null) {
            return defaultVal;
        }
        return obj.toString();
    }


    /**
     * <将obj转换为string，默认为空>
     *
     * @param obj 需要转换为string的对象
     * @return 将对象转换为string的字符串
     */
    public static String toString(Object obj) {
        return toString(obj, "");
    }

    /**
     * <将对象转换为int>
     *
     * @param obj        需要转换为int的对象
     * @param defaultVal 默认值
     * @return obj转换成的int值
     */
    public static Integer toInt(Object obj, Integer defaultVal) {
        try {
            return (obj != null) ? Integer.parseInt(toString(obj, "0")) : defaultVal;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultVal;
    }

    /**
     * <将对象转换为int>
     *
     * @param obj 需要转换为int的对象
     * @return obj转换成的int值
     */
    public static Integer toInt(Object obj) {
        return toInt(obj, 0);
    }

    /**
     * <将对象转换为Integer>
     *
     * @param obj 需要转换为Integer的对象
     * @return obj转换成的Integer值
     */
    public static Integer toInteger(Object obj) {
        return toInt(obj, null);
    }

    /**
     * <将对象转换为Float>
     *
     * @param obj 需要转换为Float的对象
     * @return obj转换成的Float值
     */
    public static Float toFloat(Object obj) {
        return toFloat(obj, 0);
    }

    /**
     * <将对象转换为int>
     *
     * @param obj        需要转换为int的对象
     * @param defaultVal 默认值
     * @return obj转换成的int值
     */
    public static Float toFloat(Object obj, float defaultVal) {
        return (obj != null) ? Float.parseFloat(toString(obj, "0")) : defaultVal;
    }

    /**
     * <将obj转换为long>
     *
     * @param obj        需要转换的对象
     * @param defaultVal 默认值
     * @return 如果obj为空则返回默认，不为空则返回转换后的long结果
     */
    public static Long toLong(Object obj, long defaultVal) {
        return (obj != null) ? Long.parseLong(toString(obj)) : defaultVal;
    }

    /**
     * <将obj转换为long>
     *
     * @param obj 需要转换的对象
     * @return 如果obj为空则返回默认的0l，不为空则返回转换后的long结果
     */
    public static Long toLong(Object obj) {
        return toLong(obj, 0l);
    }

    /**
     * 将obj 转换为布尔类型
     *
     * @param obj 对象
     * @return 转换为boolean 的结果
     */
    public static boolean toBoolean(Object obj) {
        if (Objects.isNull(obj)) {
            return false;
        }
        return "true".equals(obj.toString());
    }

    /**
     * 将object转换为double类型，如果出错则返回 defaultVal
     *
     * @param obj        需要转换的对象
     * @param defaultVal 默认值
     * @return 转换后的结果
     */
    public static Double toDouble(Object obj, Double defaultVal) {
        try {
            return Double.parseDouble(obj.toString());
        } catch (Exception e) {
            return defaultVal;
        }
    }

    /**
     * 将object转换为double类型，如果出错则返回 0d
     *
     * @param obj 需要转换的对象
     * @return 转换后的结果
     */
    public static double toDouble(Object obj) {
        return toDouble(obj, 0d);
    }

    /**
     * <将List<Object>转换为List<Map<String, Object>>>
     *
     * @param list 需要转换的list
     * @return 转换的结果
     */
    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> converterForMapList(List<Object> list) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (Object tempObj : list) {
            result.add((HashMap<String, Object>) tempObj);
        }
        return result;
    }

    /**
     * 把char转换为int
     * a 转换为1
     *
     * @param c char
     * @return int
     */
    public static int char2Int(char c) {
        return ((int) c) - 96;
    }
}
