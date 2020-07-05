package org.kaws.autoconfigure;


import com.google.common.collect.Lists;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import org.kaws.biz.MongoCallRecordBiz;
import org.kaws.config.MongoConfiguration;
import org.kaws.config.MongoConfigurationProperties;
import org.kaws.entity.MongoCallRecord;
import org.kaws.entity.MongoCallSuccessRecord;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * @Author: Heiky
 * @Date: 2020/6/20 21:57
 * @Description:
 */

@Configuration
@EnableConfigurationProperties(MongoConfigurationProperties.class)
@ConditionalOnProperty(prefix = "call.statistics.mongo", name = "active", havingValue = "true")
@Import(MongoConfiguration.class)
public class MongoCallStatisticsAutoConfiguration implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    private Lock lock;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    // 覆盖默认的MongoDbFactory
    @Bean
    public MongoDbFactory mongoDbFactory(MongoConfigurationProperties mongoConfigurationProperties) {
        //客户端配置（连接数）
        MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
        builder.connectionsPerHost(mongoConfigurationProperties.getMaxConnectionsPerHost());
        builder.minConnectionsPerHost(mongoConfigurationProperties.getMinConnectionsPerHost());
        MongoDbFactory mongoDbFactory = null;
        if (mongoConfigurationProperties.getUri() != null) {
            mongoDbFactory = new SimpleMongoDbFactory(new MongoClientURI(mongoConfigurationProperties.getUri(), builder));
        }
        return mongoDbFactory;
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory) {
        return new MongoTemplate(mongoDbFactory);
    }

    @Bean
    public MongoCallRecordBiz mongoCallRecordBiz(MongoTemplate mongoTemplate) {
        MongoCallRecordBiz mongoCallRecordBiz = new MongoCallRecordBiz();
        mongoCallRecordBiz.setMongoTemplate(mongoTemplate);
        return mongoCallRecordBiz;
    }

    @Bean
    public List<MongoCallRecord> mongoCallRecords() {
        return new ArrayList<>();
    }

    @Bean
    public List<MongoCallSuccessRecord> mongoCallSuccessRecords() {
        return new ArrayList<>();
    }


    @PostConstruct
    public void scheduleWithFixedRate() {
        List<MongoCallRecord> mongoCallRecords =
                (List<MongoCallRecord>) applicationContext.getBean("mongoCallRecords");
        List<MongoCallSuccessRecord> mongoCallSuccessRecords =
                (List<MongoCallSuccessRecord>) applicationContext.getBean("mongoCallSuccessRecords");

        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = (ScheduledThreadPoolExecutor) applicationContext.getBean("customScheduledThreadPool");

        scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
            if (!CollectionUtils.isEmpty(mongoCallRecords)) {
                MongoCallRecordBiz mongoCallRecordBiz = applicationContext.getBean(MongoCallRecordBiz.class);
                lock.lock();
                ArrayList<MongoCallRecord> savingCallRecords = Lists.newArrayList(mongoCallRecords);
                mongoCallRecords.clear();
                lock.unlock();
                mongoCallRecordBiz.saveCallRecords(savingCallRecords);
            }
            if (!CollectionUtils.isEmpty(mongoCallSuccessRecords)) {
                MongoCallRecordBiz mongoCallRecordBiz = applicationContext.getBean(MongoCallRecordBiz.class);
                lock.lock();
                ArrayList<MongoCallSuccessRecord> savingCallRecords = Lists.newArrayList(mongoCallSuccessRecords);
                mongoCallSuccessRecords.clear();
                lock.unlock();
                mongoCallRecordBiz.saveCallSuccessRecords(savingCallRecords);
            }
        }, 60, 10, TimeUnit.SECONDS);

    }

}
