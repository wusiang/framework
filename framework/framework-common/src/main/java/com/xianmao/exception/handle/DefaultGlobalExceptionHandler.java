package com.xianmao.exception.handle;

import com.xianmao.exception.BizException;
import com.xianmao.rest.APIResult;
import com.xianmao.rest.ResultCode;
import com.xianmao.utils.ServletUtil;
import com.xianmao.utils.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.BindException;

/**
 * @ClassName BaseExceptionHandler
 * @Description: TODO
 * @Author guyi
 * @Data 2019-08-14 09:16
 * @Version 1.0
 */
public abstract class DefaultGlobalExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(DefaultGlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public APIResult<?> handleException(HttpServletRequest req, Exception e) throws IOException {

        logger.error("method:{},header:{},param:{},ip:{},log:{},errorInfo__", WebUtil.getMethodName(req), ServletUtil.getHeaderMap(req).toString(), WebUtil.getRequestParamString(req), WebUtil.getIP(req), e);
        if (e instanceof BizException) {
            BizException bizException = (BizException) e;
            return APIResult.fail(bizException.getCode(), bizException.getMsg());
        } else {
            return APIResult.fail(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 校验异常
     */
    @ExceptionHandler(value = BindException.class)
    public APIResult<?> validationExceptionHandler(HttpServletRequest req, BindException e) {
        logger.error("method:{},header:{},param:{},ip:{},log:{},errorInfo__", WebUtil.getMethodName(req), ServletUtil.getHeaderMap(req).toString(), WebUtil.getRequestParamString(req), WebUtil.getIP(req), e);
        return APIResult.fail(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    /**
     * 校验异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public APIResult<?> handleMissingServletRequestParameterException(HttpServletRequest req, MissingServletRequestParameterException e) {
        logger.error("method:{},header:{},param:{},ip:{},log:{},errorInfo__", WebUtil.getMethodName(req), ServletUtil.getHeaderMap(req).toString(), WebUtil.getRequestParamString(req), WebUtil.getIP(req), e);
        return APIResult.fail(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public APIResult<?> handleException(HttpServletRequest req, HttpRequestMethodNotSupportedException e) {
        logger.error("method:{},header:{},param:{},ip:{},log:{},errorInfo__", WebUtil.getMethodName(req), ServletUtil.getHeaderMap(req).toString(), WebUtil.getRequestParamString(req), WebUtil.getIP(req), e);
        return APIResult.fail("不支持' " + e.getMethod() + "'请求");
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public APIResult<?> notFount(HttpServletRequest req, RuntimeException e) {
        logger.error("method:{},header:{},param:{},ip:{},log:{},errorInfo__", WebUtil.getMethodName(req), ServletUtil.getHeaderMap(req).toString(), WebUtil.getRequestParamString(req), WebUtil.getIP(req), e);
        return APIResult.fail("运行时异常:" + e.getMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(BizException.class)
    public Object businessException(HttpServletRequest req, BizException e) {
        logger.error("method:{},header:{},param:{},ip:{},log:{},errorInfo__", WebUtil.getMethodName(req), ServletUtil.getHeaderMap(req).toString(), WebUtil.getRequestParamString(req), WebUtil.getIP(req), e);
        return APIResult.fail(e.getMessage());
    }


}
