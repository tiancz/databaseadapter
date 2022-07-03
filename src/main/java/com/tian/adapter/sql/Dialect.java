package com.tian.adapter.sql;

import com.alibaba.fastjson.JSON;
import com.tian.adapter.exception.DialectException;
import com.tian.adapter.meta.scheme.*;
import com.tian.adapter.utils.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.EnumMap;
import java.util.Map;

public enum Dialect implements DDLOperate, DMLOperate {
    MySQL, Oracle, PostgreSQL;

    Dialect() {
        this.typeMappings = new EnumMap<>(Type.class);
        this.ddlFeatures = new DDLFeatures();
        this.sqlTemplate = null;
        this.topLimitTemplate = null;
    }

    protected static final Logger logger;

    public static final String NOT_SUPPORT = "NOT_SUPPORT";

    private static final String SKIP_ROWS = "$SKIP_ROWS";

    private static final String PAGESIZE = "$PAGESIZE";

    private static final String TOTAL_ROWS = "$TOTAL_ROWS";

    private static final String SKIP_ROWS_PLUS1 = "$SKIP_ROWS_PLUS1";

    private static final String TOTAL_ROWS_PLUS1 = "$TOTAL_ROWS_PLUS1";

    private static final String DISTINCT_TAG = "($DISTINCT)";

    protected final Map<Type, String> typeMappings;

    protected final DDLFeatures ddlFeatures;

    private String sqlTemplate;

    private String topLimitTemplate;

    static {
        logger = LoggerFactory.getLogger(Dialect.class);
        for (Dialect d : values()) {
            d.sqlTemplate = DialectUtils.initializePaginSQLTemplate(d);
            d.topLimitTemplate = DialectUtils.initializeTopLimitSqlTemplate(d);
            DDLFeatures.initDDLFeatures(d, d.ddlFeatures);
        }
        DialectUtils.initTypeMappings();
    }

    public static Dialect guessDialect(String databaseName, Object... majorVersionMinorVersion) {
        return DialectUtils.guessDialect(databaseName, majorVersionMinorVersion);
    }

    public static Dialect guessDialect(Connection connection) {
        return DialectUtils.guessDialect(connection);
    }

    public static Dialect guessDialect(DataSource datasource) {
        return DialectUtils.guessDialect(datasource);
    }

    public String pagin(int pageNumber, int pageSize, String sql) {
        String result = null;
        DialectException.assureNotNull(sql, new String[] { "sql string can not be null" });
        String trimedSql = sql.trim();
        DialectException.assureNotEmpty(trimedSql, new String[] { "sql string can not be empty" });
        if (!StrUtils.startsWithIgnoreCase(trimedSql, "select "))
            return (String)DialectException.throwEX("SQL should start with \"select \".");
        String body = trimedSql.substring(7).trim();
        DialectException.assureNotEmpty(body, new String[] { "SQL body can not be empty" });
        int skipRows = (pageNumber - 1) * pageSize;
        int skipRowsPlus1 = skipRows + 1;
        int totalRows = pageNumber * pageSize;
        int totalRowsPlus1 = totalRows + 1;
        String useTemplate = this.sqlTemplate;
        if (skipRows == 0)
            useTemplate = this.topLimitTemplate;
        if ("NOT_SUPPORT".equals(useTemplate)) {
            if (!"NOT_SUPPORT".equals(this.topLimitTemplate))
                return
                        (String)DialectException.throwEX("Dialect \"" + this + "\" only support top limit SQL, for example: \"" +
                                aTopLimitSqlExample(this.topLimitTemplate) + "\"");
            return (String)DialectException.throwEX("Dialect \"" + this + "\" does not support physical pagination");
        }
        if (useTemplate.contains("($DISTINCT)"))
            if (!StrUtils.startsWithIgnoreCase(body, "distinct ")) {
                useTemplate = StrUtils.replace(useTemplate, "($DISTINCT)", "");
            } else {
                useTemplate = StrUtils.replace(useTemplate, "($DISTINCT)", "distinct");
                body = body.substring(9);
            }
        result = StrUtils.replaceIgnoreCase(useTemplate, "$SKIP_ROWS", String.valueOf(skipRows));
        result = StrUtils.replaceIgnoreCase(result, "$PAGESIZE", String.valueOf(pageSize));
        result = StrUtils.replaceIgnoreCase(result, "$TOTAL_ROWS", String.valueOf(totalRows));
        result = StrUtils.replaceIgnoreCase(result, "$SKIP_ROWS_PLUS1", String.valueOf(skipRowsPlus1));
        result = StrUtils.replaceIgnoreCase(result, "$TOTAL_ROWS_PLUS1", String.valueOf(totalRowsPlus1));
        result = StrUtils.replace(result, "$SQL", trimedSql);
        result = StrUtils.replace(result, "$BODY", body);
        return result;
    }

    private static String aTopLimitSqlExample(String template) {
        String result = StrUtils.replaceIgnoreCase(template, "$SQL", "select * from users order by userid");
        result = StrUtils.replaceIgnoreCase(result, "$BODY", "* from users order by userid");
        result = StrUtils.replaceIgnoreCase(result, " ($DISTINCT)", "");
        result = StrUtils.replaceIgnoreCase(result, "$SKIP_ROWS", "0");
        result = StrUtils.replaceIgnoreCase(result, "$PAGESIZE", "10");
        result = StrUtils.replaceIgnoreCase(result, "$TOTAL_ROWS", "10");
        return result;
    }

    public String[] toCreateDDL(TableModel... tables) {
        return DDLCreateUtils.toCreateDDL(this, tables);
    }

    public String[] toDropDDL(TableModel... tables) {
        return DDLDropUtils.toDropDDL(this, tables);
    }

    public String dropTableDDL(String tableName) {
        return this.ddlFeatures.dropTableString.replaceFirst("_TABLENAME", tableName);
    }

    public String translateToDDLType(Type type, Integer... lengths) {
        String value = this.typeMappings.get(type);
        if (StrUtils.isEmpty(value) || "N/A".equals(value) || "n/a".equals(value))
            DialectException.throwEX("Type \"" + type + "\" is not supported by dialect \"" + this + "\"");
        if (value.contains("|")) {
            String[] mappings = StrUtils.split("|", value);
            for (String mapping : mappings) {
                if (mapping.contains("<")) {
                    String[] limitType = StrUtils.split("<", mapping);
                    if (lengths.length > 0 && lengths[0].intValue() < Integer.parseInt(limitType[1]))
                        return putParamters(type, limitType[0], lengths);
                } else {
                    return putParamters(type, mapping, lengths);
                }
            }
        } else {
            if (value.contains("$"))
                return putParamters(type, value, lengths);
            return value;
        }
        return "";
    }

    private String putParamters(Type type, String value, Integer... lengths) {
        if (lengths.length < StrUtils.countMatches(value, '$'))
            DialectException.throwEX("In Dialect \"" + this + "\", Type \"" + type + "\" should have " +
                    StrUtils.countMatches(value, '$') + " parameters");
        int i = 0;
        String newValue = value;
        if (newValue.contains("$l"))
            newValue = StrUtils.replace(newValue, "$l", String.valueOf(lengths[i++]));
        if (newValue.contains("$p"))
            newValue = StrUtils.replace(newValue, "$p", String.valueOf(lengths[i++]));
        if (newValue.contains("$s"))
            newValue = StrUtils.replace(newValue, "$s", String.valueOf(lengths[i]));
        return newValue;
    }

    public String createUser(UserModel dserModel) {
        logger.info("数据库适配-创建用户:" + JSON.toJSON(dserModel));
        String result = DDLCreateUtils.createUserSQLTemplate(this, dserModel);
        logger.info("数据库适配-创建用户:" + result);
        return result;
    }

    public String dropUser(UserModel dserModel) {
        logger.info("数据库适配-删除用户:" + JSON.toJSON(dserModel));
        String result = DDLCreateUtils.dropUserSQLTemplate(this, dserModel);
        logger.info("数据库适配-删除用户:" + result);
        return result;
    }

    public String createSchema(SchemaModel schemaModel) {
        logger.info("数据库适配-创建数据库模式:" + JSON.toJSON(schemaModel));
        String result = DDLCreateUtils.createSchema(this, schemaModel);
        logger.info("数据库适配-创建数据库模式:" + result);
        return result;
    }

    public String dropSchema(SchemaModel schemaModel) {
        logger.info("数据库适配-删除数据库模式:" + JSON.toJSON(schemaModel));
        String result = DDLCreateUtils.dropSchema(this, schemaModel);
        logger.info("数据库适配-删除数据库模式:" + result);
        return result;
    }

    public String[] userGrant(UserGrantModel userGrantModel) {
        logger.info("数据库适配-用户授权:" + JSON.toJSON(userGrantModel));
        String[] result = DDLCreateUtils.UserGrantSQLTemplate(this, userGrantModel);
        logger.info("数据库适配-用户授权:" + result);
        return result;
    }

    public String createDatabase(DatabaseModel databaseModel) {
        logger.info("数据库适配-创建数据库:" + JSON.toJSON(databaseModel));
        String result = DDLCreateUtils.createDatabase(this, databaseModel);
        logger.info("数据库适配-创建数据库:" + result);
        return result;
    }

    public String dropDatabase(DatabaseModel databaseModel) {
        logger.info("数据库适配-删除数据库:" + JSON.toJSON(databaseModel));
        String result = DDLCreateUtils.dropDatabase(this, databaseModel);
        logger.info("数据库适配-删除数据库:" + result);
        return result;
    }

    public String createTableSpace(TableSpaceModel tableSpaceModel) {
        logger.info("数据库适配-创建表空间:" + JSON.toJSON(tableSpaceModel));
        String result = DDLCreateUtils.createTableSpace(this, tableSpaceModel);
        logger.info("数据库适配-创建表空间:" + result);
        return result;
    }

    public String alterTableSpace(TableSpaceModel tableSpaceModel) {
        logger.info("数据库适配-修改表空间:" + JSON.toJSON(tableSpaceModel));
        String result = DDLCreateUtils.alterTableSpace(this, tableSpaceModel);
        logger.info("数据库适配-修改表空间:" + result);
        return result;
    }

    public String toQueryDQL(QueryModel queryModel) {
        logger.info("数据库适配-生成查询语句:" + JSON.toJSON(queryModel));
        String result = DQLCreateUtils.toQueryDQL(this, queryModel);
        logger.info("数据库适配-生成查询语句:" + result);
        return result;
    }

    public String toInsertDML(InsertModel insertModel) {
        logger.info("数据库适配-生成插入语句:" + JSON.toJSON(insertModel));
        String result = DMLCreateUtils.toInsertDML(this, insertModel);
        logger.info("数据库适配-生成插入语句:" + result);
        return result;
    }

    public String toUpdateDML(UpdateModel updateModel) {
        logger.info("数据库适配-生成修改语句:" + JSON.toJSON(updateModel));
        String result = DMLCreateUtils.toUpdateDML(this, updateModel);
        logger.info("数据库适配-生成修改语句:" + result);
        return result;
    }

    public String toDeleteDQL(DeleteModel deleteModel) {
        logger.info("数据库适配-生成删除语句:" + JSON.toJSON(deleteModel));
        String result = DMLCreateUtils.toDeleteDML(this, deleteModel);
        logger.info("数据库适配-生成删除语句:" + result);
        return result;
    }

    public String toCountSpaceDML(DatabaseModel databaseModel) {
        logger.info("数据库适配-生成统计语句:" + JSON.toJSON(databaseModel));
        String result = DMLCreateUtils.toCountSpaceDML(this, databaseModel);
        logger.info("数据库适配-生成统计语句:" + result);
        return result;
    }
}
