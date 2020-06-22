package org.kaws.autoconfigure;

import org.kaws.biz.MySQLCallRecordBiz;
import org.kaws.config.JPAConfiguration;
import org.kaws.config.MySQLConfigurationProperties;
import org.kaws.dao.CallRecordRepository;
import org.kaws.dao.CallSuccessRecordRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Author: Heiky
 * @Date: 2020/6/20 19:16
 * @Description:
 */

@Configuration
@EnableConfigurationProperties(MySQLConfigurationProperties.class)
@ConditionalOnProperty(prefix = "call.statistics.mysql", name = "active", havingValue = "true")
@Import(JPAConfiguration.class)
public class MySQLCallStatisticsAutoConfiguration {

    @Bean
    public MySQLCallRecordBiz mySQLCallRecordBiz(CallRecordRepository callRecordRepository,
                                                 CallSuccessRecordRepository callSuccessRecordRepository) {
        MySQLCallRecordBiz mySQLCallRecordBiz = new MySQLCallRecordBiz();
        mySQLCallRecordBiz.setCallRecordRepository(callRecordRepository);
        mySQLCallRecordBiz.setCallSuccessRecordRepository(callSuccessRecordRepository);
        return mySQLCallRecordBiz;
    }

}
