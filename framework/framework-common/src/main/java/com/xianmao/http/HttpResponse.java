package com.xianmao.http;

import java.io.Serializable;

/**
 * @ClassName HttpClientResult
 * @Description: TODO
 * @Author wjh
 * @Data 2020/7/25 9:15 下午
 * @Version 1.0
 */
public class HttpResponse implements Serializable {

    /**
     * 响应状态码
     */
    private int code;

    /**
     * 响应数据
     */
    private String body;

    public HttpResponse() {
    }

    public HttpResponse(int code) {
        this.code = code;
    }

    public HttpResponse(String body) {
        this.body = body;
    }

    public HttpResponse(int code, String body) {
        this.code = code;
        this.body = body;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Result [code=" + code + ", body=" + body + "]";
    }

}