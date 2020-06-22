package org.kaws.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * @Author: Heiky
 * @Date: 2020/6/20 21:03
 * @Description:
 */

@Data
@Document("MongoCallRecord")
public class MongoCallRecord {

    /**
     * 用户标识
     */
    private String appId;

    /**
     * 调用地址
     */
    private String abilityUri;

    /**
     * 请求时间
     */
    private LocalDateTime createTime;

    public MongoCallRecord() {
    }

    public MongoCallRecord(String appId, String abilityUri, LocalDateTime createTime) {
        this.appId = appId;
        this.abilityUri = abilityUri;
        this.createTime = createTime;
    }
}
