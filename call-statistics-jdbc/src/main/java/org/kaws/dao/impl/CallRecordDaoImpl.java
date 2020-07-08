package org.kaws.dao.impl;

import org.kaws.dao.CallRecordDao;
import org.kaws.entity.MySQLCallRecord;
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
public class CallRecordDaoImpl implements CallRecordDao {

    @Autowired
    @Qualifier("callJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public Integer saveCallRecords(List<MySQLCallRecord> mySQLCallRecords) {
        String sql = "insert into call_record(app_id,access_ip,ability_uri,create_time) values(?,?,?,?)";
        int[] rows = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, mySQLCallRecords.get(i).getAppId());
                ps.setString(2, mySQLCallRecords.get(i).getAccessIp());
                ps.setString(3, mySQLCallRecords.get(i).getAbilityUri());
                ps.setObject(4, mySQLCallRecords.get(i).getCreateTime());
            }

            @Override
            public int getBatchSize() {
                return mySQLCallRecords.size();
            }
        });
        return rows.length;
    }


}
