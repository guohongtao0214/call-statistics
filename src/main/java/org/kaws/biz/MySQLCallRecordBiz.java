package org.kaws.biz;

import lombok.Setter;
import org.kaws.dao.CallRecordRepository;
import org.kaws.dao.CallSuccessRecordRepository;
import org.kaws.entity.MySQLCallRecord;
import org.kaws.entity.MySQLCallSuccessRecord;

import java.time.LocalDateTime;

/**
 * @Author: Heiky
 * @Date: 2020/6/8 11:48
 * @Description:
 * @Modified By:
 */

@Setter
public class MySQLCallRecordBiz implements CallRecordBiz {

    private CallRecordRepository callRecordRepository;

    private CallSuccessRecordRepository callSuccessRecordRepository;

    @Override
    public void saveCallRecord(String appId, String abilityUri){
        MySQLCallRecord callRecord = new MySQLCallRecord();
        callRecord.setAppId(appId);
        callRecord.setAbilityUri(abilityUri);
        callRecord.setCreateTime(LocalDateTime.now());
        callRecordRepository.save(callRecord);
    }

    @Override
    public void saveCallSuccessRecord(String appId, String abilityUri, String params){
        MySQLCallSuccessRecord callSuccessRecord = new MySQLCallSuccessRecord();
        callSuccessRecord.setAppId(appId);
        callSuccessRecord.setAbilityUri(abilityUri);
        callSuccessRecord.setParams(params);
        callSuccessRecord.setCreateTime(LocalDateTime.now());
        callSuccessRecordRepository.save(callSuccessRecord);
    }

}
