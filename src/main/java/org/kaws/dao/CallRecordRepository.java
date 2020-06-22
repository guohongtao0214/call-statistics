package org.kaws.dao;

import org.kaws.entity.MySQLCallRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: Heiky
 * @Date: 2020/6/8 19:12
 * @Description:
 * @Modified By:
 */
public interface CallRecordRepository extends JpaRepository<MySQLCallRecord, Long> {
}
