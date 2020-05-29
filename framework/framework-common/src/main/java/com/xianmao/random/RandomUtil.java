package com.xianmao.random;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.util.Assert;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @ClassName RandomUtil
 * @Description: 随机数工具类
 * @Author guyi
 * @Data 2019-08-14 21:51
 * @Version 1.0
 */
public class RandomUtil {

    private static final String S_INT = "0123456789";
    private static final String S_STR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String S_ALL = S_INT + S_STR;

    /**
     * 为了确保同一毫秒不能返回相同的值,不同声明在方法里面.<br>
     * 把Random对象作为一个全局实例(static)来使用. Java中Random是线程安全的(内部进行了加锁处理);
     */
    private static final Random JVM_RANDOM = new Random();

    private RandomUtil() {
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * 随机数生成
     *
     * @param count 字符长度
     * @return 随机数
     */
    public static String random(int count) {
        return RandomUtil.random(count, RandomType.ALL);
    }

    /**
     * 随机数生成
     *
     * @param count      字符长度
     * @param randomType 随机数类别
     * @return 随机数
     */
    public static String random(int count, RandomType randomType) {
        if (count == 0) {
            return "";
        }
        Assert.isTrue(count > 0, "Requested random string length " + count + " is less than 0.");
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        char[] buffer = new char[count];
        for (int i = 0; i < count; i++) {
            if (RandomType.INT == randomType) {
                buffer[i] = S_INT.charAt(random.nextInt(S_INT.length()));
            } else if (RandomType.STRING == randomType) {
                buffer[i] = S_STR.charAt(random.nextInt(S_STR.length()));
            } else {
                buffer[i] = S_ALL.charAt(random.nextInt(S_ALL.length()));
            }
        }
        return new String(buffer);
    }

    /**
     * 生成一个指定长度<code>length</code>的 <b>随机正整数</b>.
     *
     * <h3>示例:</h3>
     * <blockquote>
     *
     * <pre class="code">
     * RandomUtil.createRandomWithLength(2)
     * 生成的结果是可能是 89
     * </pre>
     *
     * </blockquote>
     *
     * @param length 设定所取出随机数的长度.
     * @return 如果 <code>length</code> {@code <=0} ,抛出 {@link IllegalArgumentException}
     */
    public static long createRandomWithLength(int length) {
        Validate.isTrue(length > 0, "input param [length] must >0,but is [%s]", length);
        long num = 1;
        for (int i = 0; i < length; ++i) {
            num = num * 10;
        }

        double random = JVM_RANDOM.nextDouble();
        random = random < 0.1 ? random + 0.1 : random;// 可能出现 0.09346924349151808
        return (long) (random * num);
    }

    /**
     * 随机抽取字符串<code>char</code>,拼接成指定长度<code>length</code>的字符串.
     *
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>常用于生成验证码</li>
     * </ol>
     * </blockquote>
     *
     * <h3>示例:</h3>
     * <blockquote>
     *
     * </blockquote>
     *
     * @param str    被抽取的字符串
     * @param length 指定字符串长度,比如 5
     * @return 如果 <code>str</code> 是null,抛出 {@link NullPointerException}<br>
     * 如果 <code>str</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * 如果 <code>length</code> {@code <=0}, 抛出 {@link IllegalArgumentException}
     * @see org.apache.commons.lang3.RandomStringUtils#random(int, String)
     */
    public static String createRandomFromString(String str, int length) {
        Validate.notBlank(str, "str can't be null/empty!");
        Validate.isTrue(length > 0, "input param [length] must >0,but is [%s]", length);
        return RandomStringUtils.random(length, str);
    }
}
