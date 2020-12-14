package com.xianmao.rest;

import com.xianmao.constant.Constants;
import com.xianmao.enums.IEnum;
import com.xianmao.obj.ObjectUtil;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Optional;

/**
 * @ClassName RestAPI
 * @Description: 统一API响应结果封装
 * @Author guyi
 * @Data 2019-08-14 09:26
 * @Version 1.0
 */
public class APIResult<T> extends HashMap<String, Object> implements Serializable {

    private static final long serialVersionUID = 6439723870077111495L;
    /***响应参数*/
    private static String CODE_KEY = "code";
    private static String MESSAGE_KEY = "message";
    private static String DATA_KEY = "data";
    private static String SUCCESS_KEY = "success";
    /***响应状态码*/
    private static int SUCCESS_CODE = ResultCode.SUCCESS.getCode();
    private static int FAILURE_CODE = ResultCode.FAILURE.getCode();

    private APIResult() {
        super();
    }

    /**
     * 获取请求状态码
     *
     * @return
     */
    public int getCode() {
        String code = String.valueOf(this.get(CODE_KEY));
        return Integer.parseInt(code);
    }

    /**
     * 获取请求错误消息
     *
     * @return
     */
    public String getMessage() {
        return String.valueOf(this.get(MESSAGE_KEY));
    }

    /**
     * 获取请求数据
     *
     * @return
     */
    public Object getData() {
        return this.get(DATA_KEY);
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
        return new APIResult<>(SUCCESS_CODE, "操作成功");
    }

    /**
     * 返回R
     *
     * @param data 数据
     * @param msg  消息
     * @param <T>  T 泛型标记
     * @return R
     */
    public static <T> APIResult<T> success(T data, String msg) {
        return new APIResult<>(SUCCESS_CODE, data, msg);
    }

    /**
     * 返回R
     *
     * @param data 数据
     * @param <T>  T 泛型标记
     * @return R
     */
    public static <T> APIResult<T> success(T data) {
        return new APIResult<>(SUCCESS_CODE, data, data == null ? "暂无数据" : "操作成功");
    }

    /**
     * 返回R
     *
     * @param resultCode 业务代码
     * @param <T>        T 泛型标记
     * @return R
     */
    public static <T> APIResult<T> success(IEnum<Integer, String> resultCode) {
        return new APIResult<>(resultCode.getCode(), resultCode.getValue());
    }

    /**
     * 返回R
     *
     * @param <T> T 泛型标记
     * @return
     */
    public static <T> APIResult<T> fail() {
        return new APIResult<>(FAILURE_CODE, "操作失败");
    }

    /**
     * 返回R
     *
     * @param msg 消息
     * @param <T> T 泛型标记
     * @return R
     */
    public static <T> APIResult<T> fail(String msg) {
        return new APIResult<>(FAILURE_CODE, msg);
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
        return new APIResult<>(code, msg);
    }

    /**
     * 返回R
     *
     * @param resultCode 业务代码
     * @param <T>        T 泛型标记
     * @return R
     */
    public static <T> APIResult<T> fail(IEnum<Integer, String> resultCode) {
        return new APIResult<>(resultCode.getCode(), resultCode.getValue());
    }

    /**
     * 需要扩展属性时使用
     *
     * @param key   属性名
     * @param value 属性值
     * @return R
     */
    public APIResult<T> fill(String key, Object value) {
        this.put(key, value);
        return this;
    }

    private APIResult(int code, String message) {
        boolean success = code == SUCCESS_CODE;
        this.put(CODE_KEY, code);
        this.put(MESSAGE_KEY, message);
        this.put(SUCCESS_KEY, success);
    }

    private APIResult(int code, T data, String message) {
        boolean success = code == SUCCESS_CODE;
        this.put(CODE_KEY, code);
        this.put(MESSAGE_KEY, message);
        this.put(DATA_KEY, data);
        this.put(SUCCESS_KEY, success);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public static String getCodeKey() {
        return CODE_KEY;
    }

    public static void setCodeKey(String codeKey) {
        CODE_KEY = codeKey;
    }

    public static String getMessageKey() {
        return MESSAGE_KEY;
    }

    public static void setMessageKey(String messageKey) {
        MESSAGE_KEY = messageKey;
    }

    public static String getDataKey() {
        return DATA_KEY;
    }

    public static void setDataKey(String dataKey) {
        DATA_KEY = dataKey;
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


}
