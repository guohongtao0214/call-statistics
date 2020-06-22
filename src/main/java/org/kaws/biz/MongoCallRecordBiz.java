package org.kaws.biz;

import lombok.Setter;
import org.kaws.entity.MongoCallRecord;
import org.kaws.entity.MongoCallSuccessRecord;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @Author: Heiky
 * @Date: 2020/6/20 20:58
 * @Description:
 */

@Setter
public class MongoCallRecordBiz implements CallRecordBiz {

    private MongoTemplate mongoTemplate;

    @Override
    public void saveCallRecord(String appId, String abilityUri) {
        MongoCallRecord mongoCallRecord = new MongoCallRecord(appId, abilityUri, LocalDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Shanghai")));
        mongoTemplate.save(mongoCallRecord);
    }

    @Override
    public void saveCallSuccessRecord(String appId, String abilityUri, String params) {
        MongoCallSuccessRecord mongoCallSuccessRecord = new MongoCallSuccessRecord(appId, abilityUri, params, LocalDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Shanghai")));
        mongoTemplate.save(mongoCallSuccessRecord);
    }
}
