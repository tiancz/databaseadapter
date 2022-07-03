package com.tian.adapter.meta.scheme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TableDataModel {
    private String tableName;

    private String id;

    private List<String> columnList = Collections.emptyList();

    private List<Item> keyValueList = Collections.emptyList();

    private List<Order> orderList = Collections.emptyList();

    private String orderString;

    private String limitString;

    public TableDataModel() {}

    public TableDataModel(String tableName) {
        this.tableName = tableName;
    }

    public TableDataModel id(String id) {
        this.id = "'" + id + "'";
        return this;
    }

    public TableDataModel tableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public TableDataModel column(Item item) {
        if (this.keyValueList.size() == 0)
            this.keyValueList = new ArrayList<>();
        this.keyValueList.add(item);
        return this;
    }

    public TableDataModel column(List<String> columns) {
        if (this.columnList.size() == 0)
            this.columnList = new ArrayList<>();
        this.columnList.addAll(columns);
        return this;
    }

    public TableDataModel column(String column) {
        if (this.columnList.size() == 0)
            this.columnList = new ArrayList<>();
        this.columnList.add(column);
        return this;
    }

    public TableDataModel column(String col1, String col2, String col3, String... col) {
        return column(col1).column(col2).column(col3).column(Arrays.asList(col));
    }

    public TableDataModel column(String key, Object value) {
        return column(new Item(key, value));
    }

    public TableDataModel order(String columnName) {
        return order(columnName, OrderType.ASC);
    }

    public TableDataModel order(List<Order> subList) {
        if (this.orderList.size() == 0)
            this.orderList = new ArrayList<>();
        this.orderList.addAll(subList);
        return this;
    }

    public TableDataModel order(String columnName, OrderType orderType) {
        if (this.orderList.size() == 0)
            this.orderList = new ArrayList<>();
        this.orderList.add(new Order(0, columnName, orderType));
        return this;
    }

    public TableDataModel toOrder() {
        if (this.orderList.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (Order order : this.orderList) {
                sb.append(order.getColumnName());
                if (order.getOrderType() == OrderType.DESC)
                    sb.append(" desc");
                sb.append(", ");
            }
            String result = sb.toString();
            this.orderString = result.substring(0, result.length() - 2);
        }
        return this;
    }

    public TableDataModel limit(String limitString) {
        this.limitString = limitString;
        return this;
    }

    public static class Item {
        private String key;

        private Object value;

        public Item() {}

        public Item(String key, Object value) {
            this.key = key;
            if (value instanceof String) {
                this.value = "'" + value + "'";
            } else {
                this.value = value;
            }
        }

        public String getKey() {
            return this.key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Object getValue() {
            return this.value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<Item> getKeyValueList() {
        return this.keyValueList;
    }

    public void setKeyValueList(List<Item> keyValueList) {
        this.keyValueList = keyValueList;
    }

    public List<Order> getOrderList() {
        return this.orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public String getOrderString() {
        return this.orderString;
    }

    public void setOrderString(String orderString) {
        this.orderString = orderString;
    }

    public List<String> getColumnList() {
        return this.columnList;
    }

    public void setColumnList(List<String> columnList) {
        this.columnList = columnList;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLimitString() {
        return this.limitString;
    }

    public void setLimitString(String limitString) {
        this.limitString = limitString;
    }
}
