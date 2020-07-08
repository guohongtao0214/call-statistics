package org.kaws.biz;

import lombok.Setter;
import org.kaws.entity.MongoCallRecord;
import org.kaws.entity.MongoCallSuccessRecord;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

/**
 * @Author: Heiky
 * @Date: 2020/6/20 20:58
 * @Description:
 */

@Setter
public class MongoCallRecordBiz {

    private MongoTemplate mongoTemplate;

    public void saveCallRecords(List<MongoCallRecord> mongoCallRecords) {
        mongoTemplate.insertAll(mongoCallRecords);
    }

    public void saveCallSuccessRecords(List<MongoCallSuccessRecord> mongoCallSuccessRecords) {
        mongoTemplate.insertAll(mongoCallSuccessRecords);
    }
}
