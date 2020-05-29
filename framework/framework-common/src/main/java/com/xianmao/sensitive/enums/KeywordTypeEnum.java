package com.xianmao.sensitive.enums;

/**
 * @ClassName KeywordTypeEnum
 * @Description: 待脱敏关键字类型
 * @Author guyi
 * @Data 2019-12-30 21:32
 * @Version 1.0
 */
public enum KeywordTypeEnum {
    /**
     * 真实姓名
     */
    TRUE_NAME("trueName","真实姓名"),
    /**
     * 身份证号码
     */
    ID_CARD_NO("idCardNo","身份证号码"),
    /**
     * 银行卡号
     */
    BANKCARD_NO("bankcardNo","银行卡号"),
    /**
     * 手机号码
     */
    PHONE_NO("phoneNo","手机号码"),
    /**
     * 其它
     */
    OTHER("other","其它");

    private String keywordType;
    private String desc;

    KeywordTypeEnum(String keywordType, String desc) {
        this.keywordType = keywordType;
        this.desc = desc;
    }

    /**
     * 通过keyword获取对象
     */
    public static KeywordTypeEnum getMessageType(String keywordType) {
        for (KeywordTypeEnum c : KeywordTypeEnum.values()) {
            if (c.getKeywordType().equals(keywordType)) {
                return c;
            }
        }
        return null;
    }

    public String getKeywordType() {
        return keywordType;
    }

    public String getDesc() {
        return desc;
    }
}
