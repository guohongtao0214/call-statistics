package org.kaws.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: Heiky
 * @Date: 2020/6/20 22:30
 * @Description:
 */

@Setter
@Getter
@ConfigurationProperties(prefix = "call.statistics.mongo")
public class MongoConfigurationProperties {

    private String active = "false";

    private String uri;

    private Integer minConnectionsPerHost = 5;

    private Integer maxConnectionsPerHost = 10;

}
