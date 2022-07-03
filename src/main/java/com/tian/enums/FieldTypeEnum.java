package com.tian.enums;

import com.tian.adapter.meta.scheme.Type;

public enum FieldTypeEnum {
    CHAR(11, Type.CHAR, "单字节字符类型"),
    NCHAR(12, Type.NCHAR, "双字节字符类型"),
    VARCHAR(13, Type.VARCHAR, "单字节变长字符类型"),
    NVARCHAR(14, Type.NVARCHAR, "双字节变长字符类型"),
    LONGVARCHAR(15, Type.LONGVARCHAR, "单字节变长文本类型"),
    LONGNVARCHAR(16, Type.LONGNVARCHAR, "双字节变长文本类型"),
    TINYINT(21, Type.TINYINT, "小整数类型"),
    SMALLINT(22, Type.SMALLINT, "小整数类型"),
    INTEGER(23, Type.INTEGER, "整数类型"),
    BIGINT(24, Type.BIGINT, "大整数类型"),
    NUMERIC(25, Type.NUMERIC, "浮点类型"),
    DATE(31, Type.DATE, "日期类型"),
    TIME(32, Type.TIME, "时间类型"),
    TIMESTAMP(33, Type.TIMESTAMP, "时间日期类型"),
    BLOB(41, Type.BLOB, "二进制流");

    int code;

    Type type;

    String desc;

    FieldTypeEnum(int code, Type type, String desc) {
        this.code = code;
        this.type = type;
        this.desc = desc;
    }

    public int getCode() {
        return this.code;
    }

    public Type getType() {
        return this.type;
    }

    public String getDesc() {
        return this.desc;
    }

    public static FieldTypeEnum getFieldTypeEnum(int code) {
        for (FieldTypeEnum typeEnum : (FieldTypeEnum[])FieldTypeEnum.class.getEnumConstants()) {
            if (typeEnum.getCode() == code)
                return typeEnum;
        }
        return null;
    }

    public static FieldTypeEnum getFieldTypeEnum(Type type) {
        for (FieldTypeEnum typeEnum : (FieldTypeEnum[])FieldTypeEnum.class.getEnumConstants()) {
            if (typeEnum.getType() == type)
                return typeEnum;
        }
        return null;
    }
}
