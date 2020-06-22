package org.kaws.biz;

/**
 * @Author: Heiky
 * @Date: 2020/6/20 20:51
 * @Description:
 */
public interface CallRecordBiz {

    void saveCallRecord(String appId, String abilityUri);

    void saveCallSuccessRecord(String appId, String abilityUri, String params);

}
