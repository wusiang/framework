package com.xianmao.cloud.config;

import com.xianmao.cloud.interceotor.OkHttpLogInterceptor;
import com.xianmao.cloud.interceotor.OkHttpNetInterceptor;
import feign.Feign;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@ConditionalOnClass(Feign.class)
@AutoConfigureBefore(FeignAutoConfiguration.class)
public class FeignOkHttpConfiguration {
    @Bean
    public okhttp3.OkHttpClient okHttpClient(OkHttpLogInterceptor okHttpLogInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                //设置连接超时
                .connectTimeout(60, TimeUnit.SECONDS)
                //设置读超时
                .readTimeout(60, TimeUnit.SECONDS)
                //设置写超时
                .writeTimeout(120, TimeUnit.SECONDS)
                //是否自动重连
                .retryOnConnectionFailure(false)
                .addNetworkInterceptor(new OkHttpNetInterceptor())
                .connectionPool(new ConnectionPool());
        return builder.build();
    }
}
