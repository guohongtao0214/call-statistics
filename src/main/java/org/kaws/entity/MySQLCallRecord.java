package org.kaws.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Heiky
 * @since 2020-06-08
 */
@Getter
@Setter
@Entity(name = "call_record")
public class MySQLCallRecord implements Serializable {

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
     * 访问ip
     */
    @Column(nullable = false)
    private String accessIp;

    /**
     * 调用地址
     */
    @Column(nullable = false)
    private String abilityUri;

    /**
     * 请求时间
     */
    @Column(nullable = false)
    private LocalDateTime createTime;

    public MySQLCallRecord() {
    }

    public MySQLCallRecord(String appId, String accessIp, String abilityUri, LocalDateTime createTime) {
        this.appId = appId;
        this.accessIp = accessIp;
        this.abilityUri = abilityUri;
        this.createTime = createTime;
    }
}
