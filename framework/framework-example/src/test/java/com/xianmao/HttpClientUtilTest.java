package com.xianmao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xianmao.http.HttpClientUtil;
import com.xianmao.http.Result;
import com.xianmao.utils.StringUtil;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName HttpClientUtilTest
 * @Description: TODO
 * @Author wjh
 * @Data 2020/7/25 9:19 下午
 * @Version 1.0
 */
public class HttpClientUtilTest {

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
        Result result = HttpClientUtil.doGet(url, param);
        System.out.println(result.getCode());
        System.out.println(result.getBody());
    }
}
