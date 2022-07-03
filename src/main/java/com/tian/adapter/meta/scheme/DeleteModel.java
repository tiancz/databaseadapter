package com.tian.adapter.meta.scheme;

import java.util.ArrayList;
import java.util.List;

public class DeleteModel {
    private String tableName;

    private List<Term> deleteWhere = new ArrayList<>();

    public DeleteModel addDeleteWhere(Term term) {
        getDeleteWhere().add(term);
        return this;
    }

    public DeleteModel addDeleteWhere(Term... terms) {
        if (terms != null && terms.length > 0)
            for (Term t : terms)
                addDeleteWhere(t);
        return this;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setDeleteWhere(List<Term> deleteWhere) {
        this.deleteWhere = deleteWhere;
    }

    public String getTableName() {
        return this.tableName;
    }

    public List<Term> getDeleteWhere() {
        return this.deleteWhere;
    }
}
