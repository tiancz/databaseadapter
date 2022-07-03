package com.tian.adapter.meta.scheme;

public class Order {
    private int idx;

    private String columnName;

    private OrderType orderType;

    public Order() {}

    public Order(int idx, String columnName, Integer orderDirection) {
        this.idx = idx;
        this.columnName = columnName;
        if (2 == orderDirection.intValue()) {
            this.orderType = OrderType.DESC;
        } else {
            this.orderType = OrderType.ASC;
        }
    }

    public Order(int idx, String columnName, OrderType orderType) {
        this.idx = idx;
        this.columnName = columnName;
        this.orderType = orderType;
    }

    public String getColumnName() {
        return this.columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public OrderType getOrderType() {
        return this.orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public int getIdx() {
        return this.idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }
}
