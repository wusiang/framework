package com.xianmao.obj;

/**
 * @ClassName ObjectUtil
 * @Description: TODO
 * @Author guyi
 * @Data 2019-12-30 21:01
 * @Version 1.0
 */
public class ObjectUtil extends org.springframework.util.ObjectUtils {

    private ObjectUtil() {
    }

    /**
     * 判断对象是否为空
     *
     * @param object 对象
     * @return 若为空则返回true、不为空返回false
     */
    public static boolean isEmpty(Object object) {
        return object == null;
    }

    /**
     * 判断对象是否不为空
     *
     * @param object 对象
     * @return 若为空则返回false、不为空返回true
     */
    public static boolean isNotEmpty(Object object) {
        return !isEmpty(object);
    }
}
