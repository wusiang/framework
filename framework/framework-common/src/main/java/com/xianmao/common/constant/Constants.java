package com.xianmao.common.constant;

/**
 * @ClassName Constant
 * @Description: TODO
 * @Author wjh
 * @Data 2020-06-03 14:28
 * @Version 1.0
 */
public class Constants {

    /**
     * 项目环境类型
     */
    public final static class Environment {
        public static final String PROD = "prod";
        public static final String TEST = "test";
        public static final String DEV = "dev";
        public static final String PRE = "pre";
    }

    /**
     * 请求头参数
     */
    public final static class RestHeader {
        public static final String AUTHORIZATION = "Authorization";
    }

    /**
     * Content-type常见类型
     */
    public final static class ContentType {
        public static final String JSON_CONTENT_TYPE = "application/json; charset=utf-8";
        public static final String FORM_CONTENT_TYPE = "application/x-www-form-urlencoded;charset=utf-8";
        public static final String TEXT_XML_CONTENT_TYPE = "text/xml";
    }

    /**
     * sources
     */
    public final static class Sources {
        /***iOS-app*/
        public static final Integer iOS = 1;
        /***安卓-app*/
        public static final Integer Android = 2;
        /***web-app*/
        public static final Integer H5 = 3;
        /***微信小程序*/
        public static final Integer Wecat = 4;
        /***支付宝小程序*/
        public static final Integer Alipay = 5;
    }
}
