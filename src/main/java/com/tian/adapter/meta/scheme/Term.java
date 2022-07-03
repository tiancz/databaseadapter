package com.tian.adapter.meta.scheme;

public class Term {
    private int idx;

    private Relational relational;

    private String fieldName;

    private Operator operator;

    private String[] val;

    private int valType;

    public int getValType() {
        return this.valType;
    }

    public void setValType(int valType) {
        this.valType = valType;
    }

    public void setRelational(Relational relational) {
        this.relational = relational;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public void setVal(String[] val) {
        this.val = val;
    }

    public Relational getRelational() {
        return this.relational;
    }

    public int getIdx() {
        return this.idx;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public Operator getOperator() {
        return this.operator;
    }

    public String[] getVal() {
        return this.val;
    }

    public Term() {}

    public Term(int idx, Relational relational, String fieldName, Operator operator, int valType, String... val) {
        this.idx = idx;
        this.relational = relational;
        this.fieldName = fieldName;
        this.operator = operator;
        this.valType = valType;
        this.val = val;
    }
}
