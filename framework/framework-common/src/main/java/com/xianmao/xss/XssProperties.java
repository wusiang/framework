package com.xianmao.xss;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName XssProperties
 * @Description: TODO
 * @Author wjh
 * @Data 2020-06-25 09:44
 * @Version 1.0
 */
public class XssProperties {
    /**
     * 开启xss
     */
    private Boolean enabled = true;

    /**
     * 放行url
     */
    private List<String> skipUrl = new ArrayList<>();

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getSkipUrl() {
        return skipUrl;
    }

    public void setSkipUrl(List<String> skipUrl) {
        this.skipUrl = skipUrl;
    }
}
