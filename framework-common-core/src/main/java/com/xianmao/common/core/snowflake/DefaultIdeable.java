package com.xianmao.common.core.snowflake;

import java.util.UUID;

public class DefaultIdeable implements Ideable<String> {

    @Override
    public String generateId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
