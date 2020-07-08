package org.kaws.biz;

import lombok.Setter;
import org.kaws.dao.CallRecordRepository;
import org.kaws.dao.CallSuccessRecordRepository;
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

    private CallRecordRepository callRecordRepository;

    private CallSuccessRecordRepository callSuccessRecordRepository;

    public void saveCallRecords(List<MySQLCallRecord> mySQLCallRecords) {
        callRecordRepository.saveAll(mySQLCallRecords);
    }

    public void saveCallSuccessRecords(List<MySQLCallSuccessRecord> mySQLCallSuccessRecords) {
        callSuccessRecordRepository.saveAll(mySQLCallSuccessRecords);
    }

}
