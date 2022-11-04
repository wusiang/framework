package com.xianmao.common.redo.schedule;

import com.xianmao.common.redo.core.RedoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 补偿任务调度器
 * 作用：查数据库表，逐个执行
*/
@Component
public class RedoTaskScheduler implements CommandLineRunner {

    private final static Logger LOGGER = LoggerFactory.getLogger(RedoTaskScheduler.class);

    private ScheduledExecutorService executorPool = Executors.newScheduledThreadPool(1);

    /**
     * 调度频率，单位：秒
     */
    //@Value("${kunghsu.redo.scheduleRate:1}")
    private Long scheduleRate = 10L;

    @Autowired
    private RedoManager redoManager;

    //初始化
    @PostConstruct
    public void init(){
        //注意，放在这里启动调度，mapper文件可能还没注册完毕，所以建议是放到CommandLineRunner里启动
    }

    @Override
    public void run(String... args) throws Exception {

        LOGGER.info("启动补偿重试组件Redo调度器");
        executorPool.scheduleAtFixedRate(new RedoTaskProcessTask(), 1, scheduleRate, TimeUnit.SECONDS);
    }

    private final class RedoTaskProcessTask implements Runnable {

        RedoTaskProcessTask() {

        }

        @Override
        public void run() {
            if (LOGGER.isDebugEnabled()){
                LOGGER.debug("RedoTaskProcessTask running!");
            }
            try {
                redoManager.findAndRedo();
            }catch (Throwable e){
                LOGGER.error("RedoTaskProcessTask error", e);
            }
        }
    }

}
