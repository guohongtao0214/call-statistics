package org.kaws.dao;

import org.kaws.entity.MySQLCallSuccessRecord;

import java.util.List;

/**
 * @Author: Heiky
 * @Date: 2020/6/8 19:12
 * @Description:
 * @Modified By:
 */
public interface CallSuccessRecordDao {

    Integer saveCallSuccessRecords(List<MySQLCallSuccessRecord> mySQLCallSuccessRecords);

}
