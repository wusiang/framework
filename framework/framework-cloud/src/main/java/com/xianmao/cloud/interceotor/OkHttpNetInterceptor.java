package com.xianmao.cloud.interceotor;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class OkHttpNetInterceptor implements Interceptor{
    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request().newBuilder().addHeader("Connection","close").build();
        return chain.proceed(request);
    }
}
