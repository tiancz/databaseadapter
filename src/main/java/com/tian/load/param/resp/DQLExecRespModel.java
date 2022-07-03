package com.tian.load.param.resp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DQLExecRespModel extends TradeExecRespModel {
    private List<Map> result = new ArrayList<>();

    public List<Map> getResult() {
        return this.result;
    }

    public void setResult(List<Map> result) {
        this.result = result;
    }
}
