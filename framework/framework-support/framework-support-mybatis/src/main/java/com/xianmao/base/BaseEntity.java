package com.xianmao.base;

import com.baomidou.mybatisplus.annotation.TableLogic;

import java.util.Date;

/**
 * @ClassName BaseEntity
 * @Description: TODO
 * @Author wjh
 * @Data 2020/11/9 12:12 上午
 * @Version 1.0
 */
public class BaseEntity {

    /**
     * 创建时间
     */
    private Date createTime;


    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 状态[0:未删除,1:删除]
     */
    @TableLogic
    private Integer isDeleted;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
