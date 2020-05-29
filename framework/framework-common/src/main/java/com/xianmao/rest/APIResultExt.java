package com.xianmao.rest;

import java.io.Serializable;
import java.util.Map;

/**
 * @ClassName APIResultExt
 * @Description: TODO
 * @Author guyi
 * @Data 2019-12-31 11:41
 * @Version 1.0
 */
public class APIResultExt<T> implements Serializable {

    private static int SUCCESS_CODE = ResultCode.SUCCESS.getCode();
    private static int FAILURE_CODE = ResultCode.BAD_REQUEST.getCode();
    /**
     * 状态码
     */
    private int code;
    /**
     * 承载数据
     */
    private T data;
    /**
     * 返回消息
     */
    private String message;
    /**
     * 成功
     */
    private boolean success;
    /**
     * 分页消息
     */
    private Map<String, Object> page;

    public static void setDefaultSuccessCode(int successCode) {
        SUCCESS_CODE = successCode;
    }

    public static void setDefaultFailureCode(int failureCode) {
        FAILURE_CODE = failureCode;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    private APIResultExt() {
    }

    private APIResultExt(ResultCode resultCode) {
        this(resultCode.getCode(), resultCode.getValue());
    }

    private APIResultExt(ResultCode resultCode, String msg) {
        this(resultCode.getCode(), msg);
    }

    private APIResultExt(ResultCode resultCode, T data) {
        this(resultCode, data, resultCode.getValue());
    }

    private APIResultExt(ResultCode resultCode, T data, String msg) {
        this(resultCode.getCode(), data, msg);
    }


    private APIResultExt(int code, String message) {
        this.code = code;
        this.message = message;
        appendSuccess(code);
    }

    private void appendSuccess(int code) {
        if (code >= 200 && code < 300) {
            this.success = true;
        }
    }

    private APIResultExt(int code, T data, String message) {
        this.code = code;
        this.message = message;
        this.data = data;
        appendSuccess(code);
    }

    private APIResultExt(int code, Page<T> page, String message) {
        this.code = code;
        this.message = message;
        this.data = page.getData();
        this.page = page.getPageInfo();
        appendSuccess(code);
    }

    /**
     * 返回R
     *
     * @param code 状态码
     * @param data 数据
     * @param msg  消息
     * @param <T>  T 泛型标记
     * @return R
     */
    public static <T> APIResultExt<T> success(int code, T data, String msg) {
        return new APIResultExt<>(code, data, data == null ? RConstant.DEFAULT_NULL_MESSAGE : msg);
    }

    /**
     * 返回R
     *
     * @param data 数据
     * @param msg  消息
     * @param <T>  T 泛型标记
     * @return R
     */
    public static <T> APIResultExt<T> success(T data, String msg) {
        return success(SUCCESS_CODE, data, msg);
    }

    public static <T> APIResultExt<T> success(Page<T> page, String msg) {
        return new APIResultExt<T>(SUCCESS_CODE, page, msg);
    }

    /**
     * 返回R
     *
     * @param data       数据
     * @param resultCode rcode
     * @param <T>        T 泛型标记
     * @return R
     */
    public static <T> APIResultExt<T> success(T data, ResultCode resultCode) {
        return success(resultCode.getCode(), data, resultCode.getValue());
    }

    /**
     * 返回R
     *
     * @param data 数据
     * @param <T>  T 泛型标记
     * @return R
     */
    public static <T> APIResultExt<T> success(T data) {
        return success(data, RConstant.DEFAULT_SUCCESS_MESSAGE);
    }

    public static <T> APIResultExt<T> success() {
        return success(RConstant.DEFAULT_SUCCESS_MESSAGE);
    }

    public static <T> APIResultExt<T> success(Page<T> page) {
        return success(page, RConstant.DEFAULT_SUCCESS_MESSAGE);
    }

    /**
     * 返回R
     *
     * @param msg 消息
     * @param <T> T 泛型标记
     * @return R
     */
    public static <T> APIResultExt<T> success(String msg) {
        return new APIResultExt<>(SUCCESS_CODE, msg);
    }

    /**
     * 返回R
     *
     * @param resultCode 业务代码
     * @param <T>        T 泛型标记
     * @return R
     */
    public static <T> APIResultExt<T> success(ResultCode resultCode) {
        return new APIResultExt<>(resultCode);
    }

    /**
     * 返回R
     *
     * @param resultCode 业务代码
     * @param msg        消息
     * @param <T>        T 泛型标记
     * @return R
     */
    public static <T> APIResultExt<T> success(ResultCode resultCode, String msg) {
        return new APIResultExt<>(resultCode, msg);
    }

    /**
     * 返回R
     *
     * @param msg 消息
     * @param <T> T 泛型标记
     * @return R
     */
    public static <T> APIResultExt<T> failure(String msg) {
        return new APIResultExt<>(FAILURE_CODE, msg);
    }

    public static <T> APIResultExt<T> failure() {
        return new APIResultExt<>(FAILURE_CODE, RConstant.DEFAULT_FAILURE_MESSAGE);
    }


    /**
     * 返回R
     *
     * @param code 状态码
     * @param msg  消息
     * @param <T>  T 泛型标记
     * @return R
     */
    public static <T> APIResultExt<T> failure(int code, String msg) {
        return new APIResultExt<>(code, msg);
    }

    /**
     * 返回R
     *
     * @param resultCode 业务代码
     * @param <T>        T 泛型标记
     * @return R
     */
    public static <T> APIResultExt<T> failure(ResultCode resultCode) {
        return new APIResultExt<>(resultCode);
    }

    /**
     * 返回R
     *
     * @param resultCode 业务代码
     * @param msg        消息
     * @param <T>        T 泛型标记
     * @return R
     */
    public static <T> APIResultExt<T> failure(ResultCode resultCode, String msg) {
        return new APIResultExt<>(resultCode, msg);
    }

    /**
     * 空
     *
     * @return
     */
    public static <T> APIResultExt<T> none() {
        return new APIResultExt<>();
    }

    public static int getSuccessCode() {
        return SUCCESS_CODE;
    }

    public static void setSuccessCode(int successCode) {
        SUCCESS_CODE = successCode;
    }

    public static int getFailureCode() {
        return FAILURE_CODE;
    }

    public static void setFailureCode(int failureCode) {
        FAILURE_CODE = failureCode;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Map<String, Object> getPage() {
        return page;
    }

    public void setPage(Map<String, Object> page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "APIResultExt{" +
                "code=" + code +
                ", data=" + data +
                ", message='" + message + '\'' +
                ", success=" + success +
                ", page=" + page +
                '}';
    }
}
