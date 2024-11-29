package com.xianmao.common.redis.handle;

import org.springframework.data.redis.core.HyperLogLogOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

/**
 * 基数助手
 * @author wujh
 * @date 2019/4/22
 * @since 1.8
 */
public final class HyperLogLogHandler implements RedisHandler {
    /**
     * 对象模板
     */
    private RedisTemplate<String, Object> redisTemplate;
    /**
     * 字符串模板
     */
    private StringRedisTemplate stringRedisTemplate;
    /**
     * 对象模板
     */
    private HyperLogLogOperations<String, Object> hyperLogLogOperations;
    /**
     * 字符串模板
     */
    private HyperLogLogOperations<String, String> stringHyperLogLogOperations;

    /**
     * 基数助手构造
     * @param dbIndex 数据库索引
     */
    @SuppressWarnings("unchecked")
    HyperLogLogHandler(Integer dbIndex) {
        List<RedisTemplate> templateList = HandlerManager.createTemplate(dbIndex);
        this.redisTemplate = templateList.get(0);
        this.stringRedisTemplate = (StringRedisTemplate) templateList.get(1);
        this.hyperLogLogOperations = redisTemplate.opsForHyperLogLog();
        this.stringHyperLogLogOperations = stringRedisTemplate.opsForHyperLogLog();
    }

    /**
     * 基数助手构造
     * @param transactionHandler 事务助手
     */
    HyperLogLogHandler(TransactionHandler transactionHandler) {
        this.redisTemplate = transactionHandler.getRedisTemplate();
        this.stringRedisTemplate = transactionHandler.getStringRedisTemplate();
        this.hyperLogLogOperations = this.redisTemplate.opsForHyperLogLog();
        this.stringHyperLogLogOperations = this.stringRedisTemplate.opsForHyperLogLog();
    }

    /**
     * 添加对象
     * @see <a href="http://redis.io/commands/pfadd">Redis Documentation: PFADD</a>
     * @since redis 2.8.9
     * @param key 键
     * @param values 对象
     * @return 返回添加对象成功数量
     */
    public Long addAsObj(String key, Object ...values) {
        return this.hyperLogLogOperations.add(key, values);
    }

    /**
     * 添加字符串
     * @see <a href="http://redis.io/commands/pfadd">Redis Documentation: PFADD</a>
     * @since redis 2.8.9
     * @param key 键
     * @param values 字符串
     * @return 返回添加字符串成功数量
     */
    public Long add(String key, String ...values) {
        return this.stringHyperLogLogOperations.add(key, values);
    }

    /**
     * 获取对象基数估算值
     * @see <a href="http://redis.io/commands/pfcount">Redis Documentation: PFCOUNT</a>
     * @since redis 2.8.9
     * @param keys 键
     * @return 返回对象基数估算值
     */
    public Long sizeAsObj(String ...keys) {
        return this.hyperLogLogOperations.size(keys);
    }

    /**
     * 获取字符串基数估算值
     * @see <a href="http://redis.io/commands/pfcount">Redis Documentation: PFCOUNT</a>
     * @since redis 2.8.9
     * @param keys 键
     * @return 返回字符串基数估算值
     */
    public Long size(String ...keys) {
        return this.stringHyperLogLogOperations.size(keys);
    }

    /**
     * 合并对象
     * @see <a href="http://redis.io/commands/pfmerge">Redis Documentation: PFMERGE</a>
     * @since redis 2.8.9
     * @param key 键
     * @param otherKeys 其他键
     * @return 返回合并后的对象基数估算值
     */
    public Long unionAsObj(String key, String ...otherKeys) {
        return this.hyperLogLogOperations.union(key, otherKeys);
    }

    /**
     * 合并字符串
     * @see <a href="http://redis.io/commands/pfmerge">Redis Documentation: PFMERGE</a>
     * @since redis 2.8.9
     * @param key 键
     * @param otherKeys 字符串
     * @return 返回合并后的字符串基数估算值
     */
    public Long union(String key, String ...otherKeys) {
        return this.stringHyperLogLogOperations.union(key, otherKeys);
    }

    /**
     * 获取spring redis模板
     * @return 返回对象模板
     */
    public RedisTemplate<String, Object> getRedisTemplate() {
        return this.redisTemplate;
    }

    /**
     * 获取spring string redis模板
     * @return 返回字符串模板
     */
    public StringRedisTemplate getStringRedisTemplate() {
        return this.stringRedisTemplate;
    }
}
