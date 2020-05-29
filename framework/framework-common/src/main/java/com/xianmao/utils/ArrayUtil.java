package com.xianmao.utils;

import java.util.Arrays;

/**
 * @ClassName ArrayUtil
 * @Description: TODO
 * @Author guyi
 * @Data 2019-08-14 10:18
 * @Version 1.0
 */
public class ArrayUtil {
    private ArrayUtil() {
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    public static <T> T[] concat(T[] srouce1, T[] source2) {
        T[] result = Arrays.copyOf(srouce1, srouce1.length + source2.length);
        System.arraycopy(source2, 0, result, srouce1.length, source2.length);
        return result;
    }
}
