package com.tian.adapter.meta.scheme;

import java.util.ArrayList;
import java.util.List;

public class UpdateModel {
    private String tableName;

    private List<Set> setData = new ArrayList<>();

    private List<Term> updateWhere = new ArrayList<>();

    public UpdateModel addSetData(Set setdata) {
        getSetData().add(setdata);
        return this;
    }

    public UpdateModel addSetData(Set... setdatas) {
        if (setdatas != null && setdatas.length > 0)
            for (Set s : setdatas)
                addSetData(s);
        return this;
    }

    public UpdateModel addUpdateWhere(Term updateWhere) {
        getUpdateWhere().add(updateWhere);
        return this;
    }

    public UpdateModel addUpdateWhere(Term... updateWheres) {
        if (updateWheres != null && updateWheres.length > 0)
            for (Term where : updateWheres)
                addUpdateWhere(where);
        return this;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setSetData(List<Set> setData) {
        this.setData = setData;
    }

    public void setUpdateWhere(List<Term> updateWhere) {
        this.updateWhere = updateWhere;
    }

    public String getTableName() {
        return this.tableName;
    }

    public List<Set> getSetData() {
        return this.setData;
    }

    public List<Term> getUpdateWhere() {
        return this.updateWhere;
    }
}
