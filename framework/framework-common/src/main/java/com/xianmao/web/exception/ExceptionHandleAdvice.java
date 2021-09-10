package com.xianmao.web.exception;

import com.xianmao.exception.DefaultGlobalExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandleAdvice extends DefaultGlobalExceptionHandler {
}
