package org.kaws.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @Author: Heiky
 * @Date: 2020/7/4 23:25
 * @Description:
 */

@Configuration
public class ThreadPoolAutoConfiguration  {

    @Bean
    @ConditionalOnMissingBean
    public ScheduledThreadPoolExecutor customScheduledThreadPool() {
        return new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors());
    }

}
