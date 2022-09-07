package com.xianmao.common.job.config;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.xianmao.common.job.annotation.Job;
import com.xianmao.common.job.base.BaseSimpleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@EnableConfigurationProperties(JobProperties.class)
public class JobAutoConfiguration extends JobBaseConfiguration implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(JobAutoConfiguration.class);
    @Autowired
    private JobProperties jobProperties;
    @Autowired
    private Environment environment;

    @Override
    public void afterPropertiesSet() {
        List<JobScheduler> jobSchedulerList = loadJobs();
        if (jobSchedulerList == null || jobSchedulerList.size() == 0) {
            return;
        }
        for (JobScheduler jobScheduler : jobSchedulerList) {
            try {
                jobScheduler.init();
            } catch (Exception e) {
                log.error("start elastic job error.", e);
            }
        }
    }

    /**
     * 加载所有的job
     */
    private List<JobScheduler> loadJobs() {
        List<JobScheduler> jobSchedulerList = new ArrayList<>();
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(Job.class);
        if (CollectionUtils.isEmpty(beans)) {
            return null;
        }
        for (Object o : beans.values()) {
            try {
                if (!BaseSimpleJob.class.isAssignableFrom(o.getClass())) {
                    throw new RuntimeException(o.getClass().getName() + " - consumer未实现Consumer抽象类");
                }
                JobScheduler jobScheduler = createJob(o.getClass().getName());
                if (jobScheduler != null) {
                    log.info(String.format("%s Job is Star Work", o.getClass().getName()));
                    jobSchedulerList.add(jobScheduler);
                }
            } catch (Exception e) {
                log.error(o.getClass().getName(), e);
            }
        }
        return jobSchedulerList;
    }

    /**
     * 创建作业
     *
     * @param className 作业类名
     * @return 作业
     * @throws Exception
     */
    protected JobScheduler createJob(String className) throws Exception {
        if (Class.forName(className).isAnnotationPresent(Job.class)) {
            Job annotation = Class.forName(className).getAnnotation(Job.class);
            boolean jobEnable = annotation.enable();
            if (!jobEnable) {
                return null;
            }

            Class<?> superclass = Class.forName(className).getSuperclass();
            if (superclass == null) {
                return null;
            }
            if (BaseSimpleJob.class.getSimpleName().equals(superclass.getSimpleName())) {
                return createSimpleJob(className, annotation);
            }
        }
        return null;
    }

    /**
     * 创建普通作业
     *
     * @param className
     * @param annotation
     * @return
     */
    private JobScheduler createSimpleJob(String className, Job annotation) {
        String applicationName = environment.getProperty("spring.application.name");
        if (applicationName == null) {
            applicationName = "" ;
        } else {
            applicationName = applicationName + "." ;
        }
        JobCoreConfiguration simpleCoreConfig = JobCoreConfiguration.newBuilder(applicationName + className, annotation.cron(), annotation.shardingCount())
                .description(annotation.description())
                .failover(annotation.failover())
                .misfire(annotation.misfire())
                .build();
        SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(simpleCoreConfig, className);
        return new JobScheduler(createRegistryCenter(), LiteJobConfiguration.newBuilder(simpleJobConfig).overwrite(true).build());
    }

    /**
     * 创建注册中心
     *
     * @return
     */
    private CoordinatorRegistryCenter createRegistryCenter() {
        String zkServerList = jobProperties.getAddress();
        log.info("zkServerList:" + zkServerList);
        zkServerList = zkServerList.replace("zookeeper://", "");
        CoordinatorRegistryCenter regCenter = new ZookeeperRegistryCenter(new ZookeeperConfiguration(zkServerList, jobProperties.getNamespace()));
        regCenter.init();
        return regCenter;
    }


}
