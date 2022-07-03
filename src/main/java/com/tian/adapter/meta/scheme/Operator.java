package com.tian.adapter.meta.scheme;

public enum Operator {
    GT(1, ">", "大于"),
    LT(2, "<", "小于"),
    ET(3, "=", "等于"),
    GTE(4, ">=", "大于等于"),
    LTE(5, "<=", "小于等于"),
    NET(6, "<>", "不等于"),
    LINK(7, "like", "模糊匹配"),
    IN(8, "in", "包括"),
    NOTIN(9, "not in", "不包括"),
    ISNULL(10, "is null", "为空"),
    ISNOTNULL(11, "is not null", "不为空");

    private final int code;

    private final String oper;

    private final String desc;

    Operator(int code, String oper, String desc) {
        this.code = code;
        this.oper = oper;
        this.desc = desc;
    }

    public int getCode() {
        return this.code;
    }

    public String getOper() {
        return this.oper;
    }

    public String getDesc() {
        return this.desc;
    }

    public static Operator getOperator(int code) {
        for (Operator operator : (Operator[])Operator.class.getEnumConstants()) {
            if (operator.getCode() == code)
                return operator;
        }
        return null;
    }
}
