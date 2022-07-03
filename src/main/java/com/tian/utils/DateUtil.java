package com.tian.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
    private static final String simDateFormat = "yyyy-MM-dd";

    private static final int simDateFormatLength = "yyyy-MM-dd".length();

    private static final String SimpleDateTimeFormat = "yyyy-MM-dd HH:mm:ss";

    private static final String BJ_TIMEZONE = "GMT+8";

    public static java.sql.Date toSqlDate(Date date) {
        return new java.sql.Date(date.getTime());
    }

    public static Date strToDate(String str) {
        if (null != str && str.length() > 0)
            try {
                if (str.length() <= simDateFormatLength)
                    return (new SimpleDateFormat("yyyy-MM-dd")).parse(str);
                return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                        .parse(str);
            } catch (ParseException error) {
                return null;
            }
        return null;
    }

    public static Date strToSqlDate(String str) {
        if (strToDate(str) == null || str.length() < 1)
            return null;
        return new Date(strToDate(str).getTime());
    }

    public static String dateTimeToStr(Date date) {
        return dateTimeToStr(date, ' ');
    }

    public static String dateTimeToISOStr(Date date) {
        return dateTimeToStr(date, 'T');
    }

    public static String dateTimeToStr(Date date, char c) {
        if (c == ' ')
            return dateTimeToStr(date, '-', ' ', ':');
        return dateTimeToStr(date, '0', 'T', ':');
    }

    public static String dateTimeToStr(Date date, char c, char c1, char c2) {
        if (date == null)
            return null;
        StringBuffer stringbuffer = new StringBuffer(20);
        GregorianCalendar gregoriancalendar = new GregorianCalendar();
        gregoriancalendar.setTime(date);
        stringbuffer.append(gregoriancalendar.get(1));
        if (c != '\000')
            stringbuffer.append(c);
        int i = gregoriancalendar.get(2) + 1;
        appendInt(stringbuffer, i);
        if (c != '\000')
            stringbuffer.append(c);
        int j = gregoriancalendar.get(5);
        appendInt(stringbuffer, j);
        int k = gregoriancalendar.get(11);
        int l = gregoriancalendar.get(12);
        int i1 = gregoriancalendar.get(13);
        if (k + l + i1 > 0) {
            if (c1 != '\000')
                stringbuffer.append(c1);
            appendInt(stringbuffer, k);
            if (c2 != '\000')
                stringbuffer.append(c2);
            appendInt(stringbuffer, l);
            if (c2 != '\000')
                stringbuffer.append(c2);
            appendInt(stringbuffer, i1);
        }
        return stringbuffer.toString();
    }

    private static void appendInt(StringBuffer stringbuffer, int i) {
        if (i < 10)
            stringbuffer.append("0");
        stringbuffer.append(i);
    }
}
