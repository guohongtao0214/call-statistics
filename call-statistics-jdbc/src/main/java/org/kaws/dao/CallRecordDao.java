package org.kaws.dao;

import org.kaws.entity.MySQLCallRecord;

import java.util.List;

/**
 * @Author: Heiky
 * @Date: 2020/6/8 19:12
 * @Description:
 * @Modified By:
 */
public interface CallRecordDao {

    Integer saveCallRecords(List<MySQLCallRecord> mySQLCallRecords);

}
