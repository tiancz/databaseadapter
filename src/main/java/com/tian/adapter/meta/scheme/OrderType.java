package com.tian.adapter.meta.scheme;

public enum OrderType {
    ASC(1, "ASC", "升序"),

    DESC(2, "DESC", "降序");

    private final int code;

    private final String type;

    private final String desc;

    OrderType(int code, String type, String desc) {
        this.code = code;
        this.type = type;
        this.desc = desc;
    }

    public int getCode() {
        return this.code;
    }

    public String getType() {
        return this.type;
    }

    public String getDesc() {
        return this.desc;
    }

    public static OrderType getOrderType(int code) {
        for (OrderType orderType : (OrderType[])OrderType.class.getEnumConstants()) {
            if (orderType.getCode() == code)
                return orderType;
        }
        return null;
    }
}
