package com.xianmao.date;

import java.time.format.DateTimeFormatter;

/**
 * @ClassName DatePattern
 * @Description: java 8 时间格式化
 * @Author wjh
 * @Data 2020-06-04 11:54
 * @Version 1.0
 */
public class DatePattern {

    /**
     * 常规格式 <code>yyyy-MM-dd HH:mm:ss</code>
     */
    public final static String NORM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 常规格式化 <code>yyyy-MM-dd HH:mm:ss</code>
     */
    public final static DateTimeFormatter NORM_DATETIME_FORMAT = DateTimeFormatter.ofPattern(NORM_DATETIME_PATTERN);

    /**
     * 常规格式 <code>yyyy-MM-dd</code>
     */
    public final static String NORM_DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 常规格式化 <code>yyyy-MM-dd</code>
     */
    public final static DateTimeFormatter NORM_DATE_FORMAT = DateTimeFormatter.ofPattern(NORM_DATE_PATTERN);

    /**
     * 纯净的日期格式 <code>yyyyMMddHHmmss</code>
     */
    public final static String PURE_DATETIME_PATTERN = "yyyyMMddHHmmss";

    /**
     * 纯净的日期格式化 <code>yyyyMMddHHmmss</code>
     */
    public final static DateTimeFormatter PURE_ATETIME_FORMAT = DateTimeFormatter.ofPattern(PURE_DATETIME_PATTERN);
    /**
     * 纯净的日期格式 <code>yyyyMMdd</code>
     */
    public final static String PURE_DATE_PATTERN = "yyyyMMdd";

    /**
     * 纯净的日期格式化 <code>yyyyMMdd</code>
     */
    public final static DateTimeFormatter PURE_DATE_FORMAT = DateTimeFormatter.ofPattern(PURE_DATE_PATTERN);

    /**
     * 中文的日期格式 <code>yyyy年MM月dd日</code>
     */
    public final static String CHINESE_DATE_PATTERN = "yyyy年MM月dd日";

    /**
     * * 中文的日期格式化 <code>yyyy年MM月dd日</code>
     */
    public final static DateTimeFormatter CHINESE_DATE_FORMAT = DateTimeFormatter.ofPattern(CHINESE_DATE_PATTERN);

    /**
     * 中文的日期格式 <code>HH:mm:ss</code>
     */
    public final static String PATTERN_TIME = "HH:mm:ss";

    /**
     * 中文的日期格式 <code>HH:mm:ss</code>
     */
    public final static DateTimeFormatter PATTERN_TIME_FORMAT = DateTimeFormatter.ofPattern(PATTERN_TIME);
}
