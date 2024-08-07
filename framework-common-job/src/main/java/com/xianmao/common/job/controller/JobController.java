package com.xianmao.common.job.controller;

import com.xianmao.common.job.base.BaseSimpleJob;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    private ApplicationContext applicationContext;

    @PostMapping("/trigger")
    public String trigger(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        if (StringUtils.isBlank(name)) {
            return "Error bean name";
        }
        try {
            BaseSimpleJob job = (BaseSimpleJob) applicationContext.getBean(name);
            job.execute(null);
            return "successfully!";
        } catch (Exception e) {
            return "Error occurred during Elastic Job execution.";
        }
    }
}
