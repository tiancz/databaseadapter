package com.tian.adapter.meta.scheme;

public class InsertDataModel {
    private int valType;

    private String val;

    private String format;

    public void setValType(int valType) {
        this.valType = valType;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getValType() {
        return this.valType;
    }

    public String getVal() {
        return this.val;
    }

    public String getFormat() {
        return this.format;
    }

    public InsertDataModel() {}

    public InsertDataModel(int valType, String val, String format) {
        this.valType = valType;
        this.val = val;
        this.format = format;
    }
}
