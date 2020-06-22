package org.kaws.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author Heiky
 * @since 2020-06-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
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


}
