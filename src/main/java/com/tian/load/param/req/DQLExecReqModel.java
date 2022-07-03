package com.tian.load.param.req;

public class DQLExecReqModel extends TradeExecReqModel {
    private String sql;

    public String getSql() {
        return this.sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
