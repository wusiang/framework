package com.xianmao.common.core.utils;

import cn.hutool.core.util.StrUtil;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PinyinUtils {

    private static final String CHINESE_REGEX = "[\\u4e00-\\u9fa5]";

    /**
     * 获取汉字拼音首字母
     */
    public static String getPinyinHeader(String str) {
        if (StrUtil.isBlank(str)) {
            return "";
        }
        StringBuilder convert = new StringBuilder();
        Pattern pattern = Pattern.compile(CHINESE_REGEX);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            String chineseChar = matcher.group();
            HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
            format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
            format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            try {
                String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(chineseChar.charAt(0), format);
                if (pinyinArray != null && pinyinArray.length > 0) {
                    convert.append(pinyinArray[0].charAt(0));
                }
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }
        }
        return convert.toString();

    }


    /**
     * 中文则获取拼音首字母，英文直接获取首字母
     */
    public static String getPinyinHeaderOrFirstWord(String str) {
        if (StrUtil.isEmpty(str)) {
            return "";
        }
        StringBuilder convert = new StringBuilder();
        String newStr = str.replaceAll(CHINESE_REGEX, "");
        if (!newStr.isEmpty()) {
            for (int j = 0; j < newStr.length(); j++) {
                char word = newStr.charAt(j);
                String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
                if (pinyinArray != null) {
                    convert.append(pinyinArray[0].charAt(0));
                } else {
                    convert.append(word);
                }
            }
        } else {
            convert.append(str.charAt(0));
        }
        return convert.toString().toUpperCase();
    }

    /**
     * 获取汉字拼音首字母大写
     */
    public static String getPinyin(String china) {
        if (StrUtil.isBlank(china)) {
            return "";
        }
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        char[] arrays = china.trim().toCharArray();
        StringBuilder result = new StringBuilder();
        try {
            for (char ti : arrays) {
                if (Character.toString(ti).matches(CHINESE_REGEX)) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(ti, format);
                    if (temp != null && temp.length > 0) {
                        // 将拼音首字母大写
                        String pinyin = temp[0];
                        String capitalizedPinyin = StrUtil.upperFirst(pinyin);
                        result.append(capitalizedPinyin);
                    }
                } else {
                    result.append(ti);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }

        return result.toString();
    }
}
