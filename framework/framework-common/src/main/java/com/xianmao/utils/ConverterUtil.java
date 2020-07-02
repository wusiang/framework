package com.xianmao.utils;

import com.xianmao.number.NumberUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @ClassName ConverterUtil
 * @Description: TODO
 * @Author wjh
 * @Data 2020/7/1 5:11 下午
 * @Version 1.0
 */
public class ConverterUtil {

    private ConverterUtil() {
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    ;

    /**
     * 强转string,并去掉多余空格
     *
     * @param obj 字符串
     * @return String
     */
    public static String toString(Object obj) {
        return toString(obj, "");
    }

    /**
     * 强转string,并去掉多余空格
     *
     * @param obj          需要转换为string的对象
     * @param defaultValue 默认值
     * @return obj转换为string
     */
    public static String toString(Object obj, String defaultValue) {
        if (null == obj) {
            return defaultValue;
        }
        return String.valueOf(obj);
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
     * @param obj 需要转换的对象
     * @return 如果obj为空则返回默认的0l，不为空则返回转换后的long结果
     */
    public static Long toLong(Object obj) {
        return toLong(obj, 0l);
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
     * 将obj 转换为布尔类型
     *
     * @param obj 对象
     * @return 转换为boolean 的结果
     */
    public static Boolean toBoolean(Object obj) {
        return toBoolean(obj, false);
    }

    /**
     * 将obj 转换为布尔类型
     *
     * @param obj 对象
     * @return 转换为boolean 的结果
     */
    public static Boolean toBoolean(Object obj, Boolean defaultValue) {
        if (obj != null) {
            String val = String.valueOf(obj);
            val = val.toLowerCase().trim();
            return Boolean.parseBoolean(val);
        }
        return defaultValue;
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
     * 转换为Integer集合<br>
     *
     * @param str 结果被转换的值
     * @return 结果
     */
    public static List<Integer> toIntList(String str) {
        return Arrays.asList(toIntArray(str));
    }

    /**
     * 转换为Integer集合<br>
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果
     */
    public static List<Integer> toIntList(String split, String str) {
        return Arrays.asList(toIntArray(split, str));
    }

    /**
     * 转换为Integer数组<br>
     *
     * @param str 被转换的值
     * @return 结果
     */
    public static Integer[] toIntArray(String str) {
        return toIntArray(",", str);
    }

    /**
     * 转换为Integer数组<br>
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果
     */
    public static Integer[] toIntArray(String split, String str) {
        if (StringUtil.isEmpty(str)) {
            return new Integer[]{};
        }
        String[] arr = str.split(split);
        final Integer[] ints = new Integer[arr.length];
        for (int i = 0; i < arr.length; i++) {
            final Integer v = toInt(arr[i], 0);
            ints[i] = v;
        }
        return ints;
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
     * 转换为String数组<br>
     *
     * @param str 被转换的值
     * @return 结果
     */
    public static String[] toStringArray(String str) {
        return toStringArray(",", str);
    }

    /**
     * 转换为String数组<br>
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果
     */
    public static String[] toStringArray(String split, String str) {
        if (StringUtils.isBlank(str)) {
            return new String[]{};
        }
        return str.split(split);
    }

    /**
     * 转换为String集合<br>
     *
     * @param str 结果被转换的值
     * @return 结果
     */
    public static List<String> toStringList(String str) {
        return Arrays.asList(toStringArray(str));
    }

    /**
     * 转换为String集合<br>
     *
     * @param split 分隔符
     * @param str   被转换的值
     * @return 结果
     */
    public static List<String> toStringList(String split, String str) {
        return Arrays.asList(toStringArray(split, str));
    }

    /**
     * 将 long 转短字符串 为 62 进制
     *
     * @param num 数字
     * @return 短字符串
     */
    public static String to62String(long num) {
        return NumberUtil.to62String(num);
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
