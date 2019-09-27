package com.ben.mysql.jdbctemplate.utils;

import com.ben.mysql.jdbctemplate.utils.entity.SysLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * — <br>
 *
 * @author: Benjamin <br>
 * @date: 2019/9/11 <br>
 */
@Slf4j
public class SqlUtilTest extends TestBase {

    @Autowired
    JdbcTemplate jdbcTemplate;


    private String getTableName() {
        return "sys_log";
    }

    private RowMapper<Pair<String, Integer>> groupByRowMapper = new RowMapper<Pair<String, Integer>>() {
        @Override
        public Pair<String, Integer> mapRow(ResultSet rs, int i) throws SQLException {
            String userName = rs.getString("username");
            int count = rs.getInt("cnt");
            return Pair.of(userName, count);
        }
    };

    @Test
    public void test_selectWithoutWhere() {
        StringBuilder sql = SqlUtil.selectWithoutWhere(getTableName(), "id", "user_id", "username", "operation", "time", "method");

        Pair<StringBuilder, List<Object>> pair = new SqlWhereGroupByOrderByLimitBuilder(sql).
                andGreaterThan("id", 1)
                .andCustomized("username=? or username=?", "test", "test1")
                .build();

        List<SysLog> res = jdbcTemplate.query(pair.getLeft().toString(), pair.getRight().toArray(), sysLogRowMapper);

        log.info("{}", res.size());
    }

    @Test
    public void test_selectWithKey() {
        Pair<StringBuilder, List<Object>> pair = SqlUtil.selectWithSingleWhere(getTableName(), "id", 1, "id", "user_id", "username", "operation", "time", "method");
        if (pair != null) {
            List<SysLog> res = jdbcTemplate.query(pair.getLeft().toString(), pair.getRight().toArray(), sysLogRowMapper);

            log.info("{}", res.size());
        }
    }

    @Test
    public void test_selectWithWhereNoParams() {
        StringBuilder sql = SqlUtil.selectWithWhereNoParams(getTableName(), "disabled=0", "id", "user_id", "username", "operation", "time", "method");
        List<SysLog> res = jdbcTemplate.query(sql.toString(), sysLogRowMapper);

        log.info("{}", res.size());
    }

    @Test
    public void test_selectWithWhereNoParams_externalParams() {
        StringBuilder sql = SqlUtil.selectWithWhereNoParams(getTableName(), "id=?", "id", "user_id", "username", "operation", "time", "method");

        List<SysLog> res = jdbcTemplate.query(sql.toString(), new Object[]{1}, sysLogRowMapper);
        log.info("{}", res.size());
    }

    @Test
    public void insertSelective() {

        Pair<StringBuilder, List<Object>> pair = SqlUtil.insertSelective(getTableName(), null,
                Pair.of("user_id", null),
                Pair.of("username", "test1"),
                Pair.of("operation", "测试"),
                Pair.of("time", 0),
                Pair.of("method", "com.test.test()"));

        if (pair != null) {
            int res = jdbcTemplate.update(pair.getLeft().toString(), pair.getRight().toArray());

            log.info("{}", res);
        }
    }

    @Test
    public void updateSelectiveWithKey() {

        Pair<StringBuilder, List<Object>> pair = SqlUtil.updateSelectiveWithKey(getTableName(), "id", 2814,
                Pair.of("user_id", 1));

        if (pair != null) {
            int res = jdbcTemplate.update(pair.getLeft().toString(), pair.getRight().toArray());

            log.info("{}", res);
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
    public void testGroupBy() {
        StringBuilder sql = SqlUtil.selectWithoutWhere(getTableName(), "username,count(*) cnt");
        Pair<StringBuilder, List<Object>> pair = new SqlWhereGroupByOrderByLimitBuilder(sql)
                .andGreaterThan("id", 20)
                .groupBy("username", "cnt > 1")
                .orderBy("cnt desc")
//                .limit(1, 2)
                .limit(2)
                .build();

        if (pair != null) {
            List<Pair<String, Integer>> res = jdbcTemplate.query(pair.getLeft().toString(), pair.getRight().toArray(), groupByRowMapper);

            log.info("asd");
        }
    }


}