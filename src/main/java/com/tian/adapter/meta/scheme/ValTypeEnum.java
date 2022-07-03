package com.tian.adapter.meta.scheme;

public enum ValTypeEnum {
    CHAR(1, "字符类型"),

    INT(2, "整数类型"),

    DATE(3, "日期类型");

    private final int code;

    private final String desc;

    ValTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    public static ValTypeEnum getValTypeEnum(int code) {
        for (ValTypeEnum calTypeEnum : (ValTypeEnum[])ValTypeEnum.class.getEnumConstants()) {
            if (calTypeEnum.getCode() == code)
                return calTypeEnum;
        }
        return null;
    }
}
