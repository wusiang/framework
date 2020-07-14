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

    /**
     * 如果给定对象为{@code null}返回默认值
     *
     * <pre>
     * ObjectUtil.defaultIfNull(null, null)      = null
     * ObjectUtil.defaultIfNull(null, "")        = ""
     * ObjectUtil.defaultIfNull(null, "zz")      = "zz"
     * ObjectUtil.defaultIfNull("abc", *)        = "abc"
     * ObjectUtil.defaultIfNull(Boolean.TRUE, *) = Boolean.TRUE
     * </pre>
     *
     * @param <T>          对象类型
     * @param object       被检查对象，可能为{@code null}
     * @param defaultValue 被检查对象为{@code null}返回的默认值，可以为{@code null}
     * @return 被检查对象为{@code null}返回默认值，否则返回原值
     * @since 3.0.7
     */
    public static <T> T defaultIfNull(final T object, final T defaultValue) {
        return (null != object) ? object : defaultValue;
    }
}
