package com.xianmao.common.core.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class MoneyUtils {
    /**
     * 元转分
     */
    public static Integer yuanToFen(BigDecimal yuan) {
        return yuan.multiply(new BigDecimal(100)).intValue();
    }

    /**
     * 分转元
     */
    public static BigDecimal fenToYuan(Integer fen) {
        if (Objects.isNull(fen)) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(fen).
                divide(new BigDecimal(100)).setScale(2, RoundingMode.DOWN);
    }
}
