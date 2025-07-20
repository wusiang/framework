package com.xianmao.common.core.snowflake;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class DefaultIdeable implements Ideable<String> {

    /**
     * 锁对象，可以为任意对象
     */
    private static final Object lockObj = "lockerOrder";
    /**
     * 订单号生成计数器
     */
    public static long codeNumCount = 0L;
    /**
     * 每毫秒生成订单号数量最大值
     */
    public static int maxPerMSECSize = 1000;


    /**
     * 生成非重复订单号，理论上限1毫秒1000个，可扩展
     */
    @Override
    public String generateId() {
        String finCodeNum = "";
        try {
            // 最终生成的订单号
            synchronized (lockObj) {
                // 取系统当前时间作为订单号变量前半部分，精确到毫秒
                long nowLong = Long.parseLong(new SimpleDateFormat("yyMMddHHmmssSSS").format(new Date()));
                // 计数器到最大值归零，可扩展更大，目前1毫秒处理峰值1000个，1秒100万
                if (codeNumCount > maxPerMSECSize) {
                    codeNumCount = 0L;
                }
                //组装订单号
                String countStr = maxPerMSECSize + codeNumCount + "";
                finCodeNum = nowLong + countStr.substring(1);
                codeNumCount++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finCodeNum;
    }
}
