package com.xianmao.utils;

import java.util.Map;

/**
 * @ClassName MapUtil
 * @Description: TODO
 * @Author guyi
 * @Data 2019-12-15 17:25
 * @Version 1.0
 */
public class MapUtil {
    /**
     * 判断Map是否为空
     *
     * @param map 待判断的Map
     * @return 是否为空，true：空；false：不为空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        if (map == null)
            return true;

        return map.isEmpty();
    }

    /**
     * 判断Map是否不为空
     *
     * @param map 待判断的Map
     * @return 是否不为空，true：不为空；false：空
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }
}
