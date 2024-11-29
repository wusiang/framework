package com.xianmao.common.log.model;

import lombok.Data;

import java.util.Date;

@Data
public class CarOperateLog {
    /**
     * 操作人账号
     */
    private String operateAccount;
    /**
     * 操作人姓名
     */
    private String operateName;
    /**
     * 操作类型
     */
    private String operateType;
    /**
     * 操作模块
     */
    private String operateModule;
    /**
     * ip地址
     */
    private String ipAddress;
    /**
     * 描述
     */
    private String description;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 公司code
     */
    private Long companyCode;
    /**
     * appid
     */
    private String appId;
    /**
     * 操作来源
     */
    private String source;

    /**
     * 请求路径
     */
    private String uri;

    /**
     * 请求参数
     */
    private String param;
}
