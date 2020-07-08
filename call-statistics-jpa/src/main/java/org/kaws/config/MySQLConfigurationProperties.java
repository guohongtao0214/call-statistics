package org.kaws.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: Heiky
 * @Date: 2020/6/8 14:38
 * @Description:
 * @Modified By:
 */

@Setter
@Getter
@ConfigurationProperties(prefix = "call.statistics.mysql")
public class MySQLConfigurationProperties {

    private String active = "false";

    // =============================MySQL配置=============================
    private String url;

    private String username;

    private String password;

    private String driverClassName;

    private long connectionTimeout;

    private int minimumIdle;

    private int maximumPoolSize;

    private boolean autoCommit;

    private long idleTimeout;

    private String poolName;

    private long maxLifetime;

    private String connectionTestQuery;
    // =============================MySQL属性=============================


}
