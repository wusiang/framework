package com.xianmao.common.id;

public interface Ideable<T> {
    /**
     * id生成
     *
     * @return id结果
     */
    T generateId();
}
