package org.kaws.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;

/**
 * @Author: Heiky
 * @Date: 2020/6/8 10:08
 * @Description:
 * @Modified By:
 */
@Configuration
@ComponentScan(basePackages = "org.kaws")
@ConditionalOnProperty(prefix = "call.statistics.mysql", name = "active", havingValue = "true")
@EnableJpaRepositories(
        entityManagerFactoryRef = "callEntityManagerFactory",
        transactionManagerRef = "callTransactionManager",
        //设置Repository所在位置
        basePackages = "org.kaws.dao")
@EnableTransactionManagement
public class JPAConfiguration {

    @Autowired
    private JpaProperties jpaProperties;

    @Autowired
    private HibernateProperties hibernateProperties;

    @Bean("callDataSource")
    @Qualifier("callDataSource")
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

    @Bean(name = "callEntityManager")
    public EntityManager callEntityManager(EntityManagerFactoryBuilder builder, MySQLConfigurationProperties dbConfig) {
        return callEntityManagerFactory(builder, dbConfig).getObject().createEntityManager();
    }

    @Bean(name = "callEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean callEntityManagerFactory(EntityManagerFactoryBuilder builder, MySQLConfigurationProperties dbConfig) {
        Map<String, Object> properties = hibernateProperties.determineHibernateProperties(jpaProperties.getProperties(), new HibernateSettings());
        return builder.dataSource(callDataSource(dbConfig))
                .properties(properties)
                //设置实体类所在位置
                .packages("org.kaws.entity")
                .persistenceUnit("callPersistenceUnit")
                .build();
    }

    @Bean(name = "callTransactionManager")
    PlatformTransactionManager callTransactionManager(EntityManagerFactoryBuilder builder, MySQLConfigurationProperties dbConfig) {
        return new JpaTransactionManager(callEntityManagerFactory(builder, dbConfig).getObject());
    }

}
