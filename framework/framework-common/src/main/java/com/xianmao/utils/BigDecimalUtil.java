package com.xianmao.utils;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @ClassName BigDecimalUtil
 * @Description: TODO
 * @Author wjh
 * @Data 2020/7/17 11:05 上午
 * @Version 1.0
 */
public class BigDecimalUtil {

    /**
     * 保留固定位数小数<br>
     * 采用四舍五入策略 {@link RoundingMode#HALF_UP}<br>
     * 例如保留2位小数：123.456789 =》 123.46
     *
     * @param v     值
     * @param scale 保留小数位数
     * @return 新值
     */
    public static BigDecimal round(double v, int scale) {
        return round(v, scale, RoundingMode.HALF_UP);
    }

    /**
     * 保留固定位数小数<br>
     * 采用四舍五入策略 {@link RoundingMode#HALF_UP}<br>
     * 例如保留2位小数：123.456789 =》 123.46
     *
     * @param v     值
     * @param scale 保留小数位数
     * @return 新值
     */
    public static String roundStr(double v, int scale) {
        return round(v, scale).toString();
    }

    /**
     * 保留固定位数小数<br>
     * 采用四舍五入策略 {@link RoundingMode#HALF_UP}<br>
     * 例如保留2位小数：123.456789 =》 123.46
     *
     * @param numberStr 数字值的字符串表现形式
     * @param scale     保留小数位数
     * @return 新值
     */
    public static BigDecimal round(String numberStr, int scale) {
        return round(numberStr, scale, RoundingMode.HALF_UP);
    }

    /**
     * 保留固定位数小数<br>
     * 采用四舍五入策略 {@link RoundingMode#HALF_UP}<br>
     * 例如保留2位小数：123.456789 =》 123.46
     *
     * @param number 数字值
     * @param scale  保留小数位数
     * @return 新值
     * @since 4.1.0
     */
    public static BigDecimal round(BigDecimal number, int scale) {
        return round(number, scale, RoundingMode.HALF_UP);
    }

    /**
     * 保留固定位数小数<br>
     * 采用四舍五入策略 {@link RoundingMode#HALF_UP}<br>
     * 例如保留2位小数：123.456789 =》 123.46
     *
     * @param numberStr 数字值的字符串表现形式
     * @param scale     保留小数位数
     * @return 新值
     * @since 3.2.2
     */
    public static String roundStr(String numberStr, int scale) {
        return round(numberStr, scale).toString();
    }

    /**
     * 保留固定位数小数<br>
     * 例如保留四位小数：123.456789 =》 123.4567
     *
     * @param v            值
     * @param scale        保留小数位数
     * @param roundingMode 保留小数的模式 {@link RoundingMode}
     * @return 新值
     */
    public static BigDecimal round(double v, int scale, RoundingMode roundingMode) {
        return round(Double.toString(v), scale, roundingMode);
    }

    /**
     * 保留固定位数小数<br>
     * 例如保留四位小数：123.456789 =》 123.4567
     *
     * @param v            值
     * @param scale        保留小数位数
     * @param roundingMode 保留小数的模式 {@link RoundingMode}
     * @return 新值
     * @since 3.2.2
     */
    public static String roundStr(double v, int scale, RoundingMode roundingMode) {
        return round(v, scale, roundingMode).toString();
    }

    /**
     * 保留固定位数小数<br>
     * 例如保留四位小数：123.456789 =》 123.4567
     *
     * @param numberStr    数字值的字符串表现形式
     * @param scale        保留小数位数，如果传入小于0，则默认0
     * @param roundingMode 保留小数的模式 {@link RoundingMode}，如果传入null则默认四舍五入
     * @return 新值
     */
    public static BigDecimal round(String numberStr, int scale, RoundingMode roundingMode) {
        if (scale < 0) {
            scale = 0;
        }
        return round(toBigDecimal(numberStr), scale, roundingMode);
    }

    /**
     * 保留固定位数小数<br>
     * 例如保留四位小数：123.456789 =》 123.4567
     *
     * @param number       数字值
     * @param scale        保留小数位数，如果传入小于0，则默认0
     * @param roundingMode 保留小数的模式 {@link RoundingMode}，如果传入null则默认四舍五入
     * @return 新值
     */
    public static BigDecimal round(BigDecimal number, int scale, RoundingMode roundingMode) {
        if (null == number) {
            number = BigDecimal.ZERO;
        }
        if (scale < 0) {
            scale = 0;
        }
        if (null == roundingMode) {
            roundingMode = RoundingMode.HALF_UP;
        }

        return number.setScale(scale, roundingMode);
    }

    /**
     * 保留固定位数小数<br>
     * 例如保留四位小数：123.456789 =》 123.4567
     *
     * @param numberStr    数字值的字符串表现形式
     * @param scale        保留小数位数
     * @param roundingMode 保留小数的模式 {@link RoundingMode}
     * @return 新值
     * @since 3.2.2
     */
    public static String roundStr(String numberStr, int scale, RoundingMode roundingMode) {
        return round(numberStr, scale, roundingMode).toString();
    }

    /**
     * 四舍六入五成双计算法
     * <p>
     * 四舍六入五成双是一种比较精确比较科学的计数保留法，是一种数字修约规则。
     * </p>
     *
     * <pre>
     * 算法规则:
     * 四舍六入五考虑，
     * 五后非零就进一，
     * 五后皆零看奇偶，
     * 五前为偶应舍去，
     * 五前为奇要进一。
     * </pre>
     *
     * @param number 需要科学计算的数据
     * @param scale  保留的小数位
     * @return 结果
     * @since 4.1.0
     */
    public static BigDecimal roundHalfEven(Number number, int scale) {
        return roundHalfEven(toBigDecimal(number), scale);
    }

    /**
     * 四舍六入五成双计算法
     * <p>
     * 四舍六入五成双是一种比较精确比较科学的计数保留法，是一种数字修约规则。
     * </p>
     *
     * <pre>
     * 算法规则:
     * 四舍六入五考虑，
     * 五后非零就进一，
     * 五后皆零看奇偶，
     * 五前为偶应舍去，
     * 五前为奇要进一。
     * </pre>
     *
     * @param value 需要科学计算的数据
     * @param scale 保留的小数位
     * @return 结果
     * @since 4.1.0
     */
    public static BigDecimal roundHalfEven(BigDecimal value, int scale) {
        return round(value, scale, RoundingMode.HALF_EVEN);
    }

    /**
     * 保留固定小数位数，舍去多余位数
     *
     * @param number 需要科学计算的数据
     * @param scale  保留的小数位
     * @return 结果
     * @since 4.1.0
     */
    public static BigDecimal roundDown(Number number, int scale) {
        return roundDown(toBigDecimal(number), scale);
    }

    /**
     * 保留固定小数位数，舍去多余位数
     *
     * @param value 需要科学计算的数据
     * @param scale 保留的小数位
     * @return 结果
     * @since 4.1.0
     */
    public static BigDecimal roundDown(BigDecimal value, int scale) {
        return round(value, scale, RoundingMode.DOWN);
    }

    /**
     * 数字转{@link BigDecimal}
     *
     * @param number 数字
     * @return {@link BigDecimal}
     * @since 4.0.9
     */
    public static BigDecimal toBigDecimal(Number number) {
        if (null == number) {
            return BigDecimal.ZERO;
        }
        return toBigDecimal(number.toString());
    }

    /**
     * 数字转{@link BigDecimal}
     *
     * @param number 数字
     * @return {@link BigDecimal}
     * @since 4.0.9
     */
    public static BigDecimal toBigDecimal(String number) {
        return (null == number) ? BigDecimal.ZERO : new BigDecimal(number);
    }

    /**
     * 格式化double<br>
     * 对 {@link DecimalFormat} 做封装<br>
     *
     * @param pattern 格式 格式中主要以 # 和 0 两种占位符号来指定数字长度。0 表示如果位数不足则以 0 填充，# 表示只要有可能就把数字拉上这个位置。<br>
     *                <ul>
     *                <li>0 =》 取一位整数</li>
     *                <li>0.00 =》 取一位整数和两位小数</li>
     *                <li>00.000 =》 取两位整数和三位小数</li>
     *                <li># =》 取所有整数部分</li>
     *                <li>#.##% =》 以百分比方式计数，并取两位小数</li>
     *                <li>#.#####E0 =》 显示为科学计数法，并取五位小数</li>
     *                <li>,### =》 每三位以逗号进行分隔，例如：299,792,458</li>
     *                <li>光速大小为每秒,###米 =》 将格式嵌入文本</li>
     *                </ul>
     * @param value   值
     * @return 格式化后的值
     */
    public static String decimalFormat(String pattern, double value) {
        return new DecimalFormat(pattern).format(value);
    }

    /**
     * 格式化double<br>
     * 对 {@link DecimalFormat} 做封装<br>
     *
     * @param pattern 格式 格式中主要以 # 和 0 两种占位符号来指定数字长度。0 表示如果位数不足则以 0 填充，# 表示只要有可能就把数字拉上这个位置。<br>
     *                <ul>
     *                <li>0 =》 取一位整数</li>
     *                <li>0.00 =》 取一位整数和两位小数</li>
     *                <li>00.000 =》 取两位整数和三位小数</li>
     *                <li># =》 取所有整数部分</li>
     *                <li>#.##% =》 以百分比方式计数，并取两位小数</li>
     *                <li>#.#####E0 =》 显示为科学计数法，并取五位小数</li>
     *                <li>,### =》 每三位以逗号进行分隔，例如：299,792,458</li>
     *                <li>光速大小为每秒,###米 =》 将格式嵌入文本</li>
     *                </ul>
     * @param value   值
     * @return 格式化后的值
     * @since 3.0.5
     */
    public static String decimalFormat(String pattern, long value) {
        return new DecimalFormat(pattern).format(value);
    }

    /**
     * 格式化double<br>
     * 对 {@link DecimalFormat} 做封装<br>
     *
     * @param pattern 格式 格式中主要以 # 和 0 两种占位符号来指定数字长度。0 表示如果位数不足则以 0 填充，# 表示只要有可能就把数字拉上这个位置。<br>
     *                <ul>
     *                <li>0 =》 取一位整数</li>
     *                <li>0.00 =》 取一位整数和两位小数</li>
     *                <li>00.000 =》 取两位整数和三位小数</li>
     *                <li># =》 取所有整数部分</li>
     *                <li>#.##% =》 以百分比方式计数，并取两位小数</li>
     *                <li>#.#####E0 =》 显示为科学计数法，并取五位小数</li>
     *                <li>,### =》 每三位以逗号进行分隔，例如：299,792,458</li>
     *                <li>光速大小为每秒,###米 =》 将格式嵌入文本</li>
     *                </ul>
     * @param value   值，支持BigDecimal、BigInteger、Number等类型
     * @return 格式化后的值
     * @since 5.1.6
     */
    public static String decimalFormat(String pattern, Object value) {
        return new DecimalFormat(pattern).format(value);
    }

    /**
     * 格式化金额输出，每三位用逗号分隔
     *
     * @param value 金额
     * @return 格式化后的值
     * @since 3.0.9
     */
    public static String decimalFormatMoney(double value) {
        return decimalFormat(",##0.00", value);
    }

    /**
     * 格式化百分比，小数采用四舍五入方式
     *
     * @param number 值
     * @param scale  保留小数位数
     * @return 百分比
     * @since 3.2.3
     */
    public static String formatPercent(double number, int scale) {
        final NumberFormat format = NumberFormat.getPercentInstance();
        format.setMaximumFractionDigits(scale);
        return format.format(number);
    }

}
