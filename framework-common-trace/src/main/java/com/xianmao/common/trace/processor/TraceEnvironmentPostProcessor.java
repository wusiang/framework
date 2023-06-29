package com.xianmao.common.trace.processor;

import com.xianmao.common.trace.TraceFormatEnum;
import com.xianmao.common.trace.constants.Constants;
import com.xianmao.common.trace.utils.EnvironmentUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日志格式处理器
 */
public class TraceEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final String PROPERTY_SOURCE_NAME = "defaultProperties";

    private static final String LEVEL_STR_ORIGINAL = "%5p [${spring.application.name:-},%X{X-TraceId:-},%X{X-RequestId:-}]";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        List<String> envList = getFormatEnv();
        Map<String, Object> map = new HashMap<>(1);
        map.put("logging.pattern.level", assemblyLevel(envList));
        EnvironmentUtils.addOrReplace(environment.getPropertySources(), map, PROPERTY_SOURCE_NAME);
    }

    /**
     * 获取 format list 对象
     */
    private List<String> getFormatEnv() {
        List<String> result = new ArrayList<>();
        // 获取自定义格式
        result.add(TraceFormatEnum.LOCAL_NAME.getValue());
        result.add(TraceFormatEnum.TRACE_ID.getValue());
        result.add(TraceFormatEnum.REQUEST_ID.getValue());
        return result;
    }

    /**
     * 处理输出格式 logging.pattern.level
     *
     * @param result 格式集合
     * @return 日志格式
     */
    private String assemblyLevel(List<String> result) {
        // 判断是否为空 返回默认格式
        if (result.isEmpty()) {
            return LEVEL_STR_ORIGINAL;
        }
        // 拼接 格式字符串
        StringBuilder sb = new StringBuilder("%5p [");
        for (String value : result) {
            if (Constants.LOCAL_NAME.equals(value)) {
                sb.append("${").append(value).append(":-}").append(",");
            } else if (TraceFormatEnum.LOCAL_NAME.name().equals(value)) {
                sb.append("${").append(Constants.LOCAL_NAME).append(":-}").append(",");
            } else {
                sb.append("%X{").append(value).append(":-}").append(",");
            }
        }
        sb.deleteCharAt(sb.length() - 1).append("]");
        return sb.toString();
    }

}
