package org.kaws.autoconfigure;

import com.google.common.collect.Lists;
import org.kaws.biz.MySQLCallRecordBiz;
import org.kaws.config.JPAConfiguration;
import org.kaws.config.MySQLConfigurationProperties;
import org.kaws.dao.CallRecordRepository;
import org.kaws.dao.CallSuccessRecordRepository;
import org.kaws.entity.MySQLCallRecord;
import org.kaws.entity.MySQLCallSuccessRecord;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * @Author: Heiky
 * @Date: 2020/6/20 19:16
 * @Description:
 */

@Configuration
@EnableConfigurationProperties(MySQLConfigurationProperties.class)
@ConditionalOnProperty(prefix = "call.statistics.mysql", name = "active", havingValue = "true")
@Import(JPAConfiguration.class)
public class MySQLCallStatisticsAutoConfiguration implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    private Lock lock;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Bean
    public MySQLCallRecordBiz mySQLCallRecordBiz(CallRecordRepository callRecordRepository,
                                                 CallSuccessRecordRepository callSuccessRecordRepository) {
        MySQLCallRecordBiz mySQLCallRecordBiz = new MySQLCallRecordBiz();
        mySQLCallRecordBiz.setCallRecordRepository(callRecordRepository);
        mySQLCallRecordBiz.setCallSuccessRecordRepository(callSuccessRecordRepository);
        return mySQLCallRecordBiz;
    }


    @Bean
    public List<MySQLCallRecord> mySQLCallRecords() {
        return new ArrayList<>();
    }

    @Bean
    public List<MySQLCallSuccessRecord> mySQLCallSuccessRecords() {
        return new ArrayList<>();
    }


    @PostConstruct
    public void scheduleWithFixedRate() {
        List<MySQLCallRecord> mySQLCallRecords =
                (List<MySQLCallRecord>) applicationContext.getBean("mySQLCallRecords");
        List<MySQLCallSuccessRecord> mySQLCallSuccessRecords =
                (List<MySQLCallSuccessRecord>) applicationContext.getBean("mySQLCallSuccessRecords");

        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = (ScheduledThreadPoolExecutor) applicationContext.getBean("customScheduledThreadPool");

        scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
            if (!CollectionUtils.isEmpty(mySQLCallRecords)) {
                MySQLCallRecordBiz mySQLCallRecordBiz = applicationContext.getBean(MySQLCallRecordBiz.class);
                lock.lock();
                ArrayList<MySQLCallRecord> savingCallRecords = Lists.newArrayList(mySQLCallRecords);
                mySQLCallRecords.clear();
                lock.unlock();
                mySQLCallRecordBiz.saveCallRecords(savingCallRecords);
            }
            if (!CollectionUtils.isEmpty(mySQLCallSuccessRecords)) {
                MySQLCallRecordBiz mySQLCallRecordBiz = applicationContext.getBean(MySQLCallRecordBiz.class);
                lock.lock();
                ArrayList<MySQLCallSuccessRecord> savingCallRecords = Lists.newArrayList(mySQLCallSuccessRecords);
                mySQLCallSuccessRecords.clear();
                lock.unlock();
                mySQLCallRecordBiz.saveCallSuccessRecords(savingCallRecords);
            }
        }, 60, 10, TimeUnit.SECONDS);

    }


}
