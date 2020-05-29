package com.xianmao.rest;

/**
 * @ClassName RConstant
 * @Description: 系统常量
 * @Author guyi
 * @Data 2019-12-31 11:18
 * @Version 1.0
 */
public interface RConstant {
    /**
     * 编码
     */
    String UTF_8 = "UTF-8";
    /**
     * JSON 资源
     */
    String CONTENT_TYPE = "application/json; charset=utf-8";
    /**
     * 默认为空消息
     */
    String DEFAULT_NULL_MESSAGE = "暂无数据";
    /**
     * 默认成功消息
     */
    String DEFAULT_SUCCESS_MESSAGE = "成功";
    /**
     * 默认失败消息
     */
    String DEFAULT_FAILURE_MESSAGE = "失败";
    /**
     * 默认未授权消息
     */
    String DEFAULT_UNAUTHORIZED_MESSAGE = "认证失败";
    /**
     * 返回结果默认key名称
     */
    String DEFAULT_RESULT_CODE_KEY = "resultCode";
    String DEFAULT_RESULT_MESSAGE_KEY = "resultMessage";
    String DEFAULT_RESULT_DATA_KEY = "data";
    String DEFAULT_RESULT_SUCCESS_KEY = "success";
    String DEFAULT_RESULT_PAGE_KEY = "page";
}
