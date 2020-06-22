package org.kaws.autoconfigure;


import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import org.kaws.biz.MongoCallRecordBiz;
import org.kaws.config.MongoConfiguration;
import org.kaws.config.MongoConfigurationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

/**
 * @Author: Heiky
 * @Date: 2020/6/20 21:57
 * @Description:
 */

@Configuration
@EnableConfigurationProperties(MongoConfigurationProperties.class)
@ConditionalOnProperty(prefix = "call.statistics.mongo", name = "active", havingValue = "true")
@Import(MongoConfiguration.class)
public class MongoCallStatisticsAutoConfiguration {

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

}
