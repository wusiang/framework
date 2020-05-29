package com.xianmao.support;

import com.xianmao.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName SqlFilter
 * @Description: SQL过滤
 * @Author guyi
 * @Data 2019-12-30 21:27
 * @Version 1.0
 */
public class SqlFilter {

    /**
     * SQL注入过滤
     * @param str  待验证的字符串
     */
    public static String sqlInject(String str){
        if(StringUtils.isBlank(str)){
            return null;
        }
        //去掉'|"|;|\字符
        str = StringUtil.replace(str, "'", "");
        str = StringUtil.replace(str, "\"", "");
        str = StringUtil.replace(str, ";", "");
        str = StringUtil.replace(str, "\\", "");

        //转换成小写
        str = str.toLowerCase();

        //非法字符
        String[] keywords = {"master", "truncate", "insert", "select", "delete", "update", "declare", "alert", "drop"};

        //判断是否包含非法字符
        for(String keyword : keywords){
            if(str.indexOf(keyword) != -1){
                throw new RuntimeException("包含非法字符");
            }
        }

        return str;
    }
}
