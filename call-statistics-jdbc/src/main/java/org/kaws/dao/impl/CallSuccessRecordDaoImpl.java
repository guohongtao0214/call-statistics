package org.kaws.dao.impl;

import org.kaws.dao.CallSuccessRecordDao;
import org.kaws.entity.MySQLCallSuccessRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * @Author: Heiky
 * @Date: 2020/7/8 15:31
 * @Description:
 */

@Repository
@ConditionalOnProperty(prefix = "call.statistics.mysql", name = "active", havingValue = "true")
public class CallSuccessRecordDaoImpl implements CallSuccessRecordDao {


    @Autowired
    @Qualifier("callJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public Integer saveCallSuccessRecords(List<MySQLCallSuccessRecord> mySQLCallSuccessRecords) {
        String sql = "insert into call_success_record(app_id,access_ip,ability_uri,params,create_time) values(?,?,?,?,?)";
        int[] rows = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, mySQLCallSuccessRecords.get(i).getAppId());
                ps.setString(2, mySQLCallSuccessRecords.get(i).getAccessIp());
                ps.setString(3, mySQLCallSuccessRecords.get(i).getAbilityUri());
                ps.setString(4, mySQLCallSuccessRecords.get(i).getParams());
                ps.setObject(5, mySQLCallSuccessRecords.get(i).getCreateTime());
            }

            @Override
            public int getBatchSize() {
                return mySQLCallSuccessRecords.size();
            }
        });
        return rows.length;
    }

}
