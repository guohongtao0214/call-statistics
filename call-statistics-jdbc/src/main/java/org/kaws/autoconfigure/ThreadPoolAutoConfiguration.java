package org.kaws.autoconfigure;

import org.kaws.config.ScheduledThreadPoolExecutorProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

/**
 * @Author: Heiky
 * @Date: 2020/7/4 23:25
 * @Description:
 */

@Configuration
@EnableConfigurationProperties(ScheduledThreadPoolExecutorProperties.class)
public class ThreadPoolAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ScheduledThreadPoolExecutor customScheduledThreadPool(ScheduledThreadPoolExecutorProperties scheduledThreadPoolExecutorProperties) {
        ScheduledThreadPoolExecutor executor =
                new ScheduledThreadPoolExecutor(scheduledThreadPoolExecutorProperties.getCorePoolSize(), r -> {
                    Thread t = new Thread(r);
                    t.setName("save-records-thread");
                    // 设置为守护线程
                    t.setDaemon(true);
                    return t;
                });
        return executor;
    }

}
