package com.xianmao.id;

public interface BaseIdeable<T> {
    /**
     * id生成
     *
     * @return id结果
     */
    T generateId();
}
