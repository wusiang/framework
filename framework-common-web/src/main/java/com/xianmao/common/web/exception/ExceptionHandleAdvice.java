package com.xianmao.common.web.exception;

import com.xianmao.common.core.exception.DefaultGlobalExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandleAdvice extends DefaultGlobalExceptionHandler {
}
