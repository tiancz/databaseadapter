package com.tian.adapter.sql;

import com.tian.adapter.exception.DialectException;
import com.tian.adapter.meta.scheme.*;
import com.tian.adapter.utils.StrUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class DDLCreateUtils {
    public static String[] toCreateDDL(Dialect dialect, TableModel... tables) {
        List<String> stringResultList = new ArrayList<>();
        for (TableModel table : tables)
            transferTableToStringList(dialect, table, stringResultList);
        String[] result = stringResultList.<String>toArray(new String[stringResultList.size()]);
        return result;
    }

    private static void transferTableToStringList(Dialect dialect, TableModel t, List<String> stringResultList) {
        DDLFeatures features = dialect.ddlFeatures;
        StringBuilder buf = new StringBuilder();
        boolean hasPkey = false;
        String pkeys = "";
        String tableName = t.getTableName();
        List<Column> columns = t.getColumns();
        for (Column col : columns) {
            if (col.getTransientable().booleanValue())
                continue;
            if (col.getPkey().booleanValue()) {
                hasPkey = true;
                if (StrUtils.isEmpty(pkeys)) {
                    pkeys = col.getColumnName();
                    continue;
                }
                pkeys = pkeys + "," + col.getColumnName();
            }
        }
        buf.append(hasPkey ? dialect.ddlFeatures.createTableString : dialect.ddlFeatures.createMultisetTableString)
                .append(" ").append(tableName).append(" ( ");
        for (Column c : columns) {
            if (c.getTransientable().booleanValue())
                continue;
            if (c.getColumnType() == null)
                DialectException.throwEX("Type not set on column \"" + c.getColumnName() + "\" at table \"" + tableName + "\"");
            buf.append(c.getColumnName()).append(" ");
            buf.append(dialect.translateToDDLType(c.getColumnType(), c.getLengths()));
            String defaultValue = c.getDefaultValue();
            if (defaultValue != null)
                buf.append(" default ").append(defaultValue);
            if (!c.getNullable().booleanValue()) {
                buf.append(" not null");
            } else {
                buf.append(features.nullColumnString);
            }
            if (!StrUtils.isEmpty(c.getCheck()))
                if (features.supportsColumnCheck.booleanValue()) {
                    buf.append(" check (").append(c.getCheck()).append(")");
                } else {
                    Dialect.logger.warn("Ignore unsupported check setting for dialect \"" + dialect + "\" on column \"" + c
                            .getColumnName() + "\" at table \"" + tableName + "\" with value: " + c.getCheck());
                }
            if (c.getComment() != null)
                if (StrUtils.isEmpty(features.columnComment) && !features.supportsCommentOn.booleanValue()) {
                    Dialect.logger.warn("Ignore unsupported comment setting for dialect \"" + dialect + "\" on column \"" + c
                            .getColumnName() + "\" at table \"" + tableName + "\" with value: " + c.getComment());
                } else {
                    buf.append(StrUtils.replace(features.columnComment, "_COMMENT", c.getComment()));
                }
            if (!StrUtils.isEmpty(c.getTail()))
                buf.append(c.getTail());
            buf.append(",");
        }
        if (!StrUtils.isEmpty(pkeys))
            buf.append(" primary key (").append(pkeys).append("),");
        if (!StrUtils.isEmpty(t.getCheck()))
            if (features.supportsTableCheck.booleanValue()) {
                buf.append(" check (").append(t.getCheck()).append("),");
            } else {
                Dialect.logger.warn("Ignore unsupported table check setting for dialect \"" + dialect + "\" on table \"" + tableName + "\" with value: " + t
                        .getCheck());
            }
        buf.setLength(buf.length() - 1);
        buf.append(")");
        String tableTypeString = features.tableTypeString;
        if (!StrUtils.isEmpty(tableTypeString) && !"NOT_SUPPORT".equals(tableTypeString)) {
            buf.append(tableTypeString);
            if (!StrUtils.isEmpty(t.getEngineTail()))
                buf.append(" ").append(t.getEngineTail());
        }
        if (!StrUtils.isBlank(t.getComment()) && !StrUtils.isEmpty(features.tableComment) && !features.supportsCommentOn.booleanValue())
            buf.append(features.tableComment.replace("_COMMENT", t.getComment()));
        stringResultList.add(buf.toString());
        if (t.getComment() != null)
            if (features.supportsCommentOn.booleanValue()) {
                stringResultList.add("comment on table " + t.getTableName() + " is '" + t.getComment() + "'");
            } else {
                Dialect.logger.warn("Ignore unsupported table comment setting for dialect \"" + dialect + "\" on table \"" + tableName + "\" with value: " + t
                        .getComment());
            }
        for (Column c : columns) {
            if (features.supportsCommentOn.booleanValue() && c.getComment() != null && StrUtils.isEmpty(features.columnComment))
                stringResultList.add("comment on column " + tableName + '.' + c
                        .getColumnName() + " is '" + c.getComment() + "'");
        }
        buildIndexDLL(dialect, stringResultList, t);
        buildUniqueDLL(dialect, stringResultList, t);
    }

    private static void buildIndexDLL(Dialect dialect, List<String> stringResultList, TableModel t) {
        List<Index> l = t.getIndexConsts();
        if (l == null || l.isEmpty())
            return;
        String template = "create $ifUnique index $indexName on " + t.getTableName() + " ($indexValues)";
        for (Index index : l) {
            String indexname = index.getName();
            if (StrUtils.isEmpty(indexname))
                indexname = "IX_" + t.getTableName() + "_" + StrUtils.arrayToString(index.getColumnList().toArray(), "_");
            String ifUnique = index.getUnique() ? "unique" : "";
            String result = StrUtils.replace(template, "$ifUnique", ifUnique);
            result = StrUtils.replace(result, "$indexName", indexname);
            result = StrUtils.replace(result, "$indexValues", StrUtils.arrayToString(index.getColumnList().toArray()));
            stringResultList.add(result);
        }
    }

    private static void buildUniqueDLL(Dialect dialect, List<String> stringResultList, TableModel t) {
        List<Unique> l = t.getUniqueConsts();
        if (l == null || l.isEmpty())
            return;
        String dialectName = "" + dialect;
        for (Unique unique : l) {
            boolean nullable = false;
            String[] columns = unique.getColumnList();
            for (String colNames : columns) {
                Column vc = t.getColumn(colNames.toLowerCase());
                if (vc != null && vc.getNullable().booleanValue())
                    nullable = true;
            }
            String uniqueName = unique.getName();
            if (StrUtils.isEmpty(uniqueName))
                uniqueName = "UK_" + t.getTableName() + "_" + StrUtils.arrayToString((Object[])unique.getColumnList(), "_");
            String template = "alter table $TABLE add constraint $UKNAME unique ($COLUMNS)";
            if ((StrUtils.startsWithIgnoreCase(dialectName, "DB2") ||

                    StrUtils.startsWithIgnoreCase(dialectName, "DERBY")) && nullable) {
                template = "create unique index $UKNAME on $TABLE ($COLUMNS)";
            } else if (StrUtils.startsWithIgnoreCase(dialectName, "Informix")) {
                template = "alter table $TABLE add constraint unique ($COLUMNS) constraint $UKNAME";
            }
            String result = StrUtils.replace(template, "$TABLE", t.getTableName());
            result = StrUtils.replace(result, "$UKNAME", uniqueName);
            result = StrUtils.replace(result, "$COLUMNS", StrUtils.arrayToString((Object[])unique.getColumnList()));
            stringResultList.add(result);
        }
    }

    protected static String createUserSQLTemplate(Dialect dialect, UserModel dserModel) {
        StringBuilder buf = new StringBuilder();
        String userName = dserModel.getUserName();
        String password = dserModel.getPassword();
        DDLFeatures features = dialect.ddlFeatures;
        if (StringUtils.isEmpty(features.createUserSqlString) || features.createUserSqlString.equals("NOT_SUPPORT"))
            return "";
        String createUserTemp = features.createUserSqlString.replace("_USERNAME", userName);
        createUserTemp = createUserTemp.replace("_PASSWORD", password);
        buf.append(createUserTemp);
        if (StringUtils.isNotEmpty(dserModel.getTableSpace()) &&
                StringUtils.isNotEmpty(features.createUserDefaultTableSpaceSqlString) && !features.createUserDefaultTableSpaceSqlString.equals("NOT_SUPPORT"))
            buf.append(" ").append(features.createUserDefaultTableSpaceSqlString.replace("_TABLESPACE", dserModel.getTableSpace()));
        if (StringUtils.isNotEmpty(dserModel.getSpace()) &&
                StringUtils.isNotEmpty(features.createUserDefaultSpaceSqlString) && !features.createUserDefaultSpaceSqlString.equals("NOT_SUPPORT")) {
            String userSpace = features.createUserDefaultSpaceSqlString.replace("_SPACE", dserModel.getSpace());
            userSpace = userSpace.replace("_TABLESPACE", dserModel.getTableSpace());
            buf.append(" ").append(userSpace);
        }
        return buf.toString();
    }

    protected static String dropUserSQLTemplate(Dialect dialect, UserModel dserModel) {
        StringBuilder buf = new StringBuilder();
        String userName = dserModel.getUserName();
        DDLFeatures features = dialect.ddlFeatures;
        if (StringUtils.isEmpty(features.dropUserSqlString) || features.dropUserSqlString.equals("NOT_SUPPORT"))
            return "";
        String createUserTemp = features.dropUserSqlString.replace("_USERNAME", userName);
        buf.append(createUserTemp);
        return buf.toString();
    }

    public static String[] UserGrantSQLTemplate(Dialect dialect, UserGrantModel userGrantModel) {
        List<String> resultList = new ArrayList<>();
        DDLFeatures features = dialect.ddlFeatures;
        String userGrantSqlString = features.userGrantSqlString;
        if (StringUtils.isNotEmpty(userGrantSqlString) && !userGrantSqlString.equals("NOT_SUPPORT"))
            if (Dialect.MySQL == dialect || Dialect.PostgreSQL == dialect) {
                userGrantSqlString = userGrantSqlString.replace("_DATABASE", userGrantModel.getDatabaseName());
                userGrantSqlString = userGrantSqlString.replace("_USERNAME", userGrantModel.getUserName());
                resultList.add(userGrantSqlString);
            } else if (Dialect.Oracle == dialect) {
                userGrantSqlString = userGrantSqlString.replace("_USERNAME", userGrantModel.getUserName());
                resultList.add(userGrantSqlString);
                if (StringUtils.isNotEmpty(features.userGrantByQuotaUnlimitedSqlString) && !features.userGrantByQuotaUnlimitedSqlString.equals("NOT_SUPPORT")) {
                    String userGrantSqlString_2 = features.userGrantByQuotaUnlimitedSqlString.replace("_USERNAME", userGrantModel.getUserName());
                    resultList.add(userGrantSqlString_2.replace("_TABLESPACE", userGrantModel.getTableSpace()));
                }
            }
        return resultList.<String>toArray(new String[resultList.size()]);
    }

    public static String createTableSpace(Dialect dialect, TableSpaceModel tableSpaceModel) {
        DDLFeatures features = dialect.ddlFeatures;
        if (StringUtils.isEmpty(features.createTableSpaceSqlString) || features.createTableSpaceSqlString.equals("NOT_SUPPORT"))
            return "";
        return getTableSpaceSql(tableSpaceModel, features, features.createTableSpaceSqlString);
    }

    public static String createDatabase(Dialect dialect, DatabaseModel databaseModel) {
        DDLFeatures features = dialect.ddlFeatures;
        if (StringUtils.isEmpty(features.createDataBaseSqlString) || features.createDataBaseSqlString.equals("NOT_SUPPORT"))
            return "";
        return getDatabaseSql(databaseModel, features, features.createDataBaseSqlString);
    }

    public static String dropDatabase(Dialect dialect, DatabaseModel databaseModel) {
        DDLFeatures features = dialect.ddlFeatures;
        if (StringUtils.isEmpty(features.dropDataBaseSqlString) || features.dropDataBaseSqlString.equals("NOT_SUPPORT"))
            return "";
        return getDatabaseSql(databaseModel, features, features.dropDataBaseSqlString);
    }

    public static String createSchema(Dialect dialect, SchemaModel schemaModel) {
        DDLFeatures features = dialect.ddlFeatures;
        if (StringUtils.isEmpty(features.createSchemaSqlString) || features.createSchemaSqlString.equals("NOT_SUPPORT"))
            return "";
        return getSchemaSql(schemaModel, features, features.createSchemaSqlString);
    }

    public static String dropSchema(Dialect dialect, SchemaModel schemaModel) {
        DDLFeatures features = dialect.ddlFeatures;
        if (StringUtils.isEmpty(features.dropSchemaSqlString) || features.dropSchemaSqlString.equals("NOT_SUPPORT"))
            return "";
        return getSchemaSql(schemaModel, features, features.dropSchemaSqlString);
    }

    public static String getSchemaSql(SchemaModel schemaModel, DDLFeatures features, String tableSpaceSqlTemp) {
        String tempSQL = tableSpaceSqlTemp.replace("_SCHEMA", schemaModel.getSchemaName());
        if (tempSQL.indexOf("_USERNAME") != -1)
            tempSQL = tempSQL.replace("_USERNAME", schemaModel.getUserName());
        return tempSQL;
    }

    public static String getDatabaseSql(DatabaseModel databaseModel, DDLFeatures features, String tableSpaceSqlTemp) {
        StringBuilder buf = new StringBuilder();
        buf.append(tableSpaceSqlTemp.replace("_DATABASE", databaseModel.getDatabaseName()));
        return buf.toString();
    }

    public static String alterTableSpace(Dialect dialect, TableSpaceModel tableSpaceModel) {
        DDLFeatures features = dialect.ddlFeatures;
        if (StringUtils.isEmpty(features.alterTableSpaceSqlString) || features.alterTableSpaceSqlString.equals("NOT_SUPPORT"))
            return "";
        return getTableSpaceSql(tableSpaceModel, features, features.alterTableSpaceSqlString);
    }

    public static String getTableSpaceSql(TableSpaceModel tableSpaceModel, DDLFeatures features, String tableSpaceSqlTemp) {
        StringBuilder buf = new StringBuilder();
        String tableSpaceName = tableSpaceModel.getTableSpaceName();
        String tableSpaceFile = tableSpaceModel.getTableSpaceFile();
        String tableSpaceSize = tableSpaceModel.getTableSpaceSize();
        String tableSpaceSql = tableSpaceSqlTemp.replace("_TABLESPACENAME", tableSpaceName);
        tableSpaceSql = tableSpaceSql.replace("_TABLESPACEFILE", tableSpaceFile);
        tableSpaceSql = tableSpaceSql.replace("_TABLESPACESIZE", tableSpaceSize);
        buf.append(tableSpaceSql);
        if (tableSpaceModel.isAutoextend() && StringUtils.isNotEmpty(tableSpaceModel.getAutoextendSize()) &&
                !StringUtils.isEmpty(features.tableSpaceAutoextendSqlString) && !features.tableSpaceAutoextendSqlString.equals("NOT_SUPPORT"))
            buf.append(" ").append(features.tableSpaceAutoextendSqlString.replace("_AUTOEXTENDSIZE", tableSpaceModel.getAutoextendSize()));
        if (StringUtils.isNotEmpty(tableSpaceModel.getMaxSize()) &&
                !StringUtils.isEmpty(features.tableSpaceMaxSizeSqlString) && !features.tableSpaceMaxSizeSqlString.equals("NOT_SUPPORT"))
            buf.append(" ").append(features.tableSpaceMaxSizeSqlString.replace("_MAXSIZE", tableSpaceModel.getMaxSize()));
        return buf.toString();
    }
}
