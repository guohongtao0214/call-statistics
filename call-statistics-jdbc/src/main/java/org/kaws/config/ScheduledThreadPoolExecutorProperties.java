package org.kaws.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: Heiky
 * @Date: 2020/7/6 22:47
 * @Description:
 */

@Setter
@Getter
@ConfigurationProperties(prefix = "call.statistics.scheduled")
public class ScheduledThreadPoolExecutorProperties {

    /**
     * 默认1，设置守护线程
     */
    private int corePoolSize = 1;

}
