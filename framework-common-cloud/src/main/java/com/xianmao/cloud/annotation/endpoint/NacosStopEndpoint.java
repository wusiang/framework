package com.xianmao.cloud.annotation.endpoint;

import com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/** 使用了nacos注册中心的服务关闭端点配置 */
@ConditionalOnClass(NacosAutoServiceRegistration.class)
@RestController
@RequestMapping("actuator")
@RequiredArgsConstructor
@Slf4j
public class NacosStopEndpoint {
    private final NacosAutoServiceRegistration nacosAutoServiceRegistration;
    private final ApplicationContext context;

    /** 注销服务后关闭应用前等待的时间(毫秒) */
    private int waitTime = 10000;

    /**
     * 关闭服务 <br>
     * 只接收localhost发起的请求
     *
     * @param request
     * @return
     */
    @PostMapping("stopService")
    public ResponseEntity<Boolean> stopNacosService(
            HttpServletRequest request) {
        if (!request.getServerName().equalsIgnoreCase("localhost"))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        new Thread(
                () -> {
                    log.info("Ready to stop service");
                    nacosAutoServiceRegistration.stop();
                    log.info("Nacos instance has been de-registered");
                    log.info("Waiting {} milliseconds...", waitTime);
                    try {
                        Thread.sleep(waitTime);
                    } catch (InterruptedException e) {
                        log.info("interrupted!", e);
                    }
                    log.info("Closing application...");
                    SpringApplication.exit(context);
                    ((ConfigurableApplicationContext) context).close();
                })
                .start();

        return ResponseEntity.ok(true);
    }
}
