package com.xianmao.config;

import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.Nullable;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @ClassName RedisKeySerializer
 * @Description: TODO
 * @Author wjh
 * @Data 2020-05-04 22:24
 * @Version 1.0
 */
public class RedisKeySerializer implements RedisSerializer<Object> {

    private final Charset charset;
    private final ConversionService converter;

    public RedisKeySerializer() {
        this(StandardCharsets.UTF_8);
    }

    public RedisKeySerializer(Charset charset) {
        Objects.requireNonNull(charset, "Charset must not be null");
        this.charset = charset;
        this.converter = DefaultConversionService.getSharedInstance();
    }

    @Override
    public Object deserialize(byte[] bytes) {
        // redis keys 会用到反序列化
        if (bytes == null) {
            return null;
        }
        return new String(bytes, charset);
    }

    @Override
    @Nullable
    public byte[] serialize(Object object) {
        Objects.requireNonNull(object, "redis key is null");
        String key;
        if (object instanceof SimpleKey) {
            key = "";
        } else if (object instanceof String) {
            key = (String) object;
        } else {
            key = converter.convert(object, String.class);
        }
        return key.getBytes(this.charset);
    }
}
