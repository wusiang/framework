package com.xianmao.common.core.exception;

import com.xianmao.common.core.web.APIResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.BindException;

public class DefaultGlobalExceptionHandler {
    private static Logger logger = LoggerFactory.getLogger(DefaultGlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public APIResult<?> handleException(HttpServletRequest req, Exception e) throws IOException {
        if (e instanceof BussinessException) {
            BussinessException bussinessException = (BussinessException) e;
            return APIResult.fail(bussinessException.getCode(), bussinessException.getMessage());
        } else {
            return APIResult.fail(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 校验异常
     */
    @ExceptionHandler(value = BindException.class)
    public APIResult<?> validationExceptionHandler(HttpServletRequest req, BindException e) {
        return APIResult.fail(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    /**
     * 校验异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public APIResult<?> handleMissingServletRequestParameterException(HttpServletRequest req, MissingServletRequestParameterException e) {
        return APIResult.fail(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public APIResult<?> handleException(HttpServletRequest req, HttpRequestMethodNotSupportedException e) {
        return APIResult.fail("不支持' " + e.getMethod() + "'请求");
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(BussinessException.class)
    public Object businessException(HttpServletRequest req, BussinessException e) {
        return APIResult.fail(e.getMessage());
    }
}
