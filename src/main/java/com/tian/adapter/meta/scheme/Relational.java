package com.tian.adapter.meta.scheme;

public enum Relational {

    AND(1, "AND", "并且"),
    OR(2, "OR", "或");

    private final int code;

    private final String rela;

    private final String desc;

    Relational(int code, String rela, String desc) {
        this.code = code;
        this.rela = rela;
        this.desc = desc;
    }

    public int getCode() {
        return this.code;
    }

    public String getRela() {
        return this.rela;
    }

    public String getDesc() {
        return this.desc;
    }

    public static Relational getRelational(int code) {
        for (Relational rela : (Relational[])Relational.class.getEnumConstants()) {
            if (rela.getCode() == code)
                return rela;
        }
        return null;
    }
}
