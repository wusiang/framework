package com.xianmao.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONLibDataFormatSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;
import java.util.Map;

/**
 * @ClassName FastjsonUtil
 * @Description: TODO
 * @Author guyi
 * @Data 2020-03-06 18:14
 * @Version 1.0
 */
public class FastjsonUtil {
    private static final SerializeConfig config;

    private FastjsonUtil() {
    }

    static {
        config = new SerializeConfig();
        config.put(java.util.Date.class, new JSONLibDataFormatSerializer());
        config.put(java.sql.Date.class, new JSONLibDataFormatSerializer());
    }

    private static final SerializerFeature[] features = {
            SerializerFeature.WriteMapNullValue, // 输出空置字段
            SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null
            SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null
            SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null
            SerializerFeature.WriteNullStringAsEmpty // 字符类型字段如果为null，输出为""，而不是null
    };

    /**
     * 将json字符串转换为对应的对象
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T toObject(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    /**
     * 转换为数组
     *
     * @param json
     * @param <T>
     * @return
     */
    public static <T> Object[] toArray(String json) {
        return toArray(json, null);
    }

    /**
     * 转换为数组
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Object[] toArray(String json, Class<T> clazz) {
        return JSON.parseArray(json, clazz).toArray();
    }

    /**
     * 转换为List
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> toList(String json, Class<T> clazz) {
        return JSON.parseArray(json, clazz);
    }

    /**
     * json字符串转化为map
     *
     * @param json
     * @return
     */
    public static Map toMap(String json) {
        return JSONObject.parseObject(json);
    }

    /**
     * 将对象按照默认配置和特性转换为json字符串
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        return JSON.toJSONString(object, config, features);
    }

    /**
     * 将对象按照默认配置转换为json字符串
     *
     * @param object
     * @return
     */
    public static String toJsonOfFeatures(Object object) {
        return JSON.toJSONString(object, config);
    }

    /**
     * 将map转化为string
     *
     * @param map
     * @return
     */
    public static String mapToJson(Map map) {
        return JSONObject.toJSONString(map);
    }
}
