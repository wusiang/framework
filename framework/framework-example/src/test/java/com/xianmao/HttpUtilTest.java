package com.xianmao;

import com.xianmao.http.HttpUtil;
import com.xianmao.http.HttpResponse;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName HttpClientUtilTest
 * @Description: TODO
 * @Author wjh
 * @Data 2020/7/25 9:19 下午
 * @Version 1.0
 */
public class HttpUtilTest {

    @Test
    public void TestHttp() throws Exception{
        /*
        Map<String, Object> param = new HashMap<>();
        param.put("token","LvfalYqkeVkAL1TGWfup8ycRv3QUUiKl67MH+%2FDUHab3%2FhRTqTZVz1L0bhFLk2b1");
        param.put("mid",48);
        String url = "https://api-uat.ym3456.com:8450/ym/nc/getModelShowreel";
        Result result = HttpClientUtil.doGet(url, param);
        System.out.println(result.getCode());
        System.out.println(result.getBody());
         */
        Map<String, Object> param = new HashMap<>();
        param.put("token","LvfalYqkeVkAL1TGWfup8ycRv3QUUiKl67MH+%2FDUHab3%2FhRTqTZVz1L0bhFLk2b1");
        param.put("mid",48);
        String url = "http://127.0.0.1:8080/ym/nc/getModelShowreel";
        HttpResponse httpResponse = HttpUtil.doGet(url, param);
        System.out.println(httpResponse.getCode());
        System.out.println(httpResponse.getBody());
    }
}
