package com.xianmao.lock;

import com.xianmao.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName RedisLock
 * @Description: TODO
 * @Author wjh
 * @Data 2020/6/29 2:33 下午
 * @Version 1.0
 */
public class RedisLock {

    private static final Logger logger = LoggerFactory.getLogger(RedisLock.class);

    private final RedisTemplate redisTemplate;

    @Autowired
    private RedisUtil redisUtil;


    public RedisLock(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 阻塞式获取锁 ,不过有超时时间，超过了tryGetLockTime还未获取到锁将直接返回false
     *
     * @param tryGetLockTime
     * @return
     * @throws InterruptedException
     */
    public synchronized boolean lock(String lockKey, long tryGetLockTime, long lockExpires) {
        try {

            long start = System.currentTimeMillis();

            while (System.currentTimeMillis() - start < tryGetLockTime) {
                long lockExpireTime = System.currentTimeMillis() + tryGetLockTime + 1L;//锁超时时间
                String stringOfLockExpireTime = String.valueOf(lockExpireTime);

                if (setNx(lockKey, stringOfLockExpireTime, lockExpires)) { // 获取到锁
                    return true;
                }

                //说明未获取到锁，进一步检查锁是否已经超时
                String lockVal = (String) redisUtil.get(lockKey);
                if (lockVal != null && Long.parseLong(lockVal) < System.currentTimeMillis()) {
                    String oldLockVal = getSet(lockKey, stringOfLockExpireTime);
                    if (lockVal.equals(oldLockVal)) {
                        redisTemplate.expire(lockKey, lockExpires, TimeUnit.MILLISECONDS);
                        return true;
                    }
                }

                Thread.sleep(100L);

            }
        } catch (InterruptedException e) {
            logger.error("阻塞式获取锁 出现异常：", e);
        }
        return false;
    }


    /**
     * 非阻塞，立即返回是否获取到锁
     *
     * @return
     */
    public boolean tryLock(String lockKey, String stringOfLockExpireTime, long lockExpires) {
        if (setNx(lockKey, stringOfLockExpireTime, lockExpires)) { // 获取到锁
            return true;
        }
        return false;
    }


    /**
     * 释放锁
     */
    public void unlock(String lockKey) {
        redisTemplate.delete(lockKey);
    }


    /**
     * 设置锁
     *
     * @param lockKey
     * @param stringOfLockExpireTime
     * @param lockExpires
     * @return
     */
    private boolean setNx(String lockKey, String stringOfLockExpireTime, long lockExpires) {
        boolean flag = (boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                StringRedisSerializer serializer = new StringRedisSerializer();
                Boolean success = connection.setNX(serializer.serialize(lockKey), serializer.serialize(String.valueOf(stringOfLockExpireTime)));
                connection.close();
                return success;
            }
        });
        if (flag) {
            logger.info("开始设置过期时间 lockKey：{} lockExpires:{}", lockKey, lockExpires);
            //设置超时时间，释放内存
            redisTemplate.expire(lockKey, lockExpires, TimeUnit.MILLISECONDS);
        }
        return flag;
    }


    /**
     * lua脚本-获取锁
     *
     * @param lockKey
     * @param lockValue
     * @param expireTime：单位-秒
     * @return
     */
    public boolean getLockForLua(String lockKey, String lockValue, long expireTime) {
        try {
            String script = "if redis.call('setNx',KEYS[1],ARGV[1])==1 then if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('pexpire',KEYS[1],ARGV[2]) else return 0 end else return 0 end";

            RedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);

            Object result = redisTemplate.execute(redisScript, new StringRedisSerializer(), new StringRedisSerializer(), Collections.singletonList(lockKey), lockValue, expireTime + "");
            if (null != result && result.equals(1L)) {
                return true;
            }

        } catch (Exception e) {
            logger.error("加锁Exception", e);
        }
        return false;
    }


    /**
     * lua脚本-释放锁
     *
     * @param lockKey
     * @param lockValue
     * @return
     */
    public boolean releaseLockForLua(String lockKey, String lockValue) {

        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        RedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);

        Object result = redisTemplate.execute(redisScript, new StringRedisSerializer(), new StringRedisSerializer(), Collections.singletonList(lockKey), lockValue);
        if (null != result && result.equals(1L)) {
            return true;
        }

        return false;
    }

    public String getSet(final String key, final String value) {
        String result = null;
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        result = operations.getAndSet(key, value);
        return result;
    }
}
