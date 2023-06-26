package com.xianmao.cloud.http;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OkHttpSlf4jLogger implements HttpLoggingInterceptor.Logger {
	@Override
	public void log(String message) {
		log.info(message);
	}
}
