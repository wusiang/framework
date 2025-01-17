package com.xianmao.common.redis.config.redisson;


import com.xianmao.common.redis.util.ApplicationContextUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;

/**
 * org.redisson.api.RedissonClient帮助类
 * @author wujh
 * @date 2019/5/16
 * @since 1.8
 */
public class RedissonClientHelper {

    /**
     * 获取客户端名称
     * @return 返回客户端名称
     */
    public static String getClientName() {
        return "redisson";
    }

    /**
     * 获取客户端类型
     * @return 返回客户端类型
     */
    public static Class getClientType() {
        return RedissonClient.class;
    }

    /**
     * 创建客户端
     * @param dbIndex 数据库索引
     * @return 返回客户端
     */
    public static RedissonClient createClient(int dbIndex) {
        return Redisson.create(
                ApplicationContextUtil.getContext()
                        .getBean(RedissonAutoConfiguration.class)
                        .createConfig(dbIndex)
        );
    }
}
