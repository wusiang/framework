package com.xianmao.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class CustomShutdown implements TomcatConnectorCustomizer, ApplicationListener<ContextClosedEvent> {

    public static final int TIME_OUT = 30;

    private volatile Connector connector;

    @Override
    public void customize(Connector connector) {
        this.connector = connector;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        // 暂停接受请求
        this.connector.pause();
        // 获取connector对应的线程池
        Executor executor = this.connector.getProtocolHandler().getExecutor();
        if (executor instanceof ThreadPoolExecutor) {
            try {
                log.info("准备关闭应用");
                ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
                // 初始化一个关闭任务位于当前待处理完毕的任务之后，并拒绝新的任务提交
                threadPoolExecutor.shutdown();
                if (!threadPoolExecutor.awaitTermination(TIME_OUT, TimeUnit.SECONDS)) {
                    log.info("应用等待关闭超过最大时长 " + TIME_OUT + "秒，将强制关闭");
                    threadPoolExecutor.shutdownNow();
                    if (threadPoolExecutor.awaitTermination(TIME_OUT, TimeUnit.SECONDS)) {
                        log.error("应用关闭失败");
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Bean
    public ConfigurableServletWebServerFactory webServerFactory(final CustomShutdown customShutdown) {
        TomcatServletWebServerFactory tomcatServletWebServerFactory = new TomcatServletWebServerFactory();
        tomcatServletWebServerFactory.addConnectorCustomizers(customShutdown);
        return tomcatServletWebServerFactory;
    }
}
