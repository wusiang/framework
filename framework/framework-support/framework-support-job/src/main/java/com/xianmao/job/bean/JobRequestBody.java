package com.xianmao.job.bean;

public class JobRequestBody {
    /**
     * 作业名称
     */
    private String name;

    /**
     * 作业是否启用
     */
    private boolean enable = true;

    /**
     * 作业的描述，说明本作业的作用
     */
    private String description;

    /**
     * 作业执行时间
     */
    private String cron;

    /**
     * 作业分片总数
     */
    private int shardingCount = 1;

    /**
     * 是否开启失效转移，开启表示如果作业在一次任务执行中途宕机，允许将该次未完成的任务在另一作业节点上补偿执行
     */
    private boolean failover = false;

    /**
     * 是否开启错过任务重新执行
     */
    private boolean misfire = true;

    /**
     * 脚本目录，仅用在脚本作业中
     */
    private String script = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public int getShardingCount() {
        return shardingCount;
    }

    public void setShardingCount(int shardingCount) {
        this.shardingCount = shardingCount;
    }

    public boolean isFailover() {
        return failover;
    }

    public void setFailover(boolean failover) {
        this.failover = failover;
    }

    public boolean isMisfire() {
        return misfire;
    }

    public void setMisfire(boolean misfire) {
        this.misfire = misfire;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }
}
