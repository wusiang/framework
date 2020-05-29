package com.xianmao.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName LoggerUtil
 * @Description: TODO
 * @Author guyi
 * @Data 2019-08-14 10:04
 * @Version 1.0
 */
public class LoggerUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerUtil.class);

    private LoggerUtil() {
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * 打印INFO日志
     *
     * @param LOGGER     LOGGER
     * @param formatStr  formatStr    this is logger {0},{1},{2}
     * @param parameters parameters
     */
    public static void info(Logger LOGGER, String formatStr, Object... parameters) {
        for (int i = 0; i < parameters.length; i++) {
            String replaceStr = "{" + i + "}";
            formatStr = formatStr.replace(replaceStr, "%s");
        }
        formatStr = String.format(formatStr, parameters);
        LOGGER.info(formatStr);
    }

    /**
     * 打印WARN日志
     *
     * @param LOGGER     LOGGER
     * @param formatStr  formatStr   this is logger {0},{1},{2}
     * @param parameters parameters
     */
    public static void warn(Logger LOGGER, String formatStr, Object... parameters) {
        for (int i = 0; i < parameters.length; i++) {
            String replaceStr = "{" + i + "}";
            formatStr = formatStr.replace(replaceStr, "%s");
        }
        formatStr = String.format(formatStr, parameters);
        LOGGER.warn(formatStr);
    }

    /**
     * 打印ERROR日志
     *
     * @param LOGGER     LOGGER
     * @param formatStr  formatStr   this is logger {0},{1},{2}
     * @param parameters parameters
     */
    public static void error(Logger LOGGER, String formatStr, Object... parameters) {
        for (int i = 0; i < parameters.length; i++) {
            String replaceStr = "{" + i + "}";
            formatStr = formatStr.replace(replaceStr, "%s");
        }
        formatStr = String.format(formatStr, parameters);
        LOGGER.error(formatStr);
    }
}
