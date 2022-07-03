package com.tian.adapter.sql;

import com.tian.adapter.meta.scheme.*;
import com.tian.adapter.utils.StrUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

public class DMLCreateUtils {
    private static final String separator = ",";

    private static final String tableName = "_TABLENAME";

    private static final String databaseName = "_DATABASE";

    private static final String columnName = "_COLUMN";

    private static final String whereName = "_WHERE";

    private static final String setName = "_VALUE";

    private static final String valueName = "_VALUES";

    private static final String nullName = "null";

    private static final String placeholder = "?";

    public static String toInsertDML(Dialect dialect, InsertModel insertModel) {
        String querySql = dialect.ddlFeatures.insertTableManyDataSQLString;
        querySql = StrUtils.replace(querySql, "_TABLENAME", insertModel.getTableName());
        List<String> column = insertModel.getFieldList();
        String columns = buildColumnDML(column);
        querySql = StrUtils.replace(querySql, "_COLUMN", columns.replaceAll("\\(|\\)", ""));
        List<Map<String, InsertDataModel>> data = insertModel.getDataList();
        String values = buildDataDML(dialect, columns, column, data);
        querySql = StrUtils.replace(querySql, "_VALUES", values);
        return querySql;
    }

    private static String buildColumnDML(List<String> column) {
        if (column == null || column.size() < 1)
            return "";
        StringBuilder columnSql = new StringBuilder();
        column.forEach(c -> columnSql.append("(").append(c).append(")").append(","));
        return columnSql.substring(0, columnSql.length() - 1);
    }

    private static String buildDataDML(Dialect dialect, String columns, List<String> column, List<Map<String, InsertDataModel>> datas) {
        StringBuffer result = new StringBuffer();
        if (column == null || column.size() < 1)
            return result.toString();
        if (datas == null || datas.size() < 1) {
            String v = columns;
            for (String c : column)
                v = v.replace("(" + c + ")", "?");
            return result.append("(").append(v).append(")").toString();
        }
        datas.forEach(data -> {
            String v = columns;
            StringBuffer dataRow = new StringBuffer();
            for (String c : column) {
                InsertDataModel dataModel = (InsertDataModel)data.get(c);
                if (dataModel != null) {
                    v = v.replace("(" + c + ")", buildValDML(dialect, dataModel));
                    continue;
                }
                v = v.replace("(" + c + ")", "null");
            }
            dataRow.append("(").append(v).append(")").append(",");
            result.append(dataRow);
        });
        return result.substring(0, result.length() - 1);
    }

    private static String buildValDML(Dialect dialect, InsertDataModel dataModel) {
        StringBuffer resultVal = new StringBuffer();
        if (ValTypeEnum.INT.getCode() == dataModel.getValType()) {
            resultVal.append(dataModel.getVal());
        } else if (ValTypeEnum.DATE.getCode() == dataModel.getValType()) {
            if (dialect == Dialect.Oracle) {
                resultVal.append("to_date('").append(dataModel.getVal()).append("','").append(dataModel.getFormat()).append("')");
            } else {
                resultVal.append("'").append(dataModel.getVal()).append("'");
            }
        } else {
            resultVal.append("'").append(dataModel.getVal()).append("'");
        }
        return resultVal.toString();
    }

    public static String toUpdateDML(Dialect dialect, UpdateModel updateModel) {
        StringBuffer result = new StringBuffer();
        String querySql = dialect.ddlFeatures.updateTableDataSQLString;
        querySql = StrUtils.replace(querySql, "_TABLENAME", updateModel.getTableName());
        List<Set> setDate = updateModel.getSetData();
        String sets = buildUpDataDML(dialect, setDate);
        if (!StrUtils.isEmpty(sets))
            querySql = StrUtils.replace(querySql, "_VALUE", sets);
        List<Term> where = updateModel.getUpdateWhere();
        String wheres = DQLCreateUtils.buildTermDQL("where", where);
        if (!StrUtils.isEmpty(wheres))
            querySql = StrUtils.replace(querySql, "_WHERE", wheres);
        result.append(querySql);
        return result.toString();
    }

    private static String buildUpDataDML(Dialect dialect, List<Set> setData) {
        if (setData == null || setData.size() < 1)
            return "";
        StringBuilder setSql = new StringBuilder();
        setData.stream().forEach(d -> setSql.append(" ").append(d.getFieldName()).append(" = ").append(buildValDML(dialect, (InsertDataModel)d)).append(","));
        return setSql.substring(0, setSql.length() - 1);
    }

    public static String toDeleteDML(Dialect dialect, DeleteModel deleteModel) {
        StringBuffer result = new StringBuffer();
        String querySql = dialect.ddlFeatures.deleteTableDataSQLString;
        querySql = StrUtils.replace(querySql, "_TABLENAME", deleteModel.getTableName());
        List<Term> where = deleteModel.getDeleteWhere();
        String wheres = DQLCreateUtils.buildTermDQL("where", where);
        if (!StrUtils.isEmpty(wheres))
            querySql = StrUtils.replace(querySql, "_WHERE", wheres);
        result.append(querySql);
        return result.toString();
    }

    public static String toCountSpaceDML(Dialect dialect, DatabaseModel deleteModel) {
        String sql = "";
        DDLFeatures features = dialect.ddlFeatures;
        if (StringUtils.isEmpty(features.countSpaceSQLString) || features.countSpaceSQLString.equals("NOT_SUPPORT"))
            return "";
        if (dialect != Dialect.Oracle) {
            sql = features.countSpaceSQLString.replace("_DATABASE", deleteModel.getDatabaseName());
        } else {
            sql = features.countSpaceSQLString;
        }
        return sql;
    }
}
