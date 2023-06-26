package com.xianmao.cloud.http;

import okhttp3.*;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.platform.Platform;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static okhttp3.internal.platform.Platform.INFO;

public final class HttpLoggingInterceptor implements Interceptor {
    private static final Charset UTF8 = StandardCharsets.UTF_8;

    public enum Level {
        /**
         * 无日志
         */
        NONE,
        /**
         * 记录请求和响应行
         *
         * <p>例如:
         * <pre>{@code
         * --> POST /greeting http/1.1 (3-byte body)
         * <-- 200 OK (22ms, 6-byte body)
         * }</pre>
         */
        BASIC,
        /**
         * 记录请求和响应行及请求头
         *
         * <p>例如:
         * <pre>{@code
         * --> POST /greeting http/1.1
         * Host: example.com
         * --> END POST
         *
         * <-- 200 OK (22ms)
         * <-- END HTTP
         * }</pre>
         */
        HEADERS,
        /**
         * 记录请求和响应行及其各请求头和响应body
         *
         * <p>例如:
         * <pre>{@code
         * --> POST /greeting http/1.1
         * Host: example.com
         *
         * Hi?
         * --> END POST
         *
         * <-- 200 OK (22ms)
         *
         * Hello!
         * <-- END HTTP
         * }</pre>
         */
        BODY
    }

    public interface Logger {
        void log(String message);
        Logger DEFAULT = message -> Platform.get().log(message, INFO, null);
    }

    public HttpLoggingInterceptor() {
        this(Logger.DEFAULT);
    }

    public HttpLoggingInterceptor(Logger logger) {
        this.logger = logger;
    }

    private final Logger logger;

    private volatile Level level = Level.NONE;

    /**
     * Change the level at which this interceptor logs.
     *
     * @param level log Level
     * @return HttpLoggingInterceptor
     */
    public HttpLoggingInterceptor setLevel(Level level) {
        Objects.requireNonNull(level, "level == null. Use Level.NONE instead.");
        this.level = level;
        return this;
    }

    public Level getLevel() {
        return level;
    }

	@Override
    public Response intercept(Chain chain) throws IOException {
        Level level = this.level;

        Request request = chain.request();
        if (level == Level.NONE) {
            return chain.proceed(request);
        }

        boolean logBody = level == Level.BODY;
        boolean logHeaders = logBody || level == Level.HEADERS;

        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        Connection connection = chain.connection();
        String requestStartMessage = "open-feign-start --> " + request.method() + ' ' + request.url() + (connection != null ? " " + connection.protocol() : "");
        if (!logHeaders && hasRequestBody) {
            requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
        }
        logger.log(requestStartMessage);
        if (logHeaders) {
            if (!logBody || !hasRequestBody) {
                logger.log("open-feign-start --> END " + request.method());
            } else if (bodyHasUnknownEncoding(request.headers())) {
                logger.log("open-feign-start --> END " + request.method() + " (encoded body omitted)");
            } else {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);

                Charset charset = StandardCharsets.UTF_8;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }
                if (isPlaintext(buffer)) {
                    logger.log("open-feign-start --> request-body:"+buffer.readString(charset));
                    logger.log("open-feign-start --> END " + request.method() + " (" + requestBody.contentLength() + "-byte body)");
                } else {
                    logger.log("open-feign-start --> END " + request.method() + " (binary " + requestBody.contentLength() + "-byte body omitted)");
                }
            }
        }

        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            logger.log("<-- HTTP FAILED: " + e);
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
        logger.log("open-feign-end <-- " + response.code() + (response.message().isEmpty() ? "" : ' ' + response.message()) + ' ' + response.request().url() + " (" + tookMs + "ms" + (!logHeaders ? ", " + bodySize + " body" : "") + ')');

        if (logHeaders) {
            Headers headers = response.headers();
            if (!logBody || !HttpHeaders.hasBody(response)) {
                logger.log("open-feign-end <-- END HTTP");
            } else if (bodyHasUnknownEncoding(response.headers())) {
                logger.log("open-feign-end <-- END HTTP (encoded body omitted)");
            } else {
                BufferedSource source = responseBody.source();
                // Buffer the entire body.
                source.request(Long.MAX_VALUE);
                Buffer buffer = source.buffer();

                Long gzippedLength = null;
				String contentEncoding = "Content-Encoding";
				String gzip = "gzip";
				if (gzip.equalsIgnoreCase(headers.get(contentEncoding))) {
                    gzippedLength = buffer.size();
                    GzipSource gzippedResponseBody = null;
                    try {
                        gzippedResponseBody = new GzipSource(buffer.clone());
                        buffer = new Buffer();
                        buffer.writeAll(gzippedResponseBody);
                    } finally {
                        if (gzippedResponseBody != null) {
                            gzippedResponseBody.close();
                        }
                    }
                }

                Charset charset = UTF8;
                MediaType contentType = responseBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }

                if (!isPlaintext(buffer)) {
                    logger.log("open-feign-end <-- END HTTP (binary " + buffer.size() + "-byte body omitted)");
                    return response;
                }

                if (contentLength != 0) {
                    logger.log("open-feign-end <-- response-body:"+buffer.clone().readString(charset));
                }

                if (gzippedLength != null) {
                    logger.log("open-feign-end <-- END HTTP (" + buffer.size() + "-byte, " + gzippedLength + "-gzipped-byte body)");
                } else {
                    logger.log("open-feign-end <-- END HTTP (" + buffer.size() + "-byte body)");
                }
            }
        }

        return response;
    }

    private static int plainCnt = 16;

    private static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < plainCnt; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            // Truncated UTF-8 sequence.
            return false;
        }
    }

    private boolean bodyHasUnknownEncoding(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !"identity".equalsIgnoreCase(contentEncoding) && !"gzip".equalsIgnoreCase(contentEncoding);
    }
}
