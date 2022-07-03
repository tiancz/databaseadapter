package com.tian.adapter.utils;

import com.tian.adapter.exception.DialectException;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class StrUtils {
    private static final Pattern isAllWhitespacePattern = Pattern.compile("^\\s*$");

    private static final SecureRandom random = new SecureRandom();

    private static final char[] ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();

    public static boolean isBlank(String text) {
        return (text == null || text.isEmpty() || isAllWhitespacePattern
                .matcher(text).matches());
    }

    public static boolean isEmpty(Object str) {
        return (str == null || "".equals(str));
    }

    public static boolean hasLength(CharSequence str) {
        return (str != null && str.length() > 0);
    }

    public static boolean hasLength(String str) {
        return hasLength(str);
    }

    public static boolean startsWithIgnoreCase(String str, String prefix) {
        if (str == null || prefix == null)
            return false;
        if (str.startsWith(prefix))
            return true;
        if (str.length() < prefix.length())
            return false;
        String lcStr = str.substring(0, prefix.length()).toLowerCase();
        String lcPrefix = prefix.toLowerCase();
        return lcStr.equals(lcPrefix);
    }

    public static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null)
            return false;
        int length = searchStr.length();
        if (length == 0)
            return true;
        for (int i = str.length() - length; i >= 0; i--) {
            if (str.regionMatches(true, i, searchStr, 0, length))
                return true;
        }
        return false;
    }

    public static String replaceIgnoreCase(String text, String findtxt, String replacetxt) {
        if (text == null)
            return null;
        String str = text;
        if (findtxt == null || findtxt.length() == 0)
            return str;
        if (findtxt.length() > str.length())
            return str;
        int counter = 0;
        while (counter < str.length() && str.substring(counter).length() >= findtxt.length()) {
            String thesubstr = str.substring(counter, counter + findtxt.length());
            if (thesubstr.equalsIgnoreCase(findtxt)) {
                str = str.substring(0, counter) + replacetxt + str.substring(counter + findtxt.length());
                counter += replacetxt.length();
                continue;
            }
            counter++;
        }
        return str;
    }

    public static String replace(String originString, String oldPattern, String newPattern) {
        if (!hasLength(originString) || !hasLength(oldPattern) || newPattern == null)
            return originString;
        StringBuilder sb = new StringBuilder();
        int pos = 0;
        int index = originString.indexOf(oldPattern);
        int patLen = oldPattern.length();
        while (index >= 0) {
            sb.append(originString.substring(pos, index));
            sb.append(newPattern);
            pos = index + patLen;
            index = originString.indexOf(oldPattern, pos);
        }
        sb.append(originString.substring(pos));
        return sb.toString();
    }

    public static String[] split(String str, char separatorChar) {
        return splitWorker(str, separatorChar, false);
    }

    private static String[] splitWorker(String str, char separatorChar, boolean preserveAllTokens) {
        if (str == null)
            return new String[0];
        int len = str.length();
        if (len == 0)
            return new String[0];
        List<String> list = new ArrayList<>();
        int i = 0;
        int start = 0;
        boolean match = false;
        boolean lastMatch = false;
        while (i < len) {
            if (str.charAt(i) == separatorChar) {
                if (match || preserveAllTokens) {
                    list.add(str.substring(start, i));
                    match = false;
                    lastMatch = true;
                }
                start = ++i;
                continue;
            }
            lastMatch = false;
            match = true;
            i++;
        }
        if (match || (preserveAllTokens && lastMatch))
            list.add(str.substring(start, i));
        return list.<String>toArray(new String[list.size()]);
    }

    public static int countMatches(CharSequence str, char ch) {
        if (isEmpty(str))
            return 0;
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (ch == str.charAt(i))
                count++;
        }
        return count;
    }

    public static String arrayToStringButSkipFirst(Object[] array) {
        if (array == null)
            DialectException.throwEX("StrUtils arrayToString() method do not accept null parameter");
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (Object object : array) {
            if (i++ != 1)
                sb.append("" + object + ",");
        }
        if (sb.length() > 0)
            sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public static String arrayToString(Object[] array) {
        if (array == null)
            DialectException.throwEX("StrUtils arrayToString() method do not accept null parameter");
        StringBuilder sb = new StringBuilder();
        for (Object object : array)
            sb.append("" + object + ",");
        if (sb.length() > 0)
            sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public static String arrayToString(Object[] array, String seperateString) {
        if (array == null)
            DialectException.throwEX("StrUtils arrayToString() method do not accept null parameter");
        StringBuilder sb = new StringBuilder();
        for (Object object : array)
            sb.append("" + object + seperateString);
        if (sb.length() > 0)
            sb.setLength(sb.length() - seperateString.length());
        return sb.toString();
    }

    public static String listToString(List<?> lst) {
        if (lst == null)
            DialectException.throwEX("StrUtils listToString() method do not accept null parameter");
        StringBuilder sb = new StringBuilder();
        for (Object object : lst)
            sb.append("" + object + ",");
        if (sb.length() > 0)
            sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public static String joinStringList(List<String> list, String joiner) {
        StringBuilder sb = new StringBuilder();
        for (String str : list)
            sb.append(str).append(joiner);
        String result = sb.toString();
        return result.substring(0, result.length() - 1);
    }

    public static String[] joinStringArray(String[] array1, String[] array2) {
        List<String> l = new ArrayList<>();
        for (String string : array1)
            l.add(string);
        for (String string : array2)
            l.add(string);
        return l.<String>toArray(new String[l.size()]);
    }

    public static String[] split(String separators, String list) {
        return split(separators, list, false);
    }

    public static String[] split(String separators, String list, boolean include) {
        StringTokenizer tokens = new StringTokenizer(list, separators, include);
        String[] result = new String[tokens.countTokens()];
        int i = 0;
        while (tokens.hasMoreTokens())
            result[i++] = tokens.nextToken();
        return result;
    }

    public static String quote(String s) {
        return "'" + s + "'";
    }
}
