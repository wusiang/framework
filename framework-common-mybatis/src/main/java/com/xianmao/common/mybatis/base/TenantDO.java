package com.xianmao.common.mybatis.base;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class TenantDO extends BaseDO{

    /**租户ID */
    @TableField(value = "tenant_id")
    private String tenantId;
}
