package com.xianmao.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xianmao.exception.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @ClassName JackSonUtil
 * @Description: TODO
 * @Author guyi
 * @Data 2019-08-14 09:56
 * @Version 1.0
 */
public class JackSonUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(JackSonUtil.class);

    private JackSonUtil() {
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * 将json字符串转化为对应的Object对象
     * @param jsonStr   字符串
     * @param clazz 对象的class
     * @param <T>   泛型对象
     * @return  输入的对象
     */
    public static <T> T jsonStrToObject(String jsonStr, Class<T> clazz) {
        if (null == jsonStr) {
            return null;
        }
        try {
            return new ObjectMapper().readValue(jsonStr, clazz);
        } catch (IOException e) {
            throw new BizException("JSON对象转换异常");
        }
    }

    /**
     * 将对象转化为json字符串
     * @param object    输入对象
     * @return  对象字符串
     */
    public static String objectToJsonStr(Object object) {
        if (null == object) {
            return null;
        }
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new BizException("JSON对象转换异常");
        }
    }
}
