package com.tian.db;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class BeanUtil {
    private static final Map<Class<?>, Object> primitiveDefaults = new HashMap<>();

    static {
        primitiveDefaults.put(int.class, Integer.valueOf(0));
        primitiveDefaults.put(short.class, Short.valueOf((short)0));
        primitiveDefaults.put(byte.class, Byte.valueOf((byte)0));
        primitiveDefaults.put(float.class, Float.valueOf(0.0F));
        primitiveDefaults.put(double.class, Double.valueOf(0.0D));
        primitiveDefaults.put(long.class, Long.valueOf(0L));
        primitiveDefaults.put(boolean.class, Boolean.FALSE);
        primitiveDefaults.put(char.class, Character.valueOf('0'));
    }

    public static Object createBean(ResultSet rs, Class<?> type) {
        try {
            Object bean = type.newInstance();
            ResultSetMetaData metaData = rs.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String columnName = metaData.getColumnLabel(i);
                mapping(columnName, type, bean, rs);
            }
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void mapping(String columnName, Class<?> type, Object bean, ResultSet rs) throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(type, Object.class);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor pd : propertyDescriptors) {
            String fieldName = pd.getName();
            Field field = type.getDeclaredField(fieldName);
            if (field.isAnnotationPresent((Class) Column.class)) {
                String annotationVal = ((Column)field.<Column>getAnnotation(Column.class)).value();
                if (columnName.equalsIgnoreCase(annotationVal)) {
                    setValue(rs, pd, columnName, bean);
                    break;
                }
            } else if (columnName.equalsIgnoreCase(fieldName)) {
                setValue(rs, pd, columnName, bean);
                break;
            }
        }
    }

    private static void setValue(ResultSet rs, PropertyDescriptor pd, String columnName, Object bean) throws Exception {
        Object value = (rs.getObject(columnName) == null) ? primitiveDefaults.get(pd.getPropertyType()) : processValue(rs, columnName, pd
                .getPropertyType());
        value = processDate(value, pd.getWriteMethod().getParameterTypes()[0]);
        pd.getWriteMethod().invoke(bean, new Object[] { value });
    }

    static Object processValue(ResultSet rs, String columnName, Class<?> propType) throws SQLException {
        if (propType.equals(String.class))
            return rs.getString(columnName);
        if (propType.equals(int.class) || propType
                .equals(Integer.class))
            return Integer.valueOf(rs.getInt(columnName));
        if (propType.equals(boolean.class) || propType
                .equals(Boolean.class))
            return Boolean.valueOf(rs.getBoolean(columnName));
        if (propType.equals(long.class) || propType.equals(Long.class))
            return Long.valueOf(rs.getLong(columnName));
        if (propType.equals(double.class) || propType
                .equals(Double.class))
            return Double.valueOf(rs.getDouble(columnName));
        if (propType.equals(float.class) || propType.equals(Float.class))
            return Float.valueOf(rs.getFloat(columnName));
        if (propType.equals(short.class) || propType.equals(Short.class))
            return Short.valueOf(rs.getShort(columnName));
        if (propType.equals(byte.class) || propType.equals(Byte.class))
            return Byte.valueOf(rs.getByte(columnName));
        if (propType.equals(Timestamp.class))
            return rs.getTimestamp(columnName);
        if (propType.equals(SQLXML.class))
            return rs.getSQLXML(columnName);
        return rs.getObject(columnName);
    }

    static Object processDate(Object value, Class<?> paramType) {
        if (value instanceof Date) {
            String type = paramType.getName();
            if ("java.sql.Date".equals(type)) {
                value = new Date(((Date)value).getTime());
            } else if ("java.sql.Time".equals(type)) {
                value = new Time(((Date)value).getTime());
            } else if ("java.sql.Timestamp".equals(type)) {
                Timestamp ts = (Timestamp)value;
                int nanos = ts.getNanos();
                value = new Timestamp(ts.getTime());
                ((Timestamp)value).setNanos(nanos);
            }
        }
        return value;
    }
}
