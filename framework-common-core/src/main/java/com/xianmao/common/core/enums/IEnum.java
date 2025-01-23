package com.xianmao.common.core.enums;

import cn.hutool.core.util.ObjectUtil;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
     * 获取指定枚举类的所有枚举值
     *
     * @param <E>   枚举类型
     * @param clazz 枚举类的 Class 对象
     * @return 包含所有枚举值的列表
     */
    static <E extends Enum<E>> List<E> getAllEnums(Class<E> clazz) {
        // 确保传入的枚举类不为空
        Objects.requireNonNull(clazz, "枚举类不能为 null");
        return Arrays.stream(clazz.getEnumConstants()).collect(Collectors.toList());
    }

    /**
     * 根据值获取对应的枚举实例
     *
     * @param <E>   枚举类型，该枚举类型必须实现 IEnum 接口
     * @param value 用于匹配的 value 值
     * @param clazz 枚举类的 Class 对象
     * @return 匹配到的枚举实例的 Optional 对象，如果未匹配到则返回空的 Optional
     */
    static <E extends Enum<E> & IEnum> Optional<E> fromValue(Object value, Class<E> clazz) {
        // 确保传入的 value 和枚举类都不为空
        Objects.requireNonNull(value, "value 不能为 null");
        Objects.requireNonNull(clazz, "枚举类不能为 null");
        return Arrays.stream(clazz.getEnumConstants())
                .filter(e -> ObjectUtil.equal(e.getValue(), value))
                .findFirst();
    }
}