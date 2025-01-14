package com.xianmao.common.core.enums;

import cn.hutool.core.util.ObjectUtil;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
     * 获取所有枚举值
     */
    static <E extends Enum<E>> List<E> getAllEnums(Class<E> clazz) {
        return Arrays.stream(clazz.getEnumConstants()).collect(Collectors.toList());
    }

    /**
     * 根据值获取枚举
     */
    static <E extends Enum<E> & IEnum> Optional<E> fromValue(Object value, Class<E> clazz) {
        Objects.requireNonNull(value);
        return Stream.of(clazz.getEnumConstants())
                .filter(e -> ObjectUtil.equal(e.getValue(), value))
                .findFirst();
    }
}