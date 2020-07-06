package org.kaws.autoconfigure;

import org.kaws.config.ScheduledThreadPoolExecutorProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledThreadPoolExecutor;

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
        return new ScheduledThreadPoolExecutor(scheduledThreadPoolExecutorProperties.getCorePoolSize());
    }

}
