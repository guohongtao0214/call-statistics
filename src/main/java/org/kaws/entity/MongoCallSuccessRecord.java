package org.kaws.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * @Author: Heiky
 * @Date: 2020/6/20 21:04
 * @Description:
 */

@Data
@Document("MongoCallSuccessRecord")
public class MongoCallSuccessRecord {


    /**
     * 用户标识
     */
    private String appId;

    /**
     * 调用地址
     */
    private String abilityUri;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 请求时间
     */
    private LocalDateTime createTime;

    public MongoCallSuccessRecord() {
    }

    public MongoCallSuccessRecord(String appId, String abilityUri, String params, LocalDateTime createTime) {
        this.appId = appId;
        this.abilityUri = abilityUri;
        this.params = params;
        this.createTime = createTime;
    }


}
