package com.xianmao.common.core.snowflake;

public interface Ideable<T> {
    /**
     * id生成
     *
     * @return id结果
     */
    T generateId();
}
