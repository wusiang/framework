package com.xianmao.rest;

import com.xianmao.enums.IEnum;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @ClassName RestAPI
 * @Description: 统一API响应结果封装
 * @Author guyi
 * @Data 2019-08-14 09:26
 * @Version 1.0
 */
public class APIResult<T> extends HashMap<String, Object> implements Serializable {

    private static final long serialVersionUID = 6439723870077111495L;
    private static String CODE_KEY = RConstant.DEFAULT_RESULT_CODE_KEY;
    private static String MESSAGE_KEY = RConstant.DEFAULT_RESULT_MESSAGE_KEY;
    private static String DATA_KEY = RConstant.DEFAULT_RESULT_DATA_KEY;
    private static String SUCCESS_KEY = null;
    private static String PAGE_KEY = RConstant.DEFAULT_RESULT_PAGE_KEY;
    private static int SUCCESS_CODE = ResultCode.SUCCESS.getCode();
    private static int FAILURE_CODE = ResultCode.BAD_REQUEST.getCode();

    public static void setCodeKeys(String codeKey, String messageKey, String dataKey, String successKey) {
        if (StringUtils.isNotBlank(codeKey)) CODE_KEY = codeKey;
        if (StringUtils.isNotBlank(messageKey)) MESSAGE_KEY = messageKey;
        if (StringUtils.isNotBlank(dataKey)) DATA_KEY = dataKey;
        if (StringUtils.isNotBlank(successKey)) SUCCESS_KEY = successKey;
    }

    public static void setDefaultSuccessCode(int successCode) {
        SUCCESS_CODE = successCode;
    }

    public static void setDefaultFailureCode(int failureCode) {
        FAILURE_CODE = failureCode;
    }

    public int getResultCode() {
        String code = String.valueOf(this.get(CODE_KEY));
        return Integer.parseInt(code);
    }

    public String getResultMessage() {
        String msg = String.valueOf(this.get(MESSAGE_KEY));
        return msg;
    }


    private APIResult(ResultCode resultCode) {
        this(resultCode.getCode(), resultCode.getValue());
    }

    private APIResult(ResultCode resultCode, String msg) {
        this(resultCode.getCode(), msg);
    }

    private APIResult(ResultCode resultCode, T data) {
        this(resultCode, data, resultCode.getValue());
    }

    private APIResult(ResultCode resultCode, T data, String msg) {
        this(resultCode.getCode(), data, msg);
    }

    private APIResult() {
        super();
    }

    private APIResult(int code, String message) {
        this.put(CODE_KEY, code);
        this.put(MESSAGE_KEY, message);
        appendSuccess(code);
    }

    private void appendSuccess(int code) {
        if (StringUtils.isNotBlank(SUCCESS_KEY)) {
            if (code >= 200 && code < 300) {
                this.put(SUCCESS_KEY, true);
            } else {
                this.put(SUCCESS_KEY, false);
            }
        }
    }

    private APIResult(int code, T data, String message) {
        this.put(CODE_KEY, code);
        this.put(MESSAGE_KEY, message);
        this.put(DATA_KEY, data);
        appendSuccess(code);
    }

    private APIResult(int code, Page<T> page, String message) {
        this.put(CODE_KEY, code);
        this.put(MESSAGE_KEY, message);
        this.put(DATA_KEY, page.getData());
        this.put(PAGE_KEY, page.getPage());
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
    public static <T> APIResult<T> success(int code, T data, String msg) {
        return new APIResult<>(code, data, data == null ? RConstant.DEFAULT_NULL_MESSAGE : msg);
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
        return success(data, RConstant.DEFAULT_SUCCESS_MESSAGE);
    }

    /**
     * 返回R
     *
     * @param page 分页数据
     * @param <T>  类型
     * @return
     */
    public static <T> APIResult<T> success(Page<T> page) {
        return success(page, RConstant.DEFAULT_SUCCESS_MESSAGE);
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
    public static <T> APIResult<T> failure(String msg) {
        return new APIResult<>(FAILURE_CODE, msg);
    }

    public static <T> APIResult<T> failure() {
        return new APIResult<>(FAILURE_CODE, RConstant.DEFAULT_FAILURE_MESSAGE);
    }


    /**
     * 返回R
     *
     * @param code 状态码
     * @param msg  消息
     * @param <T>  T 泛型标记
     * @return R
     */
    public static <T> APIResult<T> failure(int code, String msg) {
        return new APIResult<>(code, msg);
    }

    /**
     * 返回R
     *
     * @param resultCode 业务代码
     * @param <T>        T 泛型标记
     * @return R
     */
    public static <T> APIResult<T> failure(IEnum<Integer, String> resultCode) {
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
    public static <T> APIResult<T> failure(IEnum<Integer, String> resultCode, String msg) {
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

    public static String getSuccessKey() {
        return SUCCESS_KEY;
    }

    public static void setSuccessKey(String successKey) {
        SUCCESS_KEY = successKey;
    }

    public static String getPageKey() {
        return PAGE_KEY;
    }

    public static void setPageKey(String pageKey) {
        PAGE_KEY = pageKey;
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
