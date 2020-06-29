package com.xianmao.lock;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName RedisLock
 * @Description: TODO
 * @Author wjh
 * @Data 2020/6/29 2:33 下午
 * @Version 1.0
 */
public class RedisLock {

    /**
     * 请求锁的超时时间(ms)
     */
    private static final long timeOut = 5000;
    /**
     * 存储到redis中的锁标志
     */
    private static final String LOCKED = "LOCKED";
    /**
     * 默认锁的有效时间(s)
     */
    private static final int expireTime = 60;
    /**
     * 锁的有效时间
     */
    private long expires = 0;
    /**
     * 锁标记
     */
    private volatile boolean locked = false;

    /**
     * 锁标志对应的key
     */
    private String lockKey;


    private RedisTemplate<String, Object> redisTemplate;

    public RedisLock(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    /**
     * 获得 lock.
     * 实现思路: 主要是使用了redis 的setnx命令,缓存了锁.
     * reids缓存的key是锁的key,所有的共享, value是锁的到期时间(注意:这里把过期时间放在value了,没有时间上设置其超时时间)
     * 执行过程:
     * 1.通过setnx尝试设置某个key的值,成功(当前没有这个锁)则返回,成功获得锁
     * 2.锁已经存在则获取锁的到期时间,和当前时间比较,超时的话,则设置新的值
     *
     * @return true if lock is acquired, false acquire timeouted
     * @throws InterruptedException in case of thread interruption
     */
    public boolean lock() {
        // 请求锁超时时间，纳秒
        long timeout = timeOut * 1000000;
        // 系统当前时间，纳秒
        long nowTime = System.nanoTime();

        while ((System.nanoTime() - nowTime) < timeout) {
            // 分布式服务器有时差，这里给1秒的误差值
            expires = System.currentTimeMillis() + expireTime + 1;
            String expiresStr = String.valueOf(expires); //锁到期时间

            if (redisTemplate.opsForValue().setIfAbsent(lockKey, expiresStr)) {
                locked = true;
                // 设置锁的有效期，也是锁的自动释放时间，也是一个客户端在其他客户端能抢占锁之前可以执行任务的时间
                // 可以防止因异常情况无法释放锁而造成死锁情况的发生
                redisTemplate.expire(lockKey, expireTime, TimeUnit.SECONDS);
                // 上锁成功结束请求
                return true;
            }

            String currentValueStr = (String) redisTemplate.opsForValue().get(lockKey); //redis里的时间
            if (currentValueStr != null && Long.parseLong(currentValueStr) < System.currentTimeMillis()) {
                //判断是否为空，不为空的情况下，如果被其他线程设置了值，则第二个条件判断是过不去的
                String oldValueStr = (String) redisTemplate.opsForValue().getAndSet(lockKey, expiresStr);
                //获取上一个锁到期时间，并设置现在的锁到期时间，
                //只有一个线程才能获取上一个线上的设置时间，因为jedis.getSet是同步的
                if (oldValueStr != null && oldValueStr.equals(currentValueStr)) {
                    //防止误删（覆盖，因为key是相同的）了他人的锁——这里达不到效果，这里值会被覆盖，但是因为什么相差了很少的时间，所以可以接受
                    //[分布式的情况下]:如过这个时候，多个线程恰好都到了这里，但是只有一个线程的设置值和当前值相同，他才有权利获取锁
                    // lock acquired
                    locked = true;
                    return true;
                }
            }
            /**
             * 延迟10 毫秒,  这里使用随机时间可能会好一点,可以防止饥饿进程的出现,即,当同时到达多个进程,
             * 只会有一个进程获得锁,其他的都用同样的频率进行尝试,后面有来了一些进行,也以同样的频率申请锁,这将可能导致前面来的锁得不到满足.
             * 使用随机的等待时间可以一定程度上保证公平性
             */
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        return locked;
    }

    /**
     * 解锁
     * 只有加锁成功并且锁还有效才去释放锁
     */
    public synchronized void unlock() {
        if (locked && expires > System.currentTimeMillis()) {
            redisTemplate.delete(lockKey);
            locked = false;
        }
    }
}
