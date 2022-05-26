package com.xianmao.common.core.snowflake;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName SnowflakeIdeable
 * @Description: id生成工具类.
 * <p>名称：IdWorker.java</p>
 * <p>描述：分布式自增长ID</p>
 * <pre>
 *     Twitter的 Snowflake　JAVA实现方案
 * </pre>
 * 核心代码为其IdWorker这个类实现，其原理结构如下，我分别用一个0表示一位，用—分割开部分的作用：
 * 1||0---0000000000 0000000000 0000000000 0000000000 0 --- 00000 ---00000 ---000000000000
 * 在上面的字符串中，第一位为未使用（实际上也可作为long的符号位），接下来的41位为毫秒级时间，
 * 然后5位datacenter标识位，5位机器ID（并不算标识符，实际是为线程标识），
 * 然后12位该毫秒内的当前毫秒内的计数， ，为一个Long型。
 * 这样的好处是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞（由datacenter和机器ID作区分），
 * 并且效率较高，经测试，snowflake每秒能够产生26万ID左右，完全满足需要。
 * <p>
 * 64位ID (42(毫秒)+5(机器ID)+5(业务编码)+12(重复累加))
 * @Author wjh
 * @Data 2020-06-04 12:34
 * @Version 1.0
 */
public class SnowflakeIdeable implements Ideable<Long> {

    private final static long TWEPOCH = 1288834974657L;

    // 机器标识位数
    private final static long WORKER_ID_BITS = 5L;

    // 数据中心标识位数
    private final static long DATA_CENTER_ID_BITS = 5L;

    // 机器ID最大值 31
    private final static long MAX_WORKER_ID = -1L ^ (-1L << WORKER_ID_BITS);

    // 数据中心ID最大值 31
    private final static long MAX_DATA_CENTER_ID = -1L ^ (-1L << DATA_CENTER_ID_BITS);

    // 毫秒内自增位
    private final static long SEQUENCE_BITS = 12L;

    // 机器ID偏左移12位
    private final static long WORKER_ID_SHIFT = SEQUENCE_BITS;

    private final static long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    // 时间毫秒左移22位
    private final static long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;

    /**
     * 每台workerId服务器有3个备份workerId, 备份workerId数量越多, 可靠性越高, 但是可部署的sequence ID服务越少
     */
    private static final long BACKUP_COUNT = 3;

    /**
     * 保留workerId和lastTimestamp, 以及备用workerId和其对应的lastTimestamp
     */
    private static Map<Long, Long> workerIdLastTimeMap = new ConcurrentHashMap<>();

    /**
     * 最大容忍时间, 单位毫秒, 即如果时钟只是回拨了该变量指定的时间, 那么等待相应的时间即可;
     * 考虑到sequence服务的高性能, 这个值不易过大
     */
    private static final long MAX_BACKWARD_MS = 3;

    //原来代码 -1 的补码（二进制全1）右移13位, 然后取反
    private static final long maxWorkerId = -1L ^ (-1L << WORKER_ID_BITS);


    private final static long SEQUENCE_MASK = -1L ^ (-1L << SEQUENCE_BITS);

    private long lastTimestamp = -1L;

    private long sequence = 0L;
    private long workerId;
    private long dataCenterId;
    private static volatile SnowflakeIdeable snowflake = null;
    private static Object lock = new Object();

    //单例禁止new实例化
    private SnowflakeIdeable(long workerId, long dataCenterId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            workerId = getRandom();
        }

        if (dataCenterId > MAX_DATA_CENTER_ID || dataCenterId < 0) {

            throw new IllegalArgumentException(String.format("%s 数据中心ID最大值 必须是 %d 到 %d 之间", dataCenterId, 0, MAX_DATA_CENTER_ID));
        }

        //存取三个workerId
        for (int i = 0; i <= BACKUP_COUNT; i++) {
            workerIdLastTimeMap.put(this.workerId + (i * maxWorkerId), 0L);
        }

        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }

    /**
     * 获取单列
     *
     * @return
     */
    public static SnowflakeIdeable getInstanceSnowflake() {
        if (snowflake == null) {
            synchronized (lock) {
                long workerId;
                long dataCenterId = getRandom();
                try {
                    //第一次使用获取mac地址的
                    workerId = getWorkerId();
                } catch (Exception e) {
                    workerId = getRandom();
                }
                snowflake = new SnowflakeIdeable(workerId, dataCenterId);
            }
        }
        return snowflake;
    }

    /**
     * 生成1-31之间的随机数
     *
     * @return
     */
    private static long getRandom() {
        int max = (int) (MAX_WORKER_ID);
        int min = 1;
        Random random = new Random();
        long result = random.nextInt(max - min) + min;
        return result;
    }

    public static Long getSnowflakeId() {
        return SnowflakeIdeable.getInstanceSnowflake().nextId();
    }

    public static String getDateSnowflakeId() throws Exception {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + getSnowflakeId();
    }

    private synchronized long nextId() {
        if (lastTimestamp == -1l) {
            lastTimestamp = time();
        } else {
            // 当前毫秒内，则+1
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                // 当前毫秒内计数满了，则等待下一秒
                lastTimestamp = tilNextMillis(lastTimestamp);
            }
        }
        // ID偏移组合生成最终的ID，并返回ID
        long nextId = ((lastTimestamp - TWEPOCH) << TIMESTAMP_LEFT_SHIFT)
                | (dataCenterId << DATA_CENTER_ID_SHIFT) | (workerId << WORKER_ID_SHIFT) | sequence;

        return nextId;
    }

    private long tilNextMillis(final long lastTimestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(lastTimestamp);
        cal.add(Calendar.MILLISECOND, 1);
        return cal.getTimeInMillis();
    }

    private static long time() {
        return System.currentTimeMillis();
    }

    @SuppressWarnings("Duplicates")
    private static long getWorkerId() throws SocketException, UnknownHostException, NullPointerException {
        @SuppressWarnings("unused")
        InetAddress ip = InetAddress.getLocalHost();

        NetworkInterface network = null;
        Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
        while (en.hasMoreElements()) {
            NetworkInterface nint = en.nextElement();
            if (!nint.isLoopback() && nint.getHardwareAddress() != null) {
                network = nint;
                break;
            }
        }

        @SuppressWarnings("ConstantConditions")
        byte[] mac = network.getHardwareAddress();

        Random rnd = new Random();
        byte rndByte = (byte) (rnd.nextInt() & 0x000000FF);

        // 取mac地址最后一位和随机数
        return ((0x000000FF & (long) mac[mac.length - 1]) | (0x0000FF00 & (((long) rndByte) << 8))) >> 6;
    }

    /**
     * 尝试在workerId的备份workerId上生成
     * 核心优化代码在方法tryGenerateKeyOnBackup()中，BACKUP_COUNT即备份workerId数越多，
     * sequence服务避免时钟回拨影响的能力越强，但是可部署的sequence服务越少，
     * 设置BACKUP_COUNT为3，最多可以部署1024/(3+1)即256个sequence服务，完全够用，
     * 抗时钟回拨影响的能力也得到非常大的保障。
     *
     * @param currentMillis 当前时间
     */
    private long tryGenerateKeyOnBackup(long currentMillis) {
        // 遍历所有workerId(包括备用workerId, 查看哪些workerId可用)
        for (Map.Entry<Long, Long> entry : workerIdLastTimeMap.entrySet()) {
            this.workerId = entry.getKey();
            // 取得备用workerId的lastTime
            Long tempLastTime = entry.getValue();
            lastTimestamp = tempLastTime == null ? 0L : tempLastTime;

            // 如果找到了合适的workerId
            if (lastTimestamp <= currentMillis) {
                return lastTimestamp;
            }
        }

        // 如果所有workerId以及备用workerId都处于时钟回拨, 那么抛出异常
        throw new IllegalStateException("Clock is moving backwards, current time is "
                + currentMillis + " milliseconds, workerId map = " + workerIdLastTimeMap);
    }

    /**
     * 生成Id随机数
     *
     * @return id随机数
     */
    @Override
    public Long generateId() {
        return getSnowflakeId();
    }
}
