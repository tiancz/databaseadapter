package com.tian.adapter.meta.scheme;

public class Set extends InsertDataModel {
    private String fieldName;

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public Set(int valType, String val, String format, String fieldName) {
        super(valType, val, format);
        this.fieldName = fieldName;
    }

    public Set() {}
}
