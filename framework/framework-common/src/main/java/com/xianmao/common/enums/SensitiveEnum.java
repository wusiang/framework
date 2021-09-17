package com.xianmao.common.enums;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName KeywordTypeEnum
 * @Description: 待脱敏关键字类型
 * @Author guyi
 * @Data 2019-12-30 21:32
 * @Version 1.0
 */
public enum SensitiveEnum {
    /**
     * 中文名 (张三 → 张*)
     */
    CHINESE_NAME("(?<=.{1}).", "*"),

    /**
     * 密码
     */
    PASSWORD(".", ""),

    /**
     * 身份证号
     */
    ID_CARD("(?<=\\w{3})\\w(?=\\w{4})", "*"),

    /**
     * 座机号
     */
    FIXED_PHONE("(?<=\\w{3})\\w(?=\\w{2})", "*"),

    /**
     * 手机号
     */
    MOBILE_PHONE("(?<=\\w{3})\\w(?=\\w{4})", "*"),

    /**
     * 地址
     */
    ADDRESS("(.{5}).+(.{4})", "$1*****$2"),

    /**
     * 电子邮件
     */
    EMAIL("(\\w+)\\w{3}@(\\w+)", "$1***@$2"),

    /**
     * 银行卡
     */
    BANK_CARD("(?<=\\w{4})\\w(?=\\w{4})", "*"),

    /**
     * 公司开户银行联号
     */
    CNAPS_CODE("(?<=\\w{4})\\w(?=\\w{4})", "*"),

    /**
     * 默认值
     */
    DEFAULT_TYPE("", "");

    /**
     * 输入格式(1,2,2)
     */
    private final String pattern;
    private final String targetChar;


    SensitiveEnum(String pattern, String targetChar) {
        this.pattern = pattern;
        this.targetChar = targetChar;
    }

    public String getPattern() {
        return pattern;
    }


    public String getTargetChar() {
        return targetChar;
    }

    public static String filter(final SensitiveEnum type, String content) {
        Pattern pattern = Pattern.compile(type.getPattern());
        Matcher matcher = pattern.matcher(content);
        return matcher.replaceAll(type.getTargetChar()).trim();
    }
}
