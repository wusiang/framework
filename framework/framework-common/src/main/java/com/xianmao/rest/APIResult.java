package com.xianmao.rest;

import com.xianmao.constant.Constant;
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
    private static String CODE_KEY = Constant.Rest.DEFAULT_RESULT_CODE_KEY;
    private static String MESSAGE_KEY = Constant.Rest.DEFAULT_RESULT_MESSAGE_KEY;
    private static String DATA_KEY = Constant.Rest.DEFAULT_RESULT_DATA_KEY;
    /***响应状态码*/
    private static int SUCCESS_CODE = ResultCode.SUCCESS.getCode();
    private static int FAILURE_CODE = ResultCode.BAD_REQUEST.getCode();

    public int getResultCode() {
        String code = String.valueOf(this.get(CODE_KEY));
        return Integer.parseInt(code);
    }

    public String getResultMessage() {
        String msg = String.valueOf(this.get(MESSAGE_KEY));
        return msg;
    }

    public Object getResultData() {
        return this.get(DATA_KEY);
    }

    private APIResult() {
        super();
    }

    private APIResult(int code, String message) {
        this.put(CODE_KEY, code);
        this.put(MESSAGE_KEY, message);
    }

    private APIResult(int code, T data, String message) {
        this.put(CODE_KEY, code);
        this.put(MESSAGE_KEY, message);
        this.put(DATA_KEY, data);
    }

    private APIResult(int code, Page<T> page, String message) {
        this.put(CODE_KEY, code);
        this.put(MESSAGE_KEY, message);
        this.put(DATA_KEY, page.getData());
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
        return success(SUCCESS_CODE, data, msg);
    }

    public static <T> APIResult<T> success(Page<T> page, String msg) {
        return new APIResult<T>(SUCCESS_CODE, page, msg);
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
    public static <T> APIResult<T> success(int code, T data, String msg) {
        return new APIResult<>(code, data, data == null ? Constant.Rest.DEFAULT_NULL_MESSAGE : msg);
    }

    /**
     * 返回R
     *
     * @param data       数据
     * @param resultCode rcode
     * @param <T>        T 泛型标记
     * @return R
     */
    public static <T> APIResult<T> success(T data, IEnum<Integer, String> resultCode) {
        return success(resultCode.getCode(), data, resultCode.getValue());
    }

    /**
     * 返回R
     *
     * @param data 数据
     * @param <T>  T 泛型标记
     * @return R
     */
    public static <T> APIResult<T> success(T data) {
        return success(data, Constant.Rest.DEFAULT_SUCCESS_MESSAGE);
    }

    /**
     * 返回R
     *
     * @param page 分页数据
     * @param <T>  类型
     * @return
     */
    public static <T> APIResult<T> success(Page<T> page) {
        return success(page, Constant.Rest.DEFAULT_SUCCESS_MESSAGE);
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
     * @param resultCode 业务代码
     * @param msg        消息
     * @param <T>        T 泛型标记
     * @return R
     */
    public static <T> APIResult<T> success(IEnum<Integer, String> resultCode, String msg) {
        return new APIResult<>(resultCode.getCode(), msg);
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

    public static <T> APIResult<T> fail() {
        return new APIResult<>(FAILURE_CODE, Constant.Rest.DEFAULT_FAILURE_MESSAGE);
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
     * 返回R
     *
     * @param resultCode 业务代码
     * @param msg        消息
     * @param <T>        T 泛型标记
     * @return R
     */
    public static <T> APIResult<T> fail(IEnum<Integer, String> resultCode, String msg) {
        return new APIResult<>(resultCode.getCode(), msg);
    }

    /**
     * 空
     *
     * @return
     */
    public static <T> APIResult<T> none() {
        return new APIResult<>();
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

    /**
     * 请求成功是否
     *
     * @param result 响应体
     * @return
     */
    public static boolean isSuccess(@Nullable APIResult<?> result) {
        return Optional.ofNullable(result).map((x) -> {
            return ObjectUtil.nullSafeEquals(ResultCode.SUCCESS.getCode(), x.getResultCode());
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
     * 请求结果生成时间戳
     */
    private long timestamp = System.currentTimeMillis();

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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
