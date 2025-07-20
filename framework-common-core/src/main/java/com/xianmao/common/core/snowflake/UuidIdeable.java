package com.xianmao.common.core.snowflake;

import java.util.UUID;

public class UuidIdeable implements Ideable<String> {

    @Override
    public String generateId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
