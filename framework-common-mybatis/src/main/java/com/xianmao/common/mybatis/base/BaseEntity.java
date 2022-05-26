package com.xianmao.common.mybatis.base;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础实体类
 *
 * @author Chill
 */
@Setter
@Getter
public class BaseEntity implements Serializable {

    /*** 主键*/
    @TableId(type = IdType.AUTO)
    private Long id;

    /*** 创建时间*/
    @TableField("create_time")
    private LocalDateTime createTime;
    /*** 更新时间*/
    @TableField("update_time")
    private LocalDateTime updateTime;

    /*** 状态[0:未删除,1:删除]*/
    @TableLogic
    @TableField("del_flag")
    private Integer delFlag;
}
