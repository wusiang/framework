package com.xianmao.common.core.exception;

import com.xianmao.common.core.utils.ExceptionUtils;
import com.xianmao.common.core.web.ApiResult;
import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.net.BindException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class DefaultGlobalExceptionHandler {
    private static Logger logger = LoggerFactory.getLogger(DefaultGlobalExceptionHandler.class);

    private static Map<Class, IErrorCode> exceptionMap = new HashMap<Class, IErrorCode>() {
        {
            put(HttpMessageNotReadableException.class, ServerErrorCode.INTERNAL_SERVER_ERROR);
            put(ClientAbortException.class, ServerErrorCode.INTERNAL_SERVER_ERROR);
            put(NoHandlerFoundException.class, ServerErrorCode.INTERNAL_SERVER_ERROR);
            put(ParseException.class, ServerErrorCode.INTERNAL_SERVER_ERROR);
            put(NumberFormatException.class, ServerErrorCode.INTERNAL_SERVER_ERROR);
            put(IllegalArgumentException.class, ServerErrorCode.INTERNAL_SERVER_ERROR);
            put(MaxUploadSizeExceededException.class, ServerErrorCode.INTERNAL_SERVER_ERROR);
            put(BindException.class, ServerErrorCode.MSG_NOT_READABLE);
        }
    };

    @ExceptionHandler(Exception.class)
    public ApiResult<?> handleException(Exception e) {
        IErrorCode iErrorCode = exceptionMap.getOrDefault(e, ServerErrorCode.INTERNAL_SERVER_ERROR);
        logger.error(Error.buildError(iErrorCode, ExceptionUtils.getExceptionString(e)));
        return ApiResult.fail(iErrorCode);
    }

    @ExceptionHandler(BussinessException.class)
    public ApiResult<?> handleBussinessException(BussinessException bussinessException) {
        logger.error(Error.buildError(bussinessException.getCode(), ExceptionUtils.getExceptionString(bussinessException)));
        return ApiResult.fail(bussinessException.getCode(), bussinessException.getMessage());
    }

    /**
     * 校验异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ApiResult<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        logger.error(Error.buildError(ServerErrorCode.INTERNAL_SERVER_ERROR, ExceptionUtils.getExceptionString(e)));
        return ApiResult.fail(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResult<?> MethodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        logger.error(Error.buildError(ServerErrorCode.INTERNAL_SERVER_ERROR, ExceptionUtils.getExceptionString(e)), e);
        assert fieldError != null;
        return ApiResult.fail(HttpStatus.BAD_REQUEST.value(), fieldError.getDefaultMessage());
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ApiResult<?> handleException(HttpRequestMethodNotSupportedException e) {
        logger.error(Error.buildError(ServerErrorCode.INTERNAL_SERVER_ERROR, ExceptionUtils.getExceptionString(e)));
        return ApiResult.fail("不支持' " + e.getMethod() + "'请求");
    }
}
