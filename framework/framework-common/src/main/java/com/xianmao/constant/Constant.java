package com.xianmao.constant;

/**
 * @ClassName Constant
 * @Description: TODO
 * @Author wjh
 * @Data 2020-06-03 14:28
 * @Version 1.0
 */
public class Constant {

    /** 项目环境类型*/
    public final static class Environment {
        public static final String PROD = "prod";
        public static final String TEST = "TEST";
        public static final String DEV = "dev";
        public static final String PRE = "pre";
    }

    /** APIResult参数常量配置*/
    public final static class Rest {
        public static final String DEFAULT_NULL_MESSAGE = "暂无数据";
        public static final String DEFAULT_SUCCESS_MESSAGE = "操作成功";
        public static final String DEFAULT_FAILURE_MESSAGE = "操作失败";
        public static final String DEFAULT_UNAUTHORIZED_MESSAGE = "认证失败";
        public static final String DEFAULT_RESULT_CODE_KEY = "code";
        public static final String DEFAULT_RESULT_MESSAGE_KEY = "message";
        public static final String DEFAULT_RESULT_DATA_KEY = "data";
        public static final String DEFAULT_RESULT_SUCCESS_KEY = "success";
        public static final String DEFAULT_RESULT_PAGE_KEY = "page";
        public static final String TOKEN = "token";
        public static final String AUTHORIZATION = "Authorization";
    }

    /** Content-type常见类型*/
    public final static class ContentType {
        public static final String JSON_CONTENT_TYPE = "application/json; charset=utf-8";
        public static final String FORM_CONTENT_TYPE = "application/x-www-form-urlencoded;charset=utf-8";
        public static final String TEXT_XML_CONTENT_TYPE = "text/xml";
    }
}
