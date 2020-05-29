package com.xianmao.utils;

import com.xianmao.exception.BizException;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

/**
 * @ClassName AssertUtil
 * @Description: TODO
 * @Author guyi
 * @Data 2019-08-14 09:35
 * @Version 1.0
 */
public class AssertUtil {
    private AssertUtil() {
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * 断言不为空；如果为null || ""  "  "    抛出异常终止执行
     *
     * @param logger Logger
     * @param value  待检查值
     * @param msg    异常返回信息
     */
    public static void isNotEmpty(Logger logger, String value, String msg) {
        if (null == value || "".equals(value.trim())) {
            logger.error(msg);
            throw new BizException(msg);
        }
    }

    /**
     * 断言不为空；如果为null 抛出异常终止执行
     *
     * @param logger Logger
     * @param value  待检查值
     * @param msg    异常返回信息
     */
    public static void isNotNull(Logger logger, Object value, String msg) {
        if (null == value) {
            logger.error(msg);
            throw new BizException(msg);
        }
    }

    /**
     * 断言不为空；如果为null || list.size() == 0    抛出异常终止执行
     *
     * @param logger Logger
     * @param list   待检查值
     * @param msg    异常返回信息
     */
    public static void isNotEmpty(Logger logger, List list, String msg) {
        if (null == list || list.size() == 0) {
            logger.error(msg);
            throw new BizException(msg);
        }
    }

    /**
     * 断言不为空；如果为null || map.size() == 0     抛出异常终止执行
     *
     * @param logger Logger
     * @param map    待检查值
     * @param msg    异常返回信息
     */
    public static void isNotEmpty(Logger logger, Map map, String msg) {
        if (null == map || map.size() == 0) {
            logger.error(msg);
            throw new BizException(msg);
        }
    }

    /**
     * 断言为true 如果为不为true 抛出异常终止执行
     *
     * @param logger Logger
     * @param value  待检查值
     * @param msg    异常返回信息
     */
    public static void isTrue(Logger logger, Boolean value, String msg) {
        if (null == value || !value) {
            logger.error(msg);
            throw new BizException(msg);
        }
    }


    /**
     * 断言为true 如果为不为false 抛出异常终止执行
     *
     * @param logger Logger
     * @param value  待检查值
     * @param msg    异常返回信息
     */
    public static void isFalse(Logger logger, Boolean value, String msg) {
        if (null == value || value) {
            logger.error(msg);
            throw new BizException(msg);
        }
    }

    /**
     * 断言为空；如果为null 抛出异常终止执行
     *
     * @param logger Logger
     * @param value  待检查值
     * @param msg    异常返回信息
     */
    public static void isNull(Logger logger, Object value, String msg) {
        if (null == value) {
            logger.error(msg);
            throw new BizException(msg);
        }
    }
}
