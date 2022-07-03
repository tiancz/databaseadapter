package com.tian.adapter.utils;

public class Assert {
    public static void notNull(Object object, String message) {
        if (object == null)
            throw new IllegalArgumentException(message);
    }

    public static void notEmpty(String str, String message) {
        notNull(str, "str must not be null");
        if (str.equals(""))
            throw new IllegalArgumentException(message);
    }
}
