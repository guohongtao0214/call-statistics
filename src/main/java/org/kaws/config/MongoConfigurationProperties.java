package org.kaws.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: Heiky
 * @Date: 2020/6/20 22:30
 * @Description: 设置mongodb的连接参数，默认值请参考 com.mongodb.MongoClientOptions
 */

@Setter
@Getter
@ConfigurationProperties(prefix = "call.statistics.mongo")
public class MongoConfigurationProperties {

    private String active = "false";

    private String uri;

    private int minPoolSize = 0;

    private int maxPoolSize = 100;

    private int connectTimeOut = 10000;

    private int maxIdleTime = 0;

    private int maxLifeTime = 0;

    private int socketTimeout = 0;

    private int waitQueueMultiple = 5;

    private int waitQueueTimeout = 120000;

}
