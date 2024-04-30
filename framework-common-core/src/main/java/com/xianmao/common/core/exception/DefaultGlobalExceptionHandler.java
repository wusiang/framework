package com.xianmao.common.core.exception;

import com.xianmao.common.core.utils.ExceptionUtils;
import com.xianmao.common.entity.web.ApiResult;
import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Map<Class, ICode> exceptionMap = new HashMap<Class, ICode>() {
        {
            put(HttpMessageNotReadableException.class, ServerErrorCode.ERROR);
            put(ClientAbortException.class, ServerErrorCode.ERROR);
            put(NoHandlerFoundException.class, ServerErrorCode.ERROR);
            put(ParseException.class, ServerErrorCode.ERROR);
            put(NumberFormatException.class, ServerErrorCode.ERROR);
            put(IllegalArgumentException.class, ServerErrorCode.ERROR);
            put(MaxUploadSizeExceededException.class, ServerErrorCode.ERROR);
            put(BindException.class, ServerErrorCode.ERROR);
        }
    };

    @ExceptionHandler(Exception.class)
    public ApiResult<?> handleException(Exception e) {
        ICode iCode = exceptionMap.getOrDefault(e, ServerErrorCode.ERROR);
        logger.error(Error.buildError(iCode, ExceptionUtils.getExceptionString(e)), e);
        return ApiResult.fail(Integer.parseInt(String.valueOf(iCode.getCode())), iCode.getValue());
    }

    @ExceptionHandler(BussinessException.class)
    public ApiResult<?> handleBussinessException(BussinessException bussinessException) {
        if (bussinessException.getCode().getCode().equals(ServerErrorCode.ERROR.getCode())) {
            logger.error(Error.buildError(bussinessException.getCode(), ExceptionUtils.getExceptionString(bussinessException)), bussinessException);
        } else {
            logger.warn(Error.buildError(bussinessException.getCode(), ExceptionUtils.getExceptionString(bussinessException)), bussinessException);
        }
        return ApiResult.fail(Integer.parseInt(String.valueOf(bussinessException.getCode().getCode())), bussinessException.getMessage());
    }

    /**
     * 校验异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ApiResult<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        logger.error(Error.buildError(ServerErrorCode.ERROR, ExceptionUtils.getExceptionString(e)), e);
        return ApiResult.fail(ServerErrorCode.ERROR.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResult<?> MethodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        logger.error(Error.buildError(ServerErrorCode.ERROR, ExceptionUtils.getExceptionString(e)), e);
        assert fieldError != null;
        return ApiResult.fail(ServerErrorCode.ERROR.getCode(), fieldError.getDefaultMessage());
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ApiResult<?> handleException(HttpRequestMethodNotSupportedException e) {
        logger.error(Error.buildError(ServerErrorCode.ERROR, ExceptionUtils.getExceptionString(e)), e);
        return ApiResult.fail("不支持' " + e.getMethod() + "'请求");
    }
}
