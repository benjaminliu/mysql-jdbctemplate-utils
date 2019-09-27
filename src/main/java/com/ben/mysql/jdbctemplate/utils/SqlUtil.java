package com.ben.mysql.jdbctemplate.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * 拼接sql语句的util <br>
 *
 * @author: Benjamin <br>
 * @date: 2019/8/12 <br>
 */
public abstract class SqlUtil {

    /**
     * 生成select语句，没有添加where，columns  可以为 一个 *
     **/
    public static StringBuilder selectWithoutWhere(String tableName, String... columns) {
        if (StringUtils.isBlank(tableName))
            return null;

        if (columns == null || columns.length == 0)
            return null;

        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        sql.append(columns[0]);
        for (int i = 1; i < columns.length; i++) {
            sql.append(",");
            sql.append(columns[i]);
        }

        sql.append(" from ");
        sql.append(tableName);

        return sql;
    }

    /**
     * 生成select语句，其中包含一个where条件，就是 key=value
     **/
    public static Pair<StringBuilder, List<Object>> selectWithSingleWhere(String table, String key, Object value, String... columns) {
        if (StringUtils.isBlank(key))
            return null;

        if (value == null)
            return null;

        StringBuilder sql = selectWithoutWhere(table, columns);
        if (sql == null)
            return null;

        sql.append(" where ");
        sql.append(key);
        sql.append("=?");
        List<Object> args = new ArrayList<>();
        args.add(value);

        return Pair.of(sql, args);
    }

    /**
     * 生成select语句，自动添加where关键字， 并添加用户自己定制的where语句（不含关键字）， where语句中可以包含 ？ 但是用户需要自己在本方法外提供相应的参数值
     **/
    public static StringBuilder selectWithWhereNoParams(String table, String whereWithoutKeyword, String... columns) {
        StringBuilder sql = selectWithoutWhere(table, columns);
        if (sql == null)
            return null;

        if (StringUtils.isBlank(whereWithoutKeyword))
            return sql;

        sql.append(" where ");
        sql.append(whereWithoutKeyword);
        return sql;
    }

    /**
     * 根据传入的值是否为空来拼接insert语句，并添加相应的参数.
     * onDuplicateKeyUpdate 可空，如果不为空，就是  ON DUPLICATE KEY UPDATE 关键字后面的需要的操作，如 statics=VALUES(statics),update_time=current_timestamp
     **/
    public static Pair<StringBuilder, List<Object>> insertSelective(String tableName, String onDuplicateKeyUpdate, Pair<String, Object>... fieldPair) {
        if (StringUtils.isBlank(tableName))
            return null;

        if (fieldPair == null || fieldPair.length == 0)
            return null;

        StringBuilder sql = new StringBuilder();
        sql.append("insert into ");
        sql.append(tableName);
        sql.append("(");
        StringBuilder values = new StringBuilder();
        values.append(" values (");

        List<Object> args = new ArrayList<>();

        boolean hasField = false;
        for (Pair<String, Object> pair : fieldPair) {
            if (StringUtils.isBlank(pair.getLeft()))
                continue;
            if (pair.getRight() == null)
                continue;

            hasField = true;
            sql.append(pair.getLeft());
            sql.append(",");

            values.append("?,");
            args.add(pair.getRight());
        }
        if (hasField == false)
            return null;

        //移除最后一个逗号
        sql.setLength(sql.length() - 1);
        values.setLength(values.length() - 1);

        sql.append(")");
        sql.append(values);
        sql.append(")");

        if (StringUtils.isNotBlank(onDuplicateKeyUpdate)) {
            sql.append(" ON DUPLICATE KEY UPDATE ");
            sql.append(onDuplicateKeyUpdate);
        }

        return Pair.of(sql, args);
    }

    /**
     * 根据传入的值是否为空来拼接update语句的set部分，并添加相应的参数, where语句只有1个条件，就是指定的key，如果指定的key为空或者null，返回null
     **/
    public static Pair<StringBuilder, List<Object>> updateSelectiveWithKey(String tableName, String keyName, Object keyValue, Pair<String, Object>... fieldPair) {
        if (StringUtils.isBlank(tableName))
            return null;
        if (StringUtils.isBlank(keyName))
            return null;
        if (keyValue == null)
            return null;

        Pair<StringBuilder, List<Object>> pair = updateSelectiveWithoutWhere(tableName, fieldPair);
        if (pair == null)
            return null;

        addFirstWhereEqualTo(pair, keyName, keyValue);

        return pair;
    }

    /**
     * 根据传入的值是否为空来拼接update语句的set部分，并添加相应的参数，不添加where语句
     **/
    public static Pair<StringBuilder, List<Object>> updateSelectiveWithoutWhere(String tableName, Pair<String, Object>... fieldPair) {
        if (StringUtils.isBlank(tableName))
            return null;

        if (fieldPair == null || fieldPair.length == 0)
            return null;

        StringBuilder sb = new StringBuilder();
        sb.append("update ");
        sb.append(tableName);
        sb.append(" set ");

        List<Object> args = new ArrayList<>();

        boolean hasField = false;
        for (Pair<String, Object> pair : fieldPair) {
            if (StringUtils.isBlank(pair.getLeft()))
                continue;
            if (pair.getRight() == null)
                continue;

            hasField = true;
            sb.append(pair.getLeft());
            sb.append("=?,");

            args.add(pair.getRight());
        }
        if (hasField == false)
            return null;

        //移除最后一个逗号
        sb.setLength(sb.length() - 1);
        return Pair.of(sb, args);
    }

    private static void addFirstWhereEqualTo(Pair<StringBuilder, List<Object>> pair, String column, Object value) {
        if (StringUtils.isBlank(column))
            return;

        if (value == null)
            return;

        StringBuilder sql = pair.getLeft();
        List<Object> args = pair.getRight();

        sql.append(" where ");

        sql.append(column);
        sql.append("=?");
        args.add(value);
    }
}
