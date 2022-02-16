package com.xianmao.common.string;

import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.util.ReUtil;
import org.apache.commons.lang3.StringUtils;

public class CrypStrUtil {

    /**
     * 手机号隐藏中间4位
     *
     * @param mobile
     * @return
     */
    public static String crypMobile(final String mobile) {
        if (StringUtils.isEmpty(mobile) || !ReUtil.isMatch(PatternPool.MOBILE, mobile)) { return mobile; }
        return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 身份证隐藏中间8位
     * @param idCard
     * @return
     */
    public static String crypIdCard(final String idCard) {
        if (StringUtils.isEmpty(idCard) || !ReUtil.isMatch(PatternPool.CITIZEN_ID, idCard)) {
            return idCard;
        }
        return idCard.replaceAll("(\\d{6})\\d{8}(\\d{4})", "$1**********$2");
    }

    /**
     * 隐藏字符
     *
     * @param str
     * @return
     */
    public static String crypStr(final String str) {
        if (StringUtils.isEmpty(str)) { return str; }
        return crypStrLen(str, 6);
    }

    /**
     * 字符替换*
     *
     * @return
     */
    public static String crypStrLen(final String str) {
        return crypStrLen(str, str.length());
    }

    public static String crypStrLen(final String str, final int length) {
        if (StringUtils.isEmpty(str)) { return str; }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) { sb.append("*"); }
        return sb.toString();
    }

    /**
     * 隐藏中间汉字
     *
     * @param name
     * @return
     */
    public static String crypName(final String name) {
        if (StringUtils.isEmpty(name)) { return name; }

        int len = name.length();
        String first = name.substring(0, 1), last = name.substring(len - 1),
                crypLen = crypStrLen(name, len -2), crypName = name;
        switch (name.length()) {
            case 1:
                crypName = "*";
                break;
            case 2:
                crypName = first + "*";
                break;
            default:
                crypName = first + crypLen + last;
                break;
        }
        return crypName;
    }
}
