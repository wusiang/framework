package com.xianmao.common.feign.config;


import com.xianmao.common.feign.http.HttpLoggingInterceptor;
import com.xianmao.common.feign.http.OkHttpSlf4jLogger;
import com.xianmao.common.feign.ssl.DisableValidationTrustManager;
import com.xianmao.common.feign.ssl.TrustAllHostNames;
import com.xianmao.common.feign.utils.Holder;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
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
@Slf4j
@AllArgsConstructor
@RequiredArgsConstructor
@ConditionalOnClass(okhttp3.OkHttpClient.class)
@Configuration(proxyBeanMethods = false)
public class FeignClientConfiguration {

    private final HttpProperties properties;

    /**
     * 打印出BODY
     *
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
     */
    @Bean
    @ConditionalOnMissingBean
    public ConnectionPool httpClientConnectionPool() {
        int maxTotalConnections = properties.getMaxConnections();
        long timeToLive = properties.getTimeToLive();
        TimeUnit ttlUnit = properties.getTimeUnit();
        return new ConnectionPool(maxTotalConnections, timeToLive, ttlUnit);
    }

    /**
     * 配置OkHttpClient
     *
     * @param connectionPool 链接池配置
     * @param interceptor    拦截器
     * @return OkHttpClient
     */
    @Bean
    @ConditionalOnMissingBean
    public OkHttpClient okHttpClient(ConnectionPool connectionPool, HttpLoggingInterceptor interceptor) {
        boolean followRedirects = properties.isFollowRedirects();
        int connectTimeout = properties.getConnectionTimeout();
        return this.createBuilder(properties.isDisableSslValidation())
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .followRedirects(followRedirects)
                .connectionPool(connectionPool)
                .addInterceptor(interceptor)
                .build();
    }

    private OkHttpClient.Builder createBuilder(boolean disableSslValidation) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (disableSslValidation) {
            try {
                X509TrustManager disabledTrustManager = DisableValidationTrustManager.INSTANCE;
                TrustManager[] trustManagers = new TrustManager[]{disabledTrustManager};
                SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustManagers, Holder.SECURE_RANDOM);
                SSLSocketFactory disabledSslSocketFactory = sslContext.getSocketFactory();
                builder.sslSocketFactory(disabledSslSocketFactory, disabledTrustManager);
                builder.hostnameVerifier(TrustAllHostNames.INSTANCE);
            } catch (NoSuchAlgorithmException | KeyManagementException e) {
                log.warn("Error setting SSLSocketFactory in OKHttpClient", e);
            }
        }
        return builder;
    }
}
