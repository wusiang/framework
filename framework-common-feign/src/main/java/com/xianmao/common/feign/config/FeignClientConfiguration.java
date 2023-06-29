package com.xianmao.common.feign.config;


import com.xianmao.common.feign.http.HttpLoggingInterceptor;
import com.xianmao.common.feign.http.OkHttpSlf4jLogger;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.commons.httpclient.OkHttpClientConnectionPoolFactory;
import org.springframework.cloud.commons.httpclient.OkHttpClientFactory;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * feign配置okhttp3
 * 通过连接池，减少了请求延迟.
 * 缓存响应数据来减少重复的网络请求.
 * 允许连接到同一个主机地址的所有请求，提高请求效率.
 * 配置文件修改
 * feign.httpclient.enabled=false
 * feign.okhttp.enabled=true
 */
@AllArgsConstructor
@ConditionalOnClass(okhttp3.OkHttpClient.class)
@Configuration(proxyBeanMethods = false)
public class FeignClientConfiguration {

	/**
	 * 打印出BODY
	 * @return HttpLoggingInterceptor
	 */
	@Bean("httpLoggingInterceptor")
	public HttpLoggingInterceptor testLoggingInterceptor() {
		HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new OkHttpSlf4jLogger());
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
		return interceptor;
	}

	/**
	 * okhttp3 链接池配置
	 * @param connectionPoolFactory 链接池配置
	 * @param httpClientProperties httpClient配置
	 * @return okhttp3.ConnectionPool
	 */
	@Bean
	public okhttp3.ConnectionPool httpClientConnectionPool(FeignHttpClientProperties httpClientProperties, OkHttpClientConnectionPoolFactory connectionPoolFactory) {
		int maxTotalConnections = httpClientProperties.getMaxConnections();
		long timeToLive = httpClientProperties.getTimeToLive();
		TimeUnit ttlUnit = httpClientProperties.getTimeToLiveUnit();
		return connectionPoolFactory.create(maxTotalConnections, timeToLive, ttlUnit);
	}

	/**
	 * 配置OkHttpClient
	 * @param httpClientFactory httpClient 工厂
	 * @param connectionPool 链接池配置
	 * @param httpClientProperties httpClient配置
	 * @param interceptor 拦截器
	 * @return OkHttpClient
	 */
	@Bean
	public okhttp3.OkHttpClient httpClient(OkHttpClientFactory httpClientFactory, okhttp3.ConnectionPool connectionPool, FeignHttpClientProperties httpClientProperties, HttpLoggingInterceptor interceptor) {
		boolean followRedirects = httpClientProperties.isFollowRedirects();
		int connectTimeout = httpClientProperties.getConnectionTimeout();
		return httpClientFactory.createBuilder(httpClientProperties.isDisableSslValidation())
			.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
			.writeTimeout(60, TimeUnit.SECONDS)
			.readTimeout(60, TimeUnit.SECONDS)
			.followRedirects(followRedirects)
			.connectionPool(connectionPool)
			.addInterceptor(interceptor)
			.build();
	}
}
