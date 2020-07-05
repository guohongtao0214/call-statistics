package org.kaws.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author Heiky
 * @since 2020-06-08
 */
@Getter
@Setter
@Entity(name = "call_success_record")
public class MySQLCallSuccessRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户标识
     */
    @Column(nullable = false)
    private String appId;

    /**
     * 调用地址
     */
    @Column(nullable = false)
    private String abilityUri;

    /**
     * 请求参数
     */
    @Column
    private String params;

    /**
     * 请求时间
     */
    @Column(nullable = false)
    private LocalDateTime createTime;

    public MySQLCallSuccessRecord() {
    }

    public MySQLCallSuccessRecord(String appId, String abilityUri, String params, LocalDateTime createTime) {
        this.appId = appId;
        this.abilityUri = abilityUri;
        this.params = params;
        this.createTime = createTime;
    }
}
