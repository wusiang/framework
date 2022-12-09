package com.xianmao.common.mybatis.base;


import com.baomidou.mybatisplus.annotation.*;
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
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /*** 更新时间*/
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /*** 状态[0:未删除,1:删除]*/
    @TableField(value = "del_flag", fill = FieldFill.INSERT)
    private Integer delFlag;
}
