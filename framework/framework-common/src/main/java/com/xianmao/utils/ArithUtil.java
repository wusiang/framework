package com.xianmao.utils;

import java.math.BigDecimal;

/**
 * @ClassName ArithUtil
 * @Description: TODO
 * @Author guyi
 * @Data 2019-08-14 09:52
 * @Version 1.0
 */
public class ArithUtil {

    private static final int DEF_DIV_SCALE = 10;

    private ArithUtil() {
    }

    public static float add(float f1, float f2) {
        BigDecimal b1 = new BigDecimal(Float.toString(f1));
        BigDecimal b2 = new BigDecimal(Float.toString(f2));
        return b1.add(b2).floatValue();
    }

    public static float sub(float f1, float f2) {
        BigDecimal b1 = new BigDecimal(Float.toString(f1));
        BigDecimal b2 = new BigDecimal(Float.toString(f2));
        return b1.subtract(b2).floatValue();
    }

    public static float mul(float f1, float f2) {
        BigDecimal b1 = new BigDecimal(Float.toString(f1));
        BigDecimal b2 = new BigDecimal(Float.toString(f2));
        return b1.multiply(b2).floatValue();
    }

    public static float mul(float f1, int i1) {
        BigDecimal b1 = new BigDecimal(Float.toString(f1));
        BigDecimal b2 = new BigDecimal(Integer.toString(i1));
        return b1.multiply(b2).floatValue();
    }

    public static BigDecimal mul(BigDecimal b1, int i1) {
        return b1.multiply(new BigDecimal(i1));
    }

    public static BigDecimal mul(BigDecimal b1, float i1) {
        return b1.multiply(new BigDecimal(i1));
    }

    public static BigDecimal add(BigDecimal b1, BigDecimal b2) {
        return b1.add(b2);
    }

    public static BigDecimal sub(BigDecimal b1, BigDecimal b2) {
        if (b1 == null || b2 == null) {
            return null;
        }
        return b1.subtract(b2);
    }

    public static BigDecimal sub(BigDecimal b1, float f1) {
        return b1.subtract(new BigDecimal(f1));
    }

    public static double add(double d1, double d2) {
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.add(b2).doubleValue();
    }

    public static double sub(double d1, double d2) {
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.subtract(b2).doubleValue();
    }

    public static double mul(double d1, double d2) {
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.multiply(b2).doubleValue();
    }

    public static double div(double d1, double d2) {
        return div(d1, d2, DEF_DIV_SCALE);
    }

    public static double div(double d1, double d2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static BigDecimal toBigDecimal(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Integer) {
            return new BigDecimal(Integer.toString((Integer) obj));
        }
        if (obj instanceof String) {
            return new BigDecimal((String) obj);
        }
        if (obj instanceof Double) {
            return new BigDecimal(Double.toString((Double) obj));
        }
        return null;
    }
}
