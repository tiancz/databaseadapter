package com.tian.adapter.meta.scheme;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InsertModel {
    private String tableName;

    private List<String> fieldList = new ArrayList<>();

    private List<Map<String, InsertDataModel>> dataList = new ArrayList<>();

    public InsertModel addDataList(Map<String, InsertDataModel> rowData) {
        getDataList().add(rowData);
        return this;
    }

    public InsertModel addDataList(Map<String, InsertDataModel>... rowDatas) {
        if (rowDatas != null && rowDatas.length > 0)
            for (Map<String, InsertDataModel> rowData : rowDatas)
                addDataList(rowData);
        return this;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setFieldList(List<String> fieldList) {
        this.fieldList = fieldList;
    }

    public void setDataList(List<Map<String, InsertDataModel>> dataList) {
        this.dataList = dataList;
    }

    public String getTableName() {
        return this.tableName;
    }

    public List<String> getFieldList() {
        return this.fieldList;
    }

    public List<Map<String, InsertDataModel>> getDataList() {
        return this.dataList;
    }
}
