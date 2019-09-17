package com.ben.mysql.jdbctemplate.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 给sql语句添加where, group by, order by, limit部分, 调用方自己控制这4部分的顺序是否正确， 不能多线程使用,  <br>
 *
 * @author: Benjamin <br>
 * @date: 2019/9/11 <br>
 */
public class SqlWhereGroupByOrderByLimitBuilder {

    private StringBuilder sql;
    private List<Object> args;

    //是否已经添加了where
    private volatile boolean whereAdded = false;

    /**
     * sql是where之前的部分
     **/
    public SqlWhereGroupByOrderByLimitBuilder(StringBuilder sql) {
        this(sql, null);
    }

    /**
     * sql是where之前的部分，
     * args是sql语句的参数，就是？的参数，可以为null（如select语句）
     **/
    public SqlWhereGroupByOrderByLimitBuilder(StringBuilder sql, List<Object> args) {
        if (sql == null)
            throw new IllegalArgumentException("sql不能为空，本类只是补充where部分");

        this.sql = sql;
        if (args != null) {
            this.args = args;
        } else {
            this.args = new ArrayList<>();
        }
    }

    /**
     * 生成最终的sql和参数列表
     **/
    public Pair<StringBuilder, List<Object>> build() {
        return Pair.of(sql, args);
    }


    public SqlWhereGroupByOrderByLimitBuilder andEqualTo(String column, Object value) {
        addEqualTo(column, value, true);
        return this;
    }

    public SqlWhereGroupByOrderByLimitBuilder orEqualTo(String column, Object value) {
        addEqualTo(column, value, false);
        return this;
    }


    public SqlWhereGroupByOrderByLimitBuilder andNotEqualTo(String column, Object value) {
        addNotEqualTo(column, value, true);
        return this;
    }

    public SqlWhereGroupByOrderByLimitBuilder orNotEqualTo(String column, Object value) {
        addNotEqualTo(column, value, false);
        return this;
    }


    public SqlWhereGroupByOrderByLimitBuilder andGreaterThan(String column, Object value) {
        addGreaterThan(column, value, true);
        return this;
    }

    public SqlWhereGroupByOrderByLimitBuilder orGreaterThan(String column, Object value) {
        addGreaterThan(column, value, false);
        return this;
    }


    public SqlWhereGroupByOrderByLimitBuilder andGreaterThanOrEqualTo(String column, Object value) {
        addGreaterThanOrEqualTo(column, value, true);
        return this;
    }

    public SqlWhereGroupByOrderByLimitBuilder orGreaterThanOrEqualTo(String column, Object value) {
        addGreaterThanOrEqualTo(column, value, false);
        return this;
    }


    public SqlWhereGroupByOrderByLimitBuilder andSmallerThan(String column, Object value) {
        addSmallerThan(column, value, true);
        return this;
    }

    public SqlWhereGroupByOrderByLimitBuilder orSmallerThan(String column, Object value) {
        addSmallerThan(column, value, false);
        return this;
    }


    public SqlWhereGroupByOrderByLimitBuilder andSmallerThanOrEqualTo(String column, Object value) {
        addSmallerThanOrEqualTo(column, value, true);
        return this;
    }

    public SqlWhereGroupByOrderByLimitBuilder orSmallerThanOrEqualTo(String column, Object value) {
        addSmallerThanOrEqualTo(column, value, false);
        return this;
    }


    /**
     * like 前后都加%
     **/
    public SqlWhereGroupByOrderByLimitBuilder andContains(String column, Object value) {
        addContains(column, value, true);
        return this;
    }

    /**
     * like 前后都加%
     **/
    public SqlWhereGroupByOrderByLimitBuilder orContains(String column, Object value) {
        addContains(column, value, false);
        return this;
    }


    /**
     * not like 前后都加%
     **/
    public SqlWhereGroupByOrderByLimitBuilder andNotContains(String column, Object value) {
        addNotContains(column, value, true);
        return this;
    }

    /**
     * not like 前后都加%
     **/
    public SqlWhereGroupByOrderByLimitBuilder orNotContains(String column, Object value) {
        addNotContains(column, value, false);
        return this;
    }


    /**
     * like 前加%
     **/
    public SqlWhereGroupByOrderByLimitBuilder andStartWith(String column, Object value) {
        addStartWith(column, value, true);
        return this;
    }

    /**
     * like 前加%
     **/
    public SqlWhereGroupByOrderByLimitBuilder orStartWith(String column, Object value) {
        addStartWith(column, value, false);
        return this;
    }


    /**
     * not like 前加%
     **/
    public SqlWhereGroupByOrderByLimitBuilder andNotStartWith(String column, Object value) {
        addNotStartWith(column, value, true);
        return this;
    }

    /**
     * not like 前加%
     **/
    public SqlWhereGroupByOrderByLimitBuilder orNotStartWith(String column, Object value) {
        addNotStartWith(column, value, false);
        return this;
    }


    /**
     * like 后加%
     **/
    public SqlWhereGroupByOrderByLimitBuilder andEndWith(String column, Object value) {
        addEndWith(column, value, true);
        return this;
    }

    /**
     * like 后加%
     **/
    public SqlWhereGroupByOrderByLimitBuilder orEndWith(String column, Object value) {
        addEndWith(column, value, false);
        return this;
    }


    /**
     * not like 后加%
     **/
    public SqlWhereGroupByOrderByLimitBuilder andNotEndWith(String column, Object value) {
        addNotEndWith(column, value, true);
        return this;
    }

    /**
     * not like 后加%
     **/
    public SqlWhereGroupByOrderByLimitBuilder orNotEndWith(String column, Object value) {
        addNotEndWith(column, value, false);
        return this;
    }


    public SqlWhereGroupByOrderByLimitBuilder andBetween(String column, Object start, Object end) {
        addBetween(column, start, end, true);
        return this;
    }

    public SqlWhereGroupByOrderByLimitBuilder orBetween(String column, Object start, Object end) {
        addBetween(column, start, end, false);
        return this;
    }


    public SqlWhereGroupByOrderByLimitBuilder andIn(String column, Object... ins) {
        addIn(column, true, ins);
        return this;
    }

    public SqlWhereGroupByOrderByLimitBuilder orIn(String column, Object... ins) {
        addIn(column, false, ins);
        return this;
    }


    public SqlWhereGroupByOrderByLimitBuilder andNotIn(String column, Object... ins) {
        addNotIn(column, true, ins);
        return this;
    }

    public SqlWhereGroupByOrderByLimitBuilder orNotIn(String column, Object... ins) {
        addNotIn(column, false, ins);
        return this;
    }


    public SqlWhereGroupByOrderByLimitBuilder andIsNull(String column) {
        addIsNull(column, true);
        return this;
    }

    public SqlWhereGroupByOrderByLimitBuilder orIsNull(String column) {
        addIsNull(column, false);
        return this;
    }


    public SqlWhereGroupByOrderByLimitBuilder andIsNotNull(String column) {
        addIsNotNull(column, true);
        return this;
    }

    public SqlWhereGroupByOrderByLimitBuilder orIsNotNull(String column) {
        addIsNotNull(column, false);
        return this;
    }

    /**
     * 定制语句， 比如 and ( stop is not null or stop !+ '' ) 的括号里部分
     **/
    public SqlWhereGroupByOrderByLimitBuilder andCustomized(String condition, Object... conditionValues) {
        addCustomized(condition, true, conditionValues);
        return this;
    }

    /**
     * 定制语句， 比如 and ( stop is not null or stop !+ '' ) 的括号里部分
     **/
    public SqlWhereGroupByOrderByLimitBuilder orCustomized(String condition, Object... conditionValues) {
        addCustomized(condition, false, conditionValues);
        return this;
    }


    /**
     * groupBy 语句中分组的部分，不用包含group by关键字，
     * having 如果group by需要having语句，就写到这里面，不用包含having关键字
     **/
    public SqlWhereGroupByOrderByLimitBuilder groupBy(String groupBy, String having) {
        if (StringUtils.isNotBlank(groupBy)) {
            sql.append(" group by ");
            sql.append(groupBy);

            if (StringUtils.isNotBlank(having)) {
                sql.append(" having ");
                sql.append(having);
            }
        }

        return this;
    }

    /**
     * orderBy 语句中排序的字段（可以多个，和sql语法一样），不用包含order by关键字，但是需要包含asc（默认，可省略） 或者desc
     **/
    public SqlWhereGroupByOrderByLimitBuilder orderBy(String orderBy) {
        if (StringUtils.isNotBlank(orderBy)) {
            sql.append(" order by ");
            sql.append(orderBy);
        }
        return this;
    }

    /**
     * offset开始点， 如果小于0，默认是0，
     * count 取的数量，如果小于0，默认是1
     **/
    public SqlWhereGroupByOrderByLimitBuilder limit(int offset, int count) {
        if (offset <= 0)
            offset = 0;

        if (count <= 0)
            count = 1;

        sql.append(" limit ");
        sql.append(offset);
        sql.append(",");
        sql.append(count);

        return this;
    }

    /**
     * count 取的数量，如果小于0，默认是1. offset 默认是0
     **/
    public SqlWhereGroupByOrderByLimitBuilder limit(int count) {
        if (count <= 0)
            count = 1;

        sql.append(" limit ");
        sql.append(count);

        return this;
    }


    private void addEqualTo(String column, Object value, boolean isAnd) {
        if (StringUtils.isBlank(column))
            return;

        if (value == null)
            return;

        addWhereAndOr(isAnd);

        sql.append(column);
        sql.append("=?");
        args.add(value);
    }

    private void addNotEqualTo(String column, Object value, boolean isAnd) {
        if (StringUtils.isBlank(column))
            return;

        if (value == null)
            return;

        addWhereAndOr(isAnd);

        sql.append(column);
        sql.append("!=?");
        args.add(value);
    }

    private void addGreaterThan(String column, Object value, boolean isAnd) {
        if (StringUtils.isBlank(column))
            return;

        if (value == null)
            return;

        addWhereAndOr(isAnd);

        sql.append(column);
        sql.append(">?");
        args.add(value);
    }

    private void addGreaterThanOrEqualTo(String column, Object value, boolean isAnd) {
        if (StringUtils.isBlank(column))
            return;

        if (value == null)
            return;

        addWhereAndOr(isAnd);

        sql.append(column);
        sql.append(">=?");
        args.add(value);
    }

    private void addSmallerThan(String column, Object value, boolean isAnd) {
        if (StringUtils.isBlank(column))
            return;

        if (value == null)
            return;

        addWhereAndOr(isAnd);

        sql.append(column);
        sql.append("<?");
        args.add(value);
    }

    private void addSmallerThanOrEqualTo(String column, Object value, boolean isAnd) {
        if (StringUtils.isBlank(column))
            return;

        if (value == null)
            return;

        addWhereAndOr(isAnd);

        sql.append(column);
        sql.append("<=?");
        args.add(value);
    }

    private void addContains(String column, Object value, boolean isAnd) {
        if (StringUtils.isBlank(column))
            return;

        if (value == null)
            return;

        addWhereAndOr(isAnd);

        sql.append(column);
        sql.append(" like ?");
        value = "%" + value + "%";
        args.add(value);
    }

    private void addNotContains(String column, Object value, boolean isAnd) {
        if (StringUtils.isBlank(column))
            return;

        if (value == null)
            return;

        addWhereAndOr(isAnd);

        sql.append(column);
        sql.append(" not like ?");
        value = "%" + value + "%";
        args.add(value);
    }

    private void addStartWith(String column, Object value, boolean isAnd) {
        if (StringUtils.isBlank(column))
            return;

        if (value == null)
            return;

        addWhereAndOr(isAnd);

        sql.append(column);
        sql.append(" like ?");
        value += "%";
        args.add(value);
    }

    private void addNotStartWith(String column, Object value, boolean isAnd) {
        if (StringUtils.isBlank(column))
            return;

        if (value == null)
            return;

        addWhereAndOr(isAnd);

        sql.append(column);
        sql.append(" not like ?");
        value += "%";
        args.add(value);
    }

    private void addEndWith(String column, Object value, boolean isAnd) {
        if (StringUtils.isBlank(column))
            return;

        if (value == null)
            return;

        addWhereAndOr(isAnd);

        sql.append(column);
        sql.append(" like ?");
        value = "%" + value;
        args.add(value);
    }

    private void addNotEndWith(String column, Object value, boolean isAnd) {
        if (StringUtils.isBlank(column))
            return;

        if (value == null)
            return;

        addWhereAndOr(isAnd);

        sql.append(column);
        sql.append(" not like ?");
        value = "%" + value;
        args.add(value);
    }

    private void addBetween(String column, Object start, Object end, boolean isAnd) {
        if (StringUtils.isBlank(column))
            return;

        if (start == null || end == null)
            return;

        addWhereAndOr(isAnd);

        sql.append(column);
        sql.append(" between ? and ?");
        args.add(start);
        args.add(end);
    }

    private void addIn(String column, boolean isAnd, Object... ins) {
        if (StringUtils.isBlank(column))
            return;

        if (ins == null || ins.length == 0)
            return;

        addWhereAndOr(isAnd);

        sql.append(column);
        sql.append(" in (?");
        for (int i = 1; i < ins.length; i++) {
            sql.append(",?");
        }
        sql.append(")");

        Collections.addAll(args, ins);
    }

    private void addNotIn(String column, boolean isAnd, Object... ins) {
        if (StringUtils.isBlank(column))
            return;

        if (ins == null || ins.length == 0)
            return;

        addWhereAndOr(isAnd);

        sql.append(column);
        sql.append(" not in (?");
        for (int i = 1; i < ins.length; i++) {
            sql.append(",?");
        }
        sql.append(")");

        Collections.addAll(args, ins);
    }

    private void addIsNull(String column, boolean isAnd) {
        if (StringUtils.isBlank(column))
            return;

        addWhereAndOr(isAnd);

        sql.append(column);
        sql.append(" is null");
    }

    private void addIsNotNull(String column, boolean isAnd) {
        if (StringUtils.isBlank(column))
            return;

        addWhereAndOr(isAnd);

        sql.append(column);
        sql.append(" is not null");
    }

    /**
     * 定制语句， 比如 and ( stop is not null or stop !+ '' ) 的括号里部分
     **/
    private void addCustomized(String condition, boolean isAnd, Object... conditionValues) {
        if (StringUtils.isBlank(condition))
            return;

        addWhereAndOr(isAnd);

        sql.append("(");
        sql.append(condition);
        sql.append(")");

        if (conditionValues != null && conditionValues.length != 0)
            Collections.addAll(args, conditionValues);
    }


    private void addWhereAndOr(boolean isAnd) {
        if (whereAdded == false) {
            sql.append(" where ");
            whereAdded = true;
        } else {
            if (isAnd) {
                sql.append(" and ");
            } else {
                sql.append(" or ");
            }
        }
    }
}
