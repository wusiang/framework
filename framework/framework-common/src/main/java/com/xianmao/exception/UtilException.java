package com.xianmao.exception;

import com.xianmao.exception.util.ExceptionUtil;
import com.xianmao.utils.StringUtil;

/**
 * @ClassName UtilException
 * @Description: TODO
 * @Author wjh
 * @Data 2020/7/14 3:50 下午
 * @Version 1.0
 */
public class UtilException extends RuntimeException {
    private static final long serialVersionUID = 8247610319171014183L;

    public UtilException(Throwable e) {
        super(ExceptionUtil.getMessage(e), e);
    }

    public UtilException(String message) {
        super(message);
    }

    public UtilException(String messageTemplate, Object... params) {
        super(StringUtil.format(messageTemplate, params));
    }

    public UtilException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public UtilException(Throwable throwable, String messageTemplate, Object... params) {
        super(StringUtil.format(messageTemplate, params), throwable);
    }
}
