package org.kaws.biz;

import lombok.Setter;
import org.kaws.dao.CallRecordDao;
import org.kaws.dao.CallSuccessRecordDao;
import org.kaws.entity.MySQLCallRecord;
import org.kaws.entity.MySQLCallSuccessRecord;

import java.util.List;

/**
 * @Author: Heiky
 * @Date: 2020/6/8 11:48
 * @Description:
 * @Modified By:
 */

@Setter
public class MySQLCallRecordBiz {

    private CallRecordDao callRecordDao;

    private CallSuccessRecordDao callSuccessRecordDao;

    public void saveCallRecords(List<MySQLCallRecord> mySQLCallRecords) {
        callRecordDao.saveCallRecords(mySQLCallRecords);
    }

    public void saveCallSuccessRecords(List<MySQLCallSuccessRecord> mySQLCallSuccessRecords) {
        callSuccessRecordDao.saveCallSuccessRecords(mySQLCallSuccessRecords);
    }

}
