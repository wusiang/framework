package com.xianmao.common.entity.web;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

@Setter
@Getter
public class ApiResult<T> implements Serializable {

    private static final long serialVersionUID = 6439723870077111495L;
    /***响应参数*/
    private Integer code;
    private String message;
    private T data;
    private Boolean success;
    /***响应状态码*/
    private static int SUCCESS_CODE = 200;
    private static int FAILURE_CODE = 500;

    private ApiResult() {
        super();
    }

    /**
     * 请求成功是否
     *
     * @param result 响应体
     * @return
     */
    public static boolean isSuccess(ApiResult<?> result) {
        return Optional.ofNullable(result).map((x) -> {
            return Objects.equals(SUCCESS_CODE, x.getCode());
        }).orElse(Boolean.FALSE);
    }

    /**
     * 请求失败是否
     *
     * @param result 响应体
     * @return
     */
    public static boolean isNotSuccess(ApiResult<?> result) {
        return !isSuccess(result);
    }

    /**
     * 返回R
     *
     * @param <T> T 泛型标记
     * @return
     */
    public static <T> ApiResult<T> success() {
        return restResult(SUCCESS_CODE, "操作成功", null, Boolean.TRUE);
    }

    /**
     * 返回R
     *
     * @param data 数据
     * @param <T>  T 泛型标记
     * @return R
     */
    public static <T> ApiResult<T> success(T data) {
        return restResult(SUCCESS_CODE, data == null ? "暂无数据" : "操作成功", data, Boolean.TRUE);
    }

    /**
     * 返回R
     *
     * @param <T> T 泛型标记
     * @return
     */
    public static <T> ApiResult<T> fail() {
        return restResult(FAILURE_CODE, "操作失败", null, Boolean.FALSE);
    }

    /**
     * 返回R
     *
     * @param msg 消息
     * @param <T> T 泛型标记
     * @return R
     */
    public static <T> ApiResult<T> fail(String msg) {
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
    public static <T> ApiResult<T> fail(int code, String msg) {
        return restResult(code, msg, null, Boolean.FALSE);
    }

    public static <T> ApiResult<T> restResult(Integer code, String message, T data, Boolean success) {
        ApiResult<T> apiResult = new ApiResult<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setSuccess(success);
        apiResult.setMessage(message);
        return apiResult;
    }
}
