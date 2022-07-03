package com.tian.adapter.sql;

import com.tian.adapter.meta.scheme.Operator;
import com.tian.adapter.meta.scheme.Order;
import com.tian.adapter.meta.scheme.QueryModel;
import com.tian.adapter.meta.scheme.Term;
import com.tian.adapter.utils.StrUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DQLCreateUtils {
    public static String toQueryDQL(Dialect dialect, QueryModel queryModel) {
        StringBuffer resultSql = new StringBuffer();
        String querySql = dialect.ddlFeatures.selectTableDataListSQLString;
        querySql = StrUtils.replace(querySql, "_TABLENAME", queryModel.getTableName());
        List<String> column = queryModel.getQueryField();
        String columns = buildColumnDQL(column);
        querySql = StrUtils.replace(querySql, "_COLUMN", columns);
        resultSql.append(querySql);
        List<Term> where = queryModel.getQueryWhere();
        String wheres = buildTermDQL("where", where);
        if (!StrUtils.isEmpty(wheres))
            resultSql.append(wheres);
        List<String> group = queryModel.getGroups();
        String groups = buildColumnDQL(group);
        if (!StrUtils.isEmpty(groups))
            resultSql.append(" group by ").append(groups).append(" ");
        List<Term> having = queryModel.getQueryHaving();
        String havings = buildTermDQL("having", having);
        if (!StrUtils.isEmpty(havings))
            resultSql.append(havings);
        List<Order> order = queryModel.getOrders();
        String orders = buildOrderDQL(order);
        if (!StrUtils.isEmpty(orders))
            resultSql.append("order by ").append(orders).append(" ");
        return resultSql.toString();
    }

    private static String buildColumnDQL(List<String> column) {
        if (column == null || column.size() < 1)
            return "";
        StringBuilder columnSql = new StringBuilder();
        column.forEach(c -> columnSql.append(c).append(","));
        return columnSql.substring(0, columnSql.length() - 1);
    }

    public static String buildTermDQL(String queryKey, List<Term> terms) {
        if (terms == null || terms.size() < 1)
            return "";
        StringBuilder termSql = new StringBuilder();
        termSql.append(" ").append(queryKey).append(" ");
        boolean isTop = true;
        terms = (List<Term>)terms.stream().sorted((x, y) -> x.getIdx() - y.getIdx()).collect(Collectors.toList());
        for (Term t : terms) {
            if (isTop) {
                isTop = false;
            } else {
                termSql.append(t.getRelational().getRela()).append(" ");
            }
            if (t.getOperator() == Operator.GT || t.getOperator() == Operator.LT || t
                    .getOperator() == Operator.ET || t.getOperator() == Operator.GTE || t
                    .getOperator() == Operator.LTE || t.getOperator() == Operator.NET || t
                    .getOperator() == Operator.LINK) {
                termSql.append(t.getFieldName()).append(" ").append(t.getOperator().getOper()).append(" ").append(columnValTypeConve(t.getValType(), t.getVal())).append(" ");
                continue;
            }
            if (t.getOperator() == Operator.IN || t.getOperator() == Operator.NOTIN) {
                termSql.append(t.getFieldName()).append(" ").append(t.getOperator().getOper()).append(" (").append(columnValTypeConve(t.getValType(), t.getVal())).append(") ");
                continue;
            }
            if (t.getOperator() == Operator.ISNULL || t.getOperator() == Operator.ISNOTNULL)
                termSql.append(t.getFieldName()).append(" ").append(t.getOperator().getOper()).append(" ");
        }
        return termSql.toString();
    }

    public static String columnValTypeConve(int type, String... val) {
        if (val == null || val.length < 1)
            return "";
        StringBuilder columnSql = new StringBuilder();
        if (type == 1) {
            Arrays.<String>asList(val).forEach(c -> columnSql.append(c).append(","));
        } else {
            Arrays.<String>asList(val).forEach(c -> columnSql.append("'").append(c).append("',"));
        }
        return columnSql.substring(0, columnSql.length() - 1);
    }

    private static String buildOrderDQL(List<Order> orders) {
        if (orders == null || orders.size() < 1)
            return "";
        StringBuilder orderSql = new StringBuilder();
        orders = (List<Order>)orders.stream().sorted((x, y) -> x.getIdx() - y.getIdx()).collect(Collectors.toList());
        orders.forEach(c -> orderSql.append(c.getColumnName()).append(" ").append(c.getOrderType().toString()).append(","));
        return orderSql.substring(0, orderSql.length() - 1);
    }
}
