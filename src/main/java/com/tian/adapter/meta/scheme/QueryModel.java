package com.tian.adapter.meta.scheme;

import java.util.ArrayList;
import java.util.List;

public class QueryModel {
    private String tableName;

    private List<String> queryField = new ArrayList<>();

    private List<Term> queryWhere = new ArrayList<>();

    private List<String> groups = new ArrayList<>();

    private List<Term> queryHaving = new ArrayList<>();

    private List<Order> orders = new ArrayList<>();

    public QueryModel addQueryField(String queryField) {
        getQueryField().add(queryField);
        return this;
    }

    public QueryModel addQueryField(String... queryFields) {
        if (queryFields != null && queryFields.length > 0)
            for (String s : queryFields)
                addQueryField(s);
        return this;
    }

    public QueryModel addWhere(Term where) {
        getQueryWhere().add(where);
        return this;
    }

    public QueryModel addWhere(Term... wheres) {
        if (wheres != null && wheres.length > 0)
            for (Term where : wheres)
                addWhere(where);
        return this;
    }

    public QueryModel addGroups(String group) {
        getGroups().add(group);
        return this;
    }

    public QueryModel addGroups(String... groups) {
        if (groups != null && groups.length > 0)
            for (String group : groups)
                addGroups(group);
        return this;
    }

    public QueryModel addHaving(Term having) {
        getQueryHaving().add(having);
        return this;
    }

    public QueryModel addHaving(Term... havings) {
        if (havings != null && havings.length > 0)
            for (Term having : havings)
                addHaving(having);
        return this;
    }

    public QueryModel addOrder(Order order) {
        getOrders().add(order);
        return this;
    }

    public QueryModel addOrder(Order... orders) {
        if (orders != null && orders.length > 0)
            for (Order order : orders)
                addOrder(order);
        return this;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setQueryField(List<String> queryField) {
        this.queryField = queryField;
    }

    public void setQueryWhere(List<Term> queryWhere) {
        this.queryWhere = queryWhere;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public void setQueryHaving(List<Term> queryHaving) {
        this.queryHaving = queryHaving;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public String getTableName() {
        return this.tableName;
    }

    public List<String> getQueryField() {
        return this.queryField;
    }

    public List<Term> getQueryWhere() {
        return this.queryWhere;
    }

    public List<String> getGroups() {
        return this.groups;
    }

    public List<Term> getQueryHaving() {
        return this.queryHaving;
    }

    public List<Order> getOrders() {
        return this.orders;
    }
}
