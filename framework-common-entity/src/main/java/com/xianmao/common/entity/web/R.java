package com.xianmao.common.entity.web;

import com.xianmao.common.entity.exception.ICode;
import com.xianmao.common.entity.exception.ServerErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class R<T> implements Serializable {

    private static final long serialVersionUID = 6439723870077111495L;
    /***响应参数*/
    private String code;
    private String message;
    private T data;
    /***响应状态码*/
    private static final String SUCCESS_CODE = ServerErrorCode.SUCCESS.getCode();
    private static final String FAILURE_CODE = ServerErrorCode.ERROR.getCode();

    public boolean isOk() {
        return Optional.ofNullable(this).map((x) -> {
            return Objects.equals(SUCCESS_CODE, x.getCode());
        }).orElse(Boolean.FALSE);
    }

    public static <T> R<T> buildAPIResult(ICode<String> iCode) {
        return new R<>(iCode.getCode(), iCode.getValue());
    }

    public static <T> R<T> buildAPIResult(ICode<String> iCode, String message) {
        return new R<>(iCode.getCode(), message);
    }

    public static <T> R<T> buildAPIResult(ICode<String> iCode, String message, T data) {
        return new R<>(iCode.getCode(), message, data);
    }

    public static <T> R<T> buildAPIResult(String code, String message) {
        return new R<>(code, message);
    }

    public static <T> R<T> buildAPIResult(String code, String message, T data) {
        return new R<>(code, message, data);
    }

    public static <T> R<T> success() {
        return R.buildAPIResult(SUCCESS_CODE, "操作成功", null);
    }

    public static <T> R<T> success(T data) {
        return R.buildAPIResult(SUCCESS_CODE, data == null ? "暂无数据" : "操作成功", data);
    }
    public static <T> R<T> fail() {
        return R.buildAPIResult(FAILURE_CODE, "操作失败", null);
    }

    public static <T> R<T> fail(ICode<String> iCode) {
        return R.buildAPIResult(iCode.getCode(), iCode.getValue(), null);
    }

    public static <T> R<T> fail(String msg) {
        return R.buildAPIResult(FAILURE_CODE, msg, null);
    }

    public static <T> R<T> fail(String code, String msg) {
        return R.buildAPIResult(code, msg, null);
    }

    public R(String code, String message) {
        super();
        this.code = code;
        this.message = message;
    }
}
