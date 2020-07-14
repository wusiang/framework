package com.xianmao.http;

/**
 * @ClassName StreamProgress
 * @Description: TODO
 * @Author wjh
 * @Data 2020/7/14 4:10 下午
 * @Version 1.0
 */
public interface StreamProgress {
    /**
     * 开始
     */
    void start();

    /**
     * 进行中
     * @param progressSize 已经进行的大小
     */
    void progress(long progressSize);

    /**
     * 结束
     */
    void finish();
}
