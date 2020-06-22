package org.kaws.autoconfigure;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Author: Heiky
 * @Date: 2020/6/20 19:12
 * @Description:
 */

@Configuration
@Import({MySQLCallStatisticsAutoConfiguration.class, MongoCallStatisticsAutoConfiguration.class})
public class CallStatisticsAutoConfiguration {
}
