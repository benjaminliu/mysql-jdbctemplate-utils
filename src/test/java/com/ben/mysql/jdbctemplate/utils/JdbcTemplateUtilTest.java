package com.ben.mysql.jdbctemplate.utils;

import com.ben.mysql.jdbctemplate.utils.entity.SysLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * — <br>
 *
 * @author: 刘恒 <br>
 * @date: 2019/9/17 <br>
 */
@Slf4j
public class JdbcTemplateUtilTest extends TestBase {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private String getTableName() {
        return "sys_log";
    }

    @Test
    public void test_insertAndReturnId() {


        Pair<StringBuilder, List<Object>> pair = SqlUtil.insertSelective(getTableName(), null,
                Pair.of("user_id", null),
                Pair.of("username", "test1"),
                Pair.of("operation", "测试"),
                Pair.of("time", 0),
                Pair.of("method", "com.test.test()"));


        KeyHolder keyHolder1 = new GeneratedKeyHolder();

        int res = JdbcTemplateUtil.insertAndReturnId(jdbcTemplate, pair.getLeft().toString(), pair.getRight().toArray(), keyHolder1);

        if (res > 0) {
            if (keyHolder1.getKey() != null) {
                log.info("id: {}", keyHolder1.getKey().intValue());
            }
        }
    }

    private RowMapper<SysLog> sysLogRowMapper = new RowMapper<SysLog>() {
        @Override
        public SysLog mapRow(ResultSet rs, int i) throws SQLException {
            SysLog sysLog = new SysLog();
            sysLog.setId(rs.getInt("id"));
            sysLog.setUserId(rs.getInt("user_id"));
            sysLog.setUserName(rs.getString("username"));
            sysLog.setOperation(rs.getString("operation"));
            sysLog.setTime(rs.getInt("time"));
            sysLog.setMethod(rs.getString("method"));
            return sysLog;
        }
    };

    @Test
    public void test_queryWithPagination() {
        StringBuilder sql = SqlUtil.selectWithoutWhere(getTableName(), "id", "user_id", "username", "operation", "time", "method");

        Pair<List<SysLog>, Integer> res = JdbcTemplateUtil.queryWithPagination(jdbcTemplate, sql.toString(), null, sysLogRowMapper, 2, 10);

        log.info("total count: {}", res.getRight());
    }

    @Test
    public void test_batchInsert() {

        SysLog s1 = new SysLog();
        s1.setUserId(1);
        s1.setTime(10);
        s1.setMethod("insert()");
        s1.setOperation("add");
        s1.setUserName("test1");

        SysLog s2 = new SysLog();
        s2.setUserId(2);
        s2.setTime(20);
        s2.setMethod("update()");
        s2.setOperation("update");
        s1.setUserName("test2");

        List<SysLog> list = new ArrayList<>();
        list.add(s1);
        list.add(s2);


        int[] res = JdbcTemplateUtil.batchInsert(jdbcTemplate, getTableName(), list, null,
                Pair.of("user_id", (item) -> item.getUserId()),
                Pair.of("operation", (item) -> item.getOperation()),
                Pair.of("time", (item) -> item.getTime()),
                Pair.of("method", (item) -> item.getMethod()),
                Pair.of("username", (item) -> item.getUserName()));

        log.info("asd");
    }
}
