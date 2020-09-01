package com.xianmao.exception;

import com.xianmao.rest.APIResult;
import com.xianmao.rest.ResultCode;
import com.xianmao.utils.ServletUtil;
import com.xianmao.utils.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @ClassName BaseExceptionHandler
 * @Description: TODO
 * @Author guyi
 * @Data 2019-08-14 09:16
 * @Version 1.0
 */
public abstract class DefaultGlobalExceptionHandlerAdvice {

    private static Logger logger = LoggerFactory.getLogger(DefaultGlobalExceptionHandlerAdvice.class);

    @ExceptionHandler(Exception.class)
    public APIResult<?> handleException(HttpServletRequest req, Exception e) throws IOException {

        logger.error("method:{},header:{},param:{},ip:{},log:{},errorInfo__", WebUtil.getMethodName(req), ServletUtil.getHeaderMap(req).toString(),WebUtil.getRequestParamString(req), WebUtil.getIP(req), e);
        if (e instanceof BizException) {
            BizException bizException = (BizException) e;
            return APIResult.fail(bizException.getCode(), bizException.getMsg());
        } else {
            return APIResult.fail(ResultCode.INTERNAL_SERVER_ERROR);
        }
    }


}
