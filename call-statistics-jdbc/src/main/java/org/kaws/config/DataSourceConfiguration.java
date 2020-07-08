package org.kaws.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.JdbcProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @Author: Heiky
 * @Date: 2020/7/8 10:22
 * @Description:
 */

@ConditionalOnProperty(prefix = "call.statistics.mysql", name = "active", havingValue = "true")
@ComponentScan(basePackages = "org.kaws")
@Configuration
public class DataSourceConfiguration {


    @Bean("callDataSource")
    public DataSource callDataSource(MySQLConfigurationProperties dbConfig) {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setDriverClassName(dbConfig.getDriverClassName());
        hikariDataSource.setJdbcUrl(dbConfig.getUrl());
        hikariDataSource.setUsername(dbConfig.getUsername());
        hikariDataSource.setPassword(dbConfig.getPassword());
        hikariDataSource.setConnectionTimeout(dbConfig.getConnectionTimeout());
        hikariDataSource.setMinimumIdle(dbConfig.getMinimumIdle());
        hikariDataSource.setMaximumPoolSize(dbConfig.getMaximumPoolSize());
        hikariDataSource.setAutoCommit(dbConfig.isAutoCommit());
        hikariDataSource.setIdleTimeout(dbConfig.getIdleTimeout());
        hikariDataSource.setPoolName(dbConfig.getPoolName());
        hikariDataSource.setMaxLifetime(dbConfig.getMaxLifetime());
        hikariDataSource.setConnectionTestQuery(dbConfig.getConnectionTestQuery());
        return hikariDataSource;
    }


    /**
     * 将上面的数据源注入到mainJdbcTemplate中
     *
     * @param dataSource
     * @return
     */
    @Bean(name = "callJdbcTemplate")
    public JdbcTemplate jdbcTemplate(@Qualifier("callDataSource") DataSource dataSource, JdbcProperties properties) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        JdbcProperties.Template template = properties.getTemplate();
        jdbcTemplate.setFetchSize(template.getFetchSize());
        jdbcTemplate.setMaxRows(template.getMaxRows());
        if (template.getQueryTimeout() != null) {
            jdbcTemplate.setQueryTimeout((int) template.getQueryTimeout().getSeconds());
        }
        return jdbcTemplate;
    }


}
