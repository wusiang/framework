package com.xianmao.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName ParamUtil
 * @Description: TODO
 * @Author guyi
 * @Data 2019-12-15 17:17
 * @Version 1.0
 */
public class ParamUtil {

    /**
     * 将Map型转为请求参数型
     *
     * @param data Map类型的参数
     * @return url请求的参数
     * @throws UnsupportedEncodingException 异常
     */
    public static String getUrlParamsByMap(Map<String, String> data) throws UnsupportedEncodingException {
        if (data == null || data.isEmpty()) return null;

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> i : data.entrySet()) {

            sb.append(i.getKey())
                    .append("=")
                    .append(URLEncoder.encode(i.getValue(), "UTF-8"))
                    .append("&");

        }
        String str = sb.toString();

        return str.substring(0, str.length() - 1);
    }

    /**
     * 将url参数转换成map
     *
     * @param param [ellipsis]
     * @return 参数Map
     */
    public static Map<String, String> getUrlParams(String param) {
        Map<String, String> map = new HashMap<String, String>();
        if (StringUtils.isEmpty(param)) {
            return map;
        }
        String[] params = param.split("&");
        for (String param1 : params) {
            String[] p = param1.split("=");
            if (p.length == 2) {
                map.put(p[0], p[1]);
            }
        }
        return map;
    }
}
