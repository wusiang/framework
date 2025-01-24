package com.xianmao.common.core.enums;

import java.io.Serializable;
import java.util.Arrays;

public interface IEnum<C, V> extends Serializable {

    /**
     * 获取 code 值
     *
     * @return code 值
     */
    C getCode();

    /**
     * 获取 value 值
     *
     * @return value 值
     */
    V getValue();


    /**
     * 通用的根据 code 查询枚举对象的方法
     */
    public static <E extends Enum<E> & IEnum<C, V>, C, V> E getByCode(C code, Class<E> eClass) {
        if (code == null || eClass == null) {
            return null;
        }
        return Arrays.stream(eClass.getEnumConstants())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}