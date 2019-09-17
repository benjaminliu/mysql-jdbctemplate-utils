package com.ben.mysql.jdbctemplate.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.function.Function;

/**
 * 封装一些比较复杂的jdbctemplate的方法 <br>
 *
 * @author: Benjamin <br>
 * @date: 2019/9/11 <br>
 */
public class JdbcTemplateUtil {

    /**
     * 插入数据，并返回自动生成的id, 如果参数异常，或者有错误，返回-1,否则返回影响的行数
     **/
    public static int insertAndReturnId(JdbcTemplate jdbcTemplate, String sql, Object[] args, KeyHolder keyHolder) {
        if (jdbcTemplate == null)
            return -1;

        if (StringUtils.isBlank(sql))
            return -1;

        if (args == null || args.length == 0)
            return -1;

        if (keyHolder == null)
            return -1;

        int res = jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
//                PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int size = args.length;

                for (int i = 1; i <= size; i++) {
                    ps.setObject(i, args[i - 1]);
                }

                return ps;
            }
        }, keyHolder);

        return res;
    }

    /**
     * 给sql添加分页功能,
     * pageNo是页数，以1开始，如第一页返回0-pageSize的内容，
     * pageSize最小是1， 如果小于1，就设置为1,
     * 返回值中， left是数据列表(可为null)， right是totalCount
     **/
    public static <T> Pair<List<T>, Integer> queryWithPagination(JdbcTemplate jdbcTemplate, String sql, Object[] args, RowMapper<T> rowMapper, int pageNo, int pageSize) {
        if (jdbcTemplate == null)
            return null;

        if (StringUtils.isBlank(sql))
            return null;

        if (rowMapper == null)
            return null;

        String countSql = "select count(*) from (" + sql + ") table_total";
        int totalCount = jdbcTemplate.queryForObject(countSql, args, Integer.class);

        //如果没有查到数据，直接返回
        if (totalCount == 0) {
            return Pair.of(null, 0);
        }

        if (pageSize < 1)
            pageSize = 1;

        int offset;
        if (pageNo < 1) {
            offset = 0;
        } else {
            offset = (pageNo - 1) * pageSize;
        }

        String pageSql = "select * from (" + sql + ") table_page limit " + String.valueOf(offset) + "," + String.valueOf(pageSize);

        List<T> res = jdbcTemplate.query(pageSql, args, rowMapper);

        if (res == null || res.size() == 0) {
            return Pair.of(null, totalCount);
        } else {
            return Pair.of(res, totalCount);
        }
    }

    /**
     * 批量插入，
     * tableName为表明，
     * list为要插入的数据
     * onDuplicateKeyUpdate 可空，如果不为空，就是  ON DUPLICATE KEY UPDATE 关键字后面的需要的操作，如 statics=VALUES(statics),update_time=current_timestamp
     * fieldMaps，  左边是 数据库中字段的名字， 右边是一个 方法，接收参数为 list的类型，返回值是要插入的值， 比如要插入 moment的 phoneId字段， 可以为 (m)-> m.getPhoneId()
     **/
    public static <T> int[] batchInsert(JdbcTemplate jdbcTemplate, String tableName, List<T> list, String onDuplicateKeyUpdate, Pair<String, Function<T, Object>>... fieldMaps) {
        if (jdbcTemplate == null)
            return null;

        if (StringUtils.isBlank(tableName))
            return null;

        if (list == null || list.size() == 0)
            return null;

        if (fieldMaps == null || fieldMaps.length == 0)
            return null;

        StringBuilder sql = new StringBuilder();
        sql.append("insert into ");
        sql.append(tableName);
        sql.append("(");
        sql.append(fieldMaps[0].getLeft());

        for (int i = 1; i < fieldMaps.length; i++) {
            sql.append(",");
            sql.append(fieldMaps[i].getLeft());
        }
        sql.append(")values(?");
        for (int i = 1; i < fieldMaps.length; i++) {
            sql.append(",?");
        }
        sql.append(")");

        if (StringUtils.isNotBlank(onDuplicateKeyUpdate)) {
            sql.append(" ON DUPLICATE KEY UPDATE ");
            sql.append(onDuplicateKeyUpdate);
        }

        return jdbcTemplate.batchUpdate(sql.toString(), new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                T item = list.get(i);
                for (int idx = 0; idx < fieldMaps.length; idx++) {
                    ps.setObject(idx + 1, fieldMaps[idx].getRight().apply(item));
                }
            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
    }
}
