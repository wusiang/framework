package com.xianmao.exception;

import com.xianmao.enums.IEnum;
import com.xianmao.utils.StringUtil;

/**
 * @ClassName BizException
 * @Description: TODO
 * @Author guyi
 * @Data 2019-08-14 09:36
 * @Version 1.0
 */
public class BizException extends RuntimeException {

    private static final long serialVersionUID = -9220071231712256287L;
    /** 错误内容 */
    private String message;
    /** 错误状态码 */
    private Integer code = 500;

    public BizException(String message) {
        this(message, 500);
    }

    public BizException(IEnum<Integer, String> iEnum) {
        super(iEnum.getValue());
        this.code = iEnum.getCode();
        this.message = iEnum.getValue();
    }

    public BizException(String message, Integer code) {
        this(message, code, 500);
        this.message = message;
        this.code = code;
    }

    public BizException(String message, Integer code, int status) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BizException(String msg, Throwable cause) {
        super(msg, cause);
        this.message = msg;
    }

    public BizException(String msg, Throwable cause, int code) {
        super(msg, cause);
        this.message = msg;
        this.code = code;
    }

    public BizException(String messageTemplate, Object... params) {
        super(StringUtil.format(messageTemplate, params));
    }

    public String getMsg() {
        return message;
    }

    public void setMsg(String msg) {
        this.message = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
