package com.xianmao.xss;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName XssUrlProperties
 * @Description: TODO
 * @Author wjh
 * @Data 2020-06-25 09:45
 * @Version 1.0
 */
public class XssUrlProperties {
    private final List<String> excludePatterns = new ArrayList<>();

    public List<String> getExcludePatterns() {
        return excludePatterns;
    }
}
