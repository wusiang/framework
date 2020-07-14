package com.xianmao.date;


import com.xianmao.string.StringPool;
import com.xianmao.utils.StringUtil;
import org.springframework.util.Assert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalQuery;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @ClassName DateUtil
 * @Description: TODO
 * @Author wjh
 * @Data 2020-04-28 13:11
 * @Version 1.0
 */
public class DateTimeUtil {

    /**
     * 获取当前的Data
     *
     * @return 返回当前的Data对象
     */
    public static Date currentDate() {
        return Date.from(Instant.now());
    }

    /**
     * 当前时间，格式 yyyy-MM-dd HH:mm:ss
     *
     * @return 当前时间的标准形式字符串
     */
    public static String now() {
        return formatDateTime(new Date());
    }

    /**
     * 当前日期，格式 yyyy-MM-dd
     *
     * @return 当前日期的标准形式字符串
     */
    public static String today() {
        return formatDate(new Date());
    }

    // ------------------------------------ Format start ----------------------------------------------

    /**
     * 格式化日期时间<br>
     * 格式 yyyy-MM-dd HH:mm:ss
     *
     * @param localDateTime 被格式化的日期
     * @return 格式化后的字符串
     */
    public static String formatLocalDateTime(LocalDateTime localDateTime) {
        return format(localDateTime, cn.hutool.core.date.DatePattern.NORM_DATETIME_PATTERN);
    }

    /**
     * 根据特定格式格式化日期
     *
     * @param localDateTime 被格式化的日期
     * @param format        日期格式
     * @return 格式化后的字符串
     */
    public static String format(LocalDateTime localDateTime, String format) {
        if (null == localDateTime || StringUtil.isBlank(format)) {
            return null;
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        return localDateTime.format(df);
    }

    /**
     * 根据特定格式格式化日期
     *
     * @param date   被格式化的日期
     * @param format 日期格式
     * @return 格式化后的字符串
     */
    public static String format(Date date, String format) {
        if (null == date || StringUtil.isBlank(format)) {
            return null;
        }
        final SimpleDateFormat sdf = new SimpleDateFormat(format);
        return format(date, sdf);
    }

    /**
     * 根据特定格式格式化日期
     *
     * @param date   被格式化的日期
     * @param format {@link SimpleDateFormat}
     * @return 格式化后的字符串
     */
    public static String format(Date date, DateFormat format) {
        if (null == format || null == date) {
            return null;
        }
        return format.format(date);
    }

    /**
     * 根据特定格式格式化日期
     *
     * @param date   被格式化的日期
     * @param format {@link SimpleDateFormat}
     * @return 格式化后的字符串
     * @since 5.0.0
     */
    public static String format(Date date, DateTimeFormatter format) {
        if (null == format || null == date) {
            return null;
        }
        return format.format(date.toInstant());
    }

    /**
     * 格式化日期时间<br>
     * 格式 yyyy-MM-dd HH:mm:ss
     *
     * @param date 被格式化的日期
     * @return 格式化后的日期
     */
    public static String formatDateTime(Date date) {
        if (null == date) {
            return null;
        }
        return DatePattern.NORM_DATETIME_FORMAT.format(date);
    }

    /**
     * 格式化日期部分（不包括时间）<br>
     * 格式 yyyy-MM-dd
     *
     * @param date 被格式化的日期
     * @return 格式化后的字符串
     */
    public static String formatDate(Date date) {
        if (null == date) {
            return null;
        }
        return DatePattern.NORM_DATE_FORMAT.format(date);
    }

    /**
     * 格式化时间<br>
     * 格式 HH:mm:ss
     *
     * @param date 被格式化的日期
     * @return 格式化后的字符串
     * @since 3.0.1
     */
    public static String formatTime(Date date) {
        if (null == date) {
            return null;
        }
        return DatePattern.PATTERN_TIME_FORMAT.format(date);
    }
    //

    /**
     * 获取某日期当月的天数
     *
     * @param date 某日期
     * @return 天数
     */
    public static int getMonthDayNumByLocalDate(LocalDate date) {
        return date.getMonth().length(date.isLeapYear());
    }

    /**
     * 添加年
     *
     * @param date       时间
     * @param yearsToAdd 添加的年数
     * @return 设置后的时间
     */
    public static Date plusYears(Date date, int yearsToAdd) {
        return DateTimeUtil.set(date, Calendar.YEAR, yearsToAdd);
    }

    /**
     * 添加月
     *
     * @param date        时间
     * @param monthsToAdd 添加的月数
     * @return 设置后的时间
     */
    public static Date plusMonths(Date date, int monthsToAdd) {
        return DateTimeUtil.set(date, Calendar.MONTH, monthsToAdd);
    }

    /**
     * 添加周
     *
     * @param date       时间
     * @param weeksToAdd 添加的周数
     * @return 设置后的时间
     */
    public static Date plusWeeks(Date date, int weeksToAdd) {
        return DateTimeUtil.plus(date, Period.ofWeeks(weeksToAdd));
    }

    /**
     * 添加天
     *
     * @param date      时间
     * @param daysToAdd 添加的天数
     * @return 设置后的时间
     */
    public static Date plusDays(Date date, long daysToAdd) {
        return DateTimeUtil.plus(date, Duration.ofDays(daysToAdd));
    }

    /**
     * 添加小时
     *
     * @param date       时间
     * @param hoursToAdd 添加的小时数
     * @return 设置后的时间
     */
    public static Date plusHours(Date date, long hoursToAdd) {
        return DateTimeUtil.plus(date, Duration.ofHours(hoursToAdd));
    }

    /**
     * 添加分钟
     *
     * @param date         时间
     * @param minutesToAdd 添加的分钟数
     * @return 设置后的时间
     */
    public static Date plusMinutes(Date date, long minutesToAdd) {
        return DateTimeUtil.plus(date, Duration.ofMinutes(minutesToAdd));
    }

    /**
     * 添加秒
     *
     * @param date         时间
     * @param secondsToAdd 添加的秒数
     * @return 设置后的时间
     */
    public static Date plusSeconds(Date date, long secondsToAdd) {
        return DateTimeUtil.plus(date, Duration.ofSeconds(secondsToAdd));
    }

    /**
     * 添加毫秒
     *
     * @param date        时间
     * @param millisToAdd 添加的毫秒数
     * @return 设置后的时间
     */
    public static Date plusMillis(Date date, long millisToAdd) {
        return DateTimeUtil.plus(date, Duration.ofMillis(millisToAdd));
    }

    /**
     * 添加纳秒
     *
     * @param date       时间
     * @param nanosToAdd 添加的纳秒数
     * @return 设置后的时间
     */
    public static Date plusNanos(Date date, long nanosToAdd) {
        return DateTimeUtil.plus(date, Duration.ofNanos(nanosToAdd));
    }

    /**
     * 日期添加时间量
     *
     * @param date   时间
     * @param amount 时间量
     * @return 设置后的时间
     */
    public static Date plus(Date date, TemporalAmount amount) {
        Instant instant = date.toInstant();
        return Date.from(instant.plus(amount));
    }

    /**
     * 减少年
     *
     * @param date  时间
     * @param years 减少的年数
     * @return 设置后的时间
     */
    public static Date minusYears(Date date, int years) {
        return DateTimeUtil.set(date, Calendar.YEAR, -years);
    }

    /**
     * 减少月
     *
     * @param date   时间
     * @param months 减少的月数
     * @return 设置后的时间
     */
    public static Date minusMonths(Date date, int months) {
        return DateTimeUtil.set(date, Calendar.MONTH, -months);
    }

    /**
     * 减少周
     *
     * @param date  时间
     * @param weeks 减少的周数
     * @return 设置后的时间
     */
    public static Date minusWeeks(Date date, int weeks) {
        return DateTimeUtil.minus(date, Period.ofWeeks(weeks));
    }

    /**
     * 减少天
     *
     * @param date 时间
     * @param days 减少的天数
     * @return 设置后的时间
     */
    public static Date minusDays(Date date, long days) {
        return DateTimeUtil.minus(date, Duration.ofDays(days));
    }

    /**
     * 减少小时
     *
     * @param date  时间
     * @param hours 减少的小时数
     * @return 设置后的时间
     */
    public static Date minusHours(Date date, long hours) {
        return DateTimeUtil.minus(date, Duration.ofHours(hours));
    }

    /**
     * 减少分钟
     *
     * @param date    时间
     * @param minutes 减少的分钟数
     * @return 设置后的时间
     */
    public static Date minusMinutes(Date date, long minutes) {
        return DateTimeUtil.minus(date, Duration.ofMinutes(minutes));
    }

    /**
     * 减少秒
     *
     * @param date    时间
     * @param seconds 减少的秒数
     * @return 设置后的时间
     */
    public static Date minusSeconds(Date date, long seconds) {
        return DateTimeUtil.minus(date, Duration.ofSeconds(seconds));
    }

    /**
     * 减少毫秒
     *
     * @param date   时间
     * @param millis 减少的毫秒数
     * @return 设置后的时间
     */
    public static Date minusMillis(Date date, long millis) {
        return DateTimeUtil.minus(date, Duration.ofMillis(millis));
    }

    /**
     * 减少纳秒
     *
     * @param date  时间
     * @param nanos 减少的纳秒数
     * @return 设置后的时间
     */
    public static Date minusNanos(Date date, long nanos) {
        return DateTimeUtil.minus(date, Duration.ofNanos(nanos));
    }

    /**
     * 日期减少时间量
     *
     * @param date   时间
     * @param amount 时间量
     * @return 设置后的时间
     */
    public static Date minus(Date date, TemporalAmount amount) {
        Instant instant = date.toInstant();
        return Date.from(instant.minus(amount));
    }

    /**
     * 设置日期属性
     *
     * @param date          时间
     * @param calendarField 更改的属性
     * @param amount        更改数，-1表示减少
     * @return 设置后的时间
     */
    private static Date set(Date date, int calendarField, int amount) {
        Assert.notNull(date, "The date must not be null");
        Calendar c = Calendar.getInstance();
        c.setLenient(false);
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }

    /**
     * 将字符串转换为时间
     *
     * @param dateStr 时间字符串
     * @param pattern 表达式
     * @return 时间
     */
    public static <T> T parse(String dateStr, String pattern, TemporalQuery<T> query) {
        return DateTimeFormatter.ofPattern(pattern).parse(dateStr, query);
    }

    /**
     * 时间转 Instant
     *
     * @param dateTime 时间
     * @return Instant
     */
    public static Instant toInstant(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    /**
     * Instant 转 时间
     *
     * @param instant Instant
     * @return Instant
     */
    public static LocalDateTime toDateTime(Instant instant) {
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    /**
     * 转换成 date
     *
     * @param dateTime LocalDateTime
     * @return Date
     */
    public static Date toDate(LocalDateTime dateTime) {
        return Date.from(DateTimeUtil.toInstant(dateTime));
    }

    /**
     * 转换成 date
     *
     * @param localDate LocalDate
     * @return Date
     */
    public static Date toDate(final LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Converts local date time to Calendar.
     */
    public static Calendar toCalendar(final LocalDateTime localDateTime) {
        return GregorianCalendar.from(ZonedDateTime.of(localDateTime, ZoneId.systemDefault()));
    }

    /**
     * localDateTime 转换成毫秒数
     *
     * @param localDateTime LocalDateTime
     * @return long
     */
    public static long toMilliseconds(final LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * localDate 转换成毫秒数
     *
     * @param localDate LocalDate
     * @return long
     */
    public static long toMilliseconds(LocalDate localDate) {
        return toMilliseconds(localDate.atStartOfDay());
    }


    /**
     * 当前时间的时间戳
     *
     * @param isNano 是否为高精度时间
     * @return 时间
     */
    public static long toMillis(boolean isNano) {
        return isNano ? System.nanoTime() : System.currentTimeMillis();
    }

    /**
     * 当前时间的时间戳（秒）
     *
     * @return 当前时间秒数
     * @since 4.0.0
     */
    public static long toMilliseconds() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 比较2个时间差，跨度比较大，年月日为单位
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param isAbs     日期间隔是否只保留绝对值正数
     * @return 时间间隔
     */
    public static Period between(LocalDate startDate, LocalDate endDate, boolean isAbs) {
        if (isAbs && startDate.isAfter(endDate)) {
            // 间隔只为正数的情况下，如果开始日期晚于结束日期，置换之
            return Period.between(endDate, startDate);
        } else {
            return Period.between(startDate, endDate);
        }
    }

    /**
     * 比较2个 时间差
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param isAbs     日期间隔是否只保留绝对值正数
     * @return 时间间隔
     */
    public static Duration between(Date startDate, Date endDate, boolean isAbs) {
        if (isAbs && startDate.after(endDate)) {
            // 间隔只为正数的情况下，如果开始日期晚于结束日期，置换之
            return Duration.between(endDate.toInstant(), startDate.toInstant());
        } else {
            return Duration.between(startDate.toInstant(), endDate.toInstant());
        }
    }


    /**
     * 将秒数转换为日时分秒
     *
     * @param second 秒数
     * @return 时间
     */
    public static String secondToTime(Long second) {
        // 判断是否为空
        if (second == null || second == 0L) {
            return StringPool.EMPTY;
        }
        //转换天数
        long days = second / 86400;
        //剩余秒数
        second = second % 86400;
        //转换小时
        long hours = second / 3600;
        //剩余秒数
        second = second % 3600;
        //转换分钟
        long minutes = second / 60;
        //剩余秒数
        second = second % 60;
        if (days > 0) {
            return StringUtil.format("{}天{}小时{}分{}秒", days, hours, minutes, second);
        } else {
            return StringUtil.format("{}小时{}分{}秒", hours, minutes, second);
        }
    }

    /**
     * 获取过去的天数
     *
     * @param date
     * @return
     */
    public static long pastDays(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t / (24 * 60 * 60 * 1000);
    }

    /**
     * 获取过去的小时
     *
     * @param date
     * @return
     */
    public static long pastHour(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t / (60 * 60 * 1000);
    }

    /**
     * 获取过去的分钟
     *
     * @param date
     * @return
     */
    public static long pastMinutes(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t / (60 * 1000);
    }
}
