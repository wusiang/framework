package com.xianmao.id;

import java.util.UUID;

/**
 * @ClassName id生成工具类
 * @Description: 默认提供UUID
 * @Author wjh
 * @Data 2020-06-04 12:33
 * @Version 1.0
 */
public class DefaultIdeable implements Ideable<String> {

    @Override
    public String generateId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
