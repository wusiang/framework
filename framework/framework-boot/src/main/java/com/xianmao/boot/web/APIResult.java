package com.xianmao.boot.web;

import com.xianmao.enums.IEnum;
import com.xianmao.obj.ObjectUtil;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.Optional;

/**
 * @ClassName RestAPI
 * @Description: 统一API响应结果封装
 * @Author guyi
 * @Data 2019-08-14 09:26
 * @Version 1.0
 */
public class APIResult<T> implements Serializable {

    private static final long serialVersionUID = 6439723870077111495L;
    /***响应参数*/
    private Integer code;
    private String message;
    private T data;
    private Boolean success;
    /***响应状态码*/
    private static int SUCCESS_CODE = ResultCode.SUCCESS.getCode();
    private static int FAILURE_CODE = ResultCode.FAILURE.getCode();

    private APIResult() {
        super();
    }

    /**
     * 请求成功是否
     *
     * @param result 响应体
     * @return
     */
    public static boolean isSuccess(@Nullable APIResult<?> result) {
        return Optional.ofNullable(result).map((x) -> {
            return ObjectUtil.nullSafeEquals(ResultCode.SUCCESS.getCode(), x.getCode());
        }).orElse(Boolean.FALSE);
    }

    /**
     * 请求失败是否
     *
     * @param result 响应体
     * @return
     */
    public static boolean isNotSuccess(@Nullable APIResult<?> result) {
        return !isSuccess(result);
    }

    /**
     * 返回R
     *
     * @param <T> T 泛型标记
     * @return
     */
    public static <T> APIResult<T> success() {
        return restResult(SUCCESS_CODE, "操作成功", null, Boolean.TRUE);
    }

    /**
     * 返回R
     *
     * @param <T>  T 泛型标记
     * @param data 数据
     * @param msg  消息
     * @return R
     */
    public static <T> APIResult success(T data, String msg) {
        return restResult(SUCCESS_CODE, data == null ? "暂无数据" : "操作成功", data, Boolean.TRUE);
    }

    /**
     * 返回R
     *
     * @param data 数据
     * @param <T>  T 泛型标记
     * @return R
     */
    public static <T> APIResult<T> success(T data) {
        return restResult(SUCCESS_CODE, data == null ? "暂无数据" : "操作成功", data, Boolean.TRUE);
    }

    /**
     * 返回R
     *
     * @param resultCode 业务代码
     * @param <T>        T 泛型标记
     * @return R
     */
    public static <T> APIResult<T> success(IEnum<Integer, String> resultCode) {
        return restResult(resultCode.getCode(), resultCode.getValue(), null, Boolean.FALSE);
    }

    /**
     * 返回R
     *
     * @param <T> T 泛型标记
     * @return
     */
    public static <T> APIResult<T> fail() {
        return restResult(FAILURE_CODE, "操作失败", null, Boolean.FALSE);
    }

    /**
     * 返回R
     *
     * @param msg 消息
     * @param <T> T 泛型标记
     * @return R
     */
    public static <T> APIResult<T> fail(String msg) {
        return restResult(FAILURE_CODE, msg, null, Boolean.FALSE);
    }


    /**
     * 返回R
     *
     * @param code 状态码
     * @param msg  消息
     * @param <T>  T 泛型标记
     * @return R
     */
    public static <T> APIResult<T> fail(int code, String msg) {
        return restResult(code, msg, null, Boolean.FALSE);
    }

    /**
     * 返回R
     *
     * @param resultCode 业务代码
     * @param <T>        T 泛型标记
     * @return R
     */
    public static <T> APIResult<T> fail(IEnum<Integer, String> resultCode) {
        return restResult(resultCode.getCode(), resultCode.getValue(), null, Boolean.FALSE);
    }

    public static <T> APIResult<T> restResult(Integer code, String message, T data, Boolean success) {
        APIResult<T> apiResult = new APIResult<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setSuccess(success);
        apiResult.setMessage(message);
        return apiResult;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
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

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }
}
