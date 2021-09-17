package com.xianmao.common.date;

import java.text.SimpleDateFormat;

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
    public final static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 常规格式化 <code>yyyy-MM-dd HH:mm:ss</code>
     */
    public final static SimpleDateFormat YYYY_MM_DD_HH_MM_SS_FORMAT = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);

    /**
     * 常规格式 <code>yyyy-MM-dd</code>
     */
    public final static String YYYY_MM_DD = "yyyy-MM-dd";

    /**
     * 常规格式化 <code>yyyy-MM-dd</code>
     */
    public final static SimpleDateFormat YYYY_MM_DD_FORMAT = new SimpleDateFormat(YYYY_MM_DD);

    /**
     * 纯净的日期格式 <code>yyyyMMddHHmmss</code>
     */
    public final static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    /**
     * 纯净的日期格式化 <code>yyyyMMddHHmmss</code>
     */
    public final static SimpleDateFormat YYYYMMDDHHMMSS_FORMAT = new SimpleDateFormat(YYYYMMDDHHMMSS);
    /**
     * 纯净的日期格式 <code>yyyyMMdd</code>
     */
    public final static String YYYYMMDD = "yyyyMMdd";

    /**
     * 纯净的日期格式化 <code>yyyyMMdd</code>
     */
    public final static SimpleDateFormat YYYYMMDD_FORMAT = new SimpleDateFormat(YYYYMMDD);

    /**
     * 标准日期格式：yyyy年MM月dd日 HH时mm分ss秒
     */
    public static final String YYYY年MM月DD日HH时MM分SS秒 = "yyyy年MM月dd日HH时mm分ss秒";
    /**
     * 标准日期格式
     */
    public static final SimpleDateFormat YYYY年MM月DD日HH时MM分SS秒_FORMAT = new SimpleDateFormat(YYYY年MM月DD日HH时MM分SS秒);

    /**
     * 中文的日期格式 <code>yyyy年MM月dd日</code>
     */
    public final static String YYYY年MM月DD日 = "yyyy年MM月dd日";

    /**
     * * 中文的日期格式化 <code>yyyy年MM月dd日</code>
     */
    public final static SimpleDateFormat YYYY年MM月DD日_FORMAT = new SimpleDateFormat(YYYY年MM月DD日);

    /**
     * 中文的日期格式 <code>HH:mm:ss</code>
     */
    public final static String HH_MM_SS = "HH:mm:ss";

    /**
     * 中文的日期格式 <code>HH:mm:ss</code>
     */
    public final static SimpleDateFormat HH_MM_SS_FORMAT = new SimpleDateFormat(HH_MM_SS);
}
