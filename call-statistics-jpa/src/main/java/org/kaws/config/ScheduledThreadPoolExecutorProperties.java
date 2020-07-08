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

    private int corePoolSize = Runtime.getRuntime().availableProcessors();

}
