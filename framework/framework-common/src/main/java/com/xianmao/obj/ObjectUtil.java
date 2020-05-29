package com.xianmao.obj;

import org.springframework.lang.Nullable;

/**
 * @ClassName ObjectUtil
 * @Description: TODO
 * @Author guyi
 * @Data 2019-12-30 21:01
 * @Version 1.0
 */
public class ObjectUtil extends org.springframework.util.ObjectUtils  {

    private ObjectUtil() {}

    /**
     * 判断元素不为空
     * @param obj object
     * @return boolean
     */
    public static boolean isNotEmpty(@Nullable Object obj) {
        return !ObjectUtil.isEmpty(obj);
    }
}
