package com.xianmao.boot.config;

import com.xianmao.boot.web.exception.DefaultGlobalExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandleConfiguration extends DefaultGlobalExceptionHandler {
}
