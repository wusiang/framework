package com.xianmao.common.redo.dao.entity;

import java.util.Date;

public class RedoTaskDO {
    private String redoTaskId;

    /**
     * 应用名：默认和spring的spring.application.name保持一致
     */
    private String applicationName;

    /**
     * 最大重试次数
     */
    private int maxAttempts;

    /**
     * 已执行次数，默认是0
     */
    private int execTimes;

    /**
     * 是否一直重试
     * 假如设置为true，则maxAttempts无效
     */
    private boolean tryForever;

    /**
     * 过期日期
     * 任务超过了这个日期，将不会再被重试
     */
    private Date expiredDate;

    private String reqParam;

    private Date createTime = new Date();

    public String getRedoTaskId() {
        return redoTaskId;
    }

    public void setRedoTaskId(String redoTaskId) {
        this.redoTaskId = redoTaskId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public boolean isTryForever() {
        return tryForever;
    }

    public void setTryForever(boolean tryForever) {
        this.tryForever = tryForever;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getReqParam() {
        return reqParam;
    }

    public void setReqParam(String reqParam) {
        this.reqParam = reqParam;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getExecTimes() {
        return execTimes;
    }

    public void setExecTimes(int execTimes) {
        this.execTimes = execTimes;
    }
}
