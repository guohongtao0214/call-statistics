package org.kaws.autoconfigure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: Heiky
 * @Date: 2020/6/20 19:12
 * @Description:
 */

@Configuration
@Import({MySQLCallStatisticsAutoConfiguration.class, MongoCallStatisticsAutoConfiguration.class, ThreadPoolAutoConfiguration.class})
public class CallStatisticsAutoConfiguration {

    @Bean
    public Lock lock() {
        return new ReentrantLock();
    }

}
