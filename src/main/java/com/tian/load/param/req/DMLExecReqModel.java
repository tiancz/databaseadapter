package com.tian.load.param.req;

public class DMLExecReqModel extends TradeExecReqModel {
    private String sql;

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return this.sql;
    }
}
