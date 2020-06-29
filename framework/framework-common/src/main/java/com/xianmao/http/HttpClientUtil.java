package com.xianmao.http;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName HttpClientUtils
 * @Description: TODO
 * @Author guyi
 * @Data 2019-08-21 08:50
 * @Version 1.0
 */
public class HttpClientUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    private HttpClientUtil() {
        throw new IllegalAccessError("HttpClientUtils工具类不能实例化");
    }

    private static PoolingHttpClientConnectionManager connectionManager = null;
    private static RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000)
            .setConnectionRequestTimeout(3000).build();

    static {
        connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(1000);
        //// 每个路由最大的请求数量
        connectionManager.setDefaultMaxPerRoute(200);
    }

    private static CloseableHttpClient getHttpClient() {
        return getHttpClientBuilder().build();
    }

    private static CloseableHttpClient getHttpClient(SSLContext sslContext) {
        return getHttpClientBuilder(sslContext).build();
    }

    private static HttpClientBuilder getHttpClientBuilder() {
        return HttpClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(requestConfig);
    }

    private static HttpClientBuilder getHttpClientBuilder(SSLContext sslContext) {
        if (sslContext != null) {
            return getHttpClientBuilder().setSSLContext(sslContext);
        } else {
            return getHttpClientBuilder();
        }
    }

    /**
     * post 请求
     *
     * @param httpUrl    请求地址
     * @param sslContext ssl证书信息
     * @return
     */
    public static String sendHttpPost(String httpUrl, SSLContext sslContext) {
        HttpPost httpPost = new HttpPost(httpUrl);
        return sendHttpPost(httpPost, sslContext);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     */
    public static String sendHttpPost(String httpUrl) {
        HttpPost httpPost = new HttpPost(httpUrl);
        return sendHttpPost(httpPost, null);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param params  参数(格式:key1=value1&key2=value2)
     */
    public static String sendHttpPost(String httpUrl, String params) {
        return sendHttpPost(httpUrl, params, null);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl    地址
     * @param params     参数(格式:key1=value1&key2=value2)
     * @param sslContext ssl证书信息
     */
    public static String sendHttpPost(String httpUrl, String params, SSLContext sslContext) {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        try {
            // 设置参数
            StringEntity stringEntity = new StringEntity(params, "UTF-8");
            stringEntity.setContentType("application/x-www-form-urlencoded");
            httpPost.setEntity(stringEntity);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return sendHttpPost(httpPost, sslContext);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param maps    参数
     */
    public static String sendHttpPost(String httpUrl, Map<String, String> maps) {
        return sendHttpPost(httpUrl, maps, null);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl    地址
     * @param maps       参数
     * @param sslContext ssl证书信息
     */
    public static String sendHttpPost(String httpUrl, Map<String, String> maps, SSLContext sslContext) {
        HttpPost httpPost = wrapHttpPost(httpUrl, maps);
        return sendHttpPost(httpPost, null);
    }

    /**
     * 封装获取HttpPost方法
     *
     * @param httpUrl
     * @param maps
     * @return
     */
    public static HttpPost wrapHttpPost(String httpUrl, Map<String, String> maps) {
        HttpPost httpPost = new HttpPost(httpUrl);
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        for (Map.Entry<String, String> m : maps.entrySet()) {
            nameValuePairs.add(new BasicNameValuePair(m.getKey(), m.getValue()));
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return httpPost;
    }

    /**
     * 发送Post请求
     *
     * @param httpPost
     * @return
     */
    public static String sendHttpPost(HttpPost httpPost) {
        return sendHttpPost(httpPost, null);
    }

    /**
     * 发送Post请求
     *
     * @param httpPost
     * @param sslConext ssl证书信息
     * @return
     */
    public static String sendHttpPost(HttpPost httpPost, SSLContext sslConext) {
        CloseableHttpClient httpClient = getHttpClient(sslConext);
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;
        try {
            // 执行请求
            response = httpClient.execute(httpPost);
            entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                if (entity != null) {
                    EntityUtils.consumeQuietly(entity);
                }
                if (response != null) {
                    response.close();
                }

            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

        }
        return responseContent;
    }

    /**
     * 发送 get请求
     *
     * @param httpUrl
     */
    public static String sendHttpGet(String httpUrl) {
        return sendHttpGet(httpUrl, null);
    }

    /**
     * 发送 get请求
     *
     * @param httpUrl
     * @param sslConext ssl证书信息
     */
    public static String sendHttpGet(String httpUrl, SSLContext sslConext) {
        HttpGet httpGet = new HttpGet(httpUrl);
        return sendHttpGet(httpGet, sslConext);
    }

    /**
     * 发送Get请求
     *
     * @param httpGet
     * @return
     */
    public static String sendHttpGet(HttpGet httpGet) {
        return sendHttpGet(httpGet, null);
    }

    /**
     * 发送Get请求
     *
     * @param httpGet
     * @param sslConext ssl证书信息
     * @return
     */
    public static String sendHttpGet(HttpGet httpGet, SSLContext sslConext) {
        CloseableHttpClient httpClient = getHttpClient(sslConext);
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;
        try {
            response = httpClient.execute(httpGet);
            entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                if (entity != null) {
                    EntityUtils.consumeQuietly(entity);
                }
                if (response != null) {
                    response.close();
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return responseContent;
    }

    /**
     * 发送 get请求
     *
     * @param httpUrl 请求路径
     * @param headers 请求头参数
     * @return
     */
    public static String sendHttpHeaderGet(String httpUrl, Map<String, String> headers) {
        HttpGet httpGet = new HttpGet(httpUrl);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            httpGet.setHeader(key, value);
        }
        return sendHttpGet(httpGet, null);
    }

    /**
     * Get 下载文件
     *
     * @param httpUrl
     * @param file
     * @return
     */
    public static File sendHttpGetFile(String httpUrl, File file) {

        if (file == null) {
            return null;
        }

        HttpGet httpGet = new HttpGet(httpUrl);
        CloseableHttpClient httpClient = getHttpClient();
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            response = httpClient.execute(httpGet);
            entity = response.getEntity();
            inputStream = entity.getContent();
            fileOutputStream = new FileOutputStream(file);
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = inputStream.read(buf, 0, 1024)) != -1) {
                fileOutputStream.write(buf, 0, len);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {

            try {

                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }

                if (entity != null) {
                    EntityUtils.consumeQuietly(entity);
                }
                if (response != null) {
                    response.close();
                }

            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

        }
        return file;
    }

    /**
     * Post 下载文件
     *
     * @param httpUrl
     * @param maps
     * @param file
     * @return
     */
    public static File sendHttpPostFile(String httpUrl, Map<String, String> maps, File file) {

        if (file == null) {
            return null;
        }

        HttpPost httpPost = wrapHttpPost(httpUrl, maps);

        CloseableHttpClient httpClient = getHttpClient();
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            // 执行请求
            response = httpClient.execute(httpPost);

            entity = response.getEntity();
            inputStream = entity.getContent();
            fileOutputStream = new FileOutputStream(file);
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = inputStream.read(buf, 0, 1024)) != -1) {
                fileOutputStream.write(buf, 0, len);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }

                if (entity != null) {
                    EntityUtils.consumeQuietly(entity);
                }
                if (response != null) {
                    response.close();
                }

            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

        }
        return file;
    }

}
