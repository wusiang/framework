package com.xianmao.common.core.web;

import cn.hutool.core.util.ObjectUtil;
import com.xianmao.common.core.exception.IErrorCode;
import com.xianmao.common.core.exception.ServerErrorCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.Optional;

@Setter
@Getter
public class APIResult<T> implements Serializable {

    private static final long serialVersionUID = 6439723870077111495L;
    /***响应参数*/
    private Integer code;
    private String message;
    private T data;
    private Boolean success;
    /***响应状态码*/
    private static int SUCCESS_CODE = ServerErrorCode.SUCCESS.getCode();
    private static int FAILURE_CODE = ServerErrorCode.FAILURE.getCode();

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
            return ObjectUtil.equal(ServerErrorCode.SUCCESS.getCode(), x.getCode());
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
     * @param iErrorCode 状态码
     * @param msg  消息
     * @param <T>  T 泛型标记
     * @return R
     */
    public static <T> APIResult<T> fail(IErrorCode iErrorCode, String msg) {
        return restResult(iErrorCode.getCode(), msg, null, Boolean.FALSE);
    }

    /**
     * 返回R
     *
     * @param errorCode 业务代码
     * @param <T>        T 泛型标记
     * @return R
     */
    public static <T> APIResult<T> fail(IErrorCode errorCode) {
        return restResult(errorCode.getCode(), errorCode.getValue(), null, Boolean.FALSE);
    }

    public static <T> APIResult<T> restResult(Integer code, String message, T data, Boolean success) {
        APIResult<T> apiResult = new APIResult<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setSuccess(success);
        apiResult.setMessage(message);
        return apiResult;
    }
}
