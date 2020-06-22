package org.kaws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,
        MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class CallStatisticsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CallStatisticsApplication.class, args);
    }

}
