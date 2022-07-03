package com.tian.adapter.sql;

import com.tian.adapter.utils.StrUtils;

public class DDLFeatures {
    public static final String NOT_SUPPORT = "NOT_SUPPORT";

    protected String addColumnString;

    protected String addColumnSuffixString;

    protected String addForeignKeyConstraintString;

    protected String addFKeyRefPkeyString;

    protected String addPrimaryKeyConstraintString;

    protected String columnComment;

    protected String tableComment;

    protected String createCatalogCommand;

    protected String createMultisetTableString;

    protected String createPooledSequenceStrings;

    protected String createSchemaCommand;

    protected String createSequenceStrings;

    protected String createTableString;

    protected String currentSchemaCommand;

    protected String dropCatalogCommand;

    protected String dropForeignKeyString;

    protected String dropSchemaCommand;

    protected String dropSequenceStrings;

    protected String dropTableString;

    protected Boolean hasAlterTable;

    protected Boolean hasDataTypeInIdentityColumn;

    protected String identityColumnString;

    protected String identityColumnStringBigINT;

    protected String identitySelectString;

    protected String identitySelectStringBigINT;

    protected Boolean needDropConstraintsBeforeDropTable;

    protected String nullColumnString;

    protected Boolean requiresParensForTupleDistinctCounts;

    protected String selectSequenceNextValString;

    protected String sequenceNextValString;

    protected Boolean supportsColumnCheck;

    protected Boolean supportsCommentOn;

    protected Boolean supportsIdentityColumns;

    protected Boolean supportsIfExistsAfterConstraintName;

    protected String addTableColumnString;

    protected String delTableColumnString;

    protected String addTableIndexString;

    protected String delTableIndexString;

    protected String addTablePrimaryKeyString;

    protected String delTablePrimaryKeyString;

    protected Boolean supportsIfExistsAfterTableName;

    protected Boolean supportsIfExistsBeforeConstraintName;

    protected Boolean supportsIfExistsBeforeTableName;

    protected Boolean supportsInsertSelectIdentity;

    protected Boolean supportsPooledSequences;

    protected Boolean supportsSequences;

    protected Boolean supportsTableCheck;

    protected String tableTypeString;

    protected String insertTableDataSQLString;

    protected String insertTableManyDataSQLString;

    protected String selectTableDataByIdSQLString;

    protected String updateTableDataSQLString;

    protected String selectTableDataListSQLString;

    protected String deleteTableDataSQLString;

    protected String createTableSpaceSqlString;

    protected String alterTableSpaceSqlString;

    protected String tableSpaceAutoextendSqlString;

    protected String tableSpaceMaxSizeSqlString;

    protected String createUserSqlString;

    protected String dropUserSqlString;

    protected String createUserDefaultTableSpaceSqlString;

    protected String createUserDefaultSpaceSqlString;

    protected String userGrantSqlString;

    protected String userGrantByUnLimitedTableSpaceSqlString;

    protected String userGrantByQuotaUnlimitedSqlString;

    protected String createDataBaseSqlString;

    protected String dropDataBaseSqlString;

    protected String countSpaceSQLString;

    protected String createSchemaSqlString;

    protected String dropSchemaSqlString;

    public static boolean isValidDDLTemplate(String featureValue) {
        return (!StrUtils.isEmpty(featureValue) && !"NOT_SUPPORT".equals(featureValue));
    }

    public boolean supportBasicOrPooledSequence() {
        return (this.supportsSequences.booleanValue() || this.supportsPooledSequences.booleanValue());
    }

    protected static void initDDLFeatures(Dialect dia, DDLFeatures ddl) {
        ddl.addColumnString = "add column";
        ddl.addColumnSuffixString = "";
        ddl.addFKeyRefPkeyString = " add constraint _FKEYNAME foreign key (_FK1, _FK2) references _REFTABLE (_REF1, _REF2)";
        ddl.addForeignKeyConstraintString = " add constraint _FKEYNAME foreign key (_FK1, _FK2) references _REFTABLE (_REF1, _REF2)";
        ddl.addPrimaryKeyConstraintString = " add constraint _PKEYNAME primary key ";
        ddl.columnComment = " comment '_COMMENT'";
        ddl.tableComment = " comment='_COMMENT'";
        ddl.createCatalogCommand = "create database _CATALOGNAME";
        ddl.createMultisetTableString = "create table";
        ddl.createPooledSequenceStrings = "NOT_SUPPORT";
        ddl.createSchemaCommand = "NOT_SUPPORT";
        ddl.createSequenceStrings = "NOT_SUPPORT";
        ddl.createTableString = "create table";
        ddl.currentSchemaCommand = "NOT_SUPPORT";
        ddl.dropCatalogCommand = "drop database _CATALOGNAME";
        ddl.dropForeignKeyString = " drop foreign key ";
        ddl.dropSchemaCommand = "NOT_SUPPORT";
        ddl.dropSequenceStrings = "NOT_SUPPORT";
        ddl.dropTableString = "drop table if exists _TABLENAME";
        ddl.hasAlterTable = Boolean.valueOf(true);
        ddl.hasDataTypeInIdentityColumn = Boolean.valueOf(true);
        ddl.identityColumnString = "not null auto_increment";
        ddl.identityColumnStringBigINT = "not null auto_increment";
        ddl.identitySelectString = "select last_insert_id()";
        ddl.identitySelectStringBigINT = "select last_insert_id()";
        ddl.needDropConstraintsBeforeDropTable = Boolean.valueOf(true);
        ddl.nullColumnString = "";
        ddl.requiresParensForTupleDistinctCounts = Boolean.valueOf(false);
        ddl.selectSequenceNextValString = "NOT_SUPPORT";
        ddl.sequenceNextValString = "NOT_SUPPORT";
        ddl.supportsColumnCheck = Boolean.valueOf(false);
        ddl.supportsCommentOn = Boolean.valueOf(false);
        ddl.supportsIdentityColumns = Boolean.valueOf(true);
        ddl.supportsIfExistsAfterConstraintName = Boolean.valueOf(false);
        ddl.supportsIfExistsBeforeConstraintName = Boolean.valueOf(false);
        ddl.supportsPooledSequences = Boolean.valueOf(false);
        ddl.supportsSequences = Boolean.valueOf(false);
        ddl.supportsTableCheck = Boolean.valueOf(false);
        ddl.tableTypeString = " engine=InnoDB";
        ddl.addTableColumnString = "alter table";
        ddl.delTableColumnString = "alter table _TABLENAME drop column _COLUMNNAME";
        ddl.addTableIndexString = "create index _INDEXNAME using btree on _TABLENANE";
        ddl.delTableIndexString = "alter table _TABLENAME drop index _INDEXNAME";
        ddl.addTablePrimaryKeyString = "alter table _TABLENAME add constraint _PRIMARYKEY primary key";
        ddl.delTablePrimaryKeyString = "alter table _TABLENAME drop primary key";
        ddl.insertTableDataSQLString = "insert into _TABLENAME (_COLUMN) values (_VALUE)";
        ddl.insertTableManyDataSQLString = "insert into _TABLENAME (_COLUMN) values _VALUES";
        ddl.selectTableDataByIdSQLString = "select _COLUMN from _TABLENAME where id=_ID";
        ddl.updateTableDataSQLString = "update _TABLENAME set _VALUE _WHERE";
        ddl.selectTableDataListSQLString = "select _COLUMN from _TABLENAME";
        ddl.deleteTableDataSQLString = "delete from _TABLENAME _WHERE";
        ddl.createTableSpaceSqlString = "NOT_SUPPORT";
        ddl.tableSpaceAutoextendSqlString = "NOT_SUPPORT";
        ddl.tableSpaceMaxSizeSqlString = "NOT_SUPPORT";
        ddl.alterTableSpaceSqlString = "NOT_SUPPORT";
        ddl.createUserSqlString = "create user '_USERNAME'@'%' identified by '_PASSWORD'";
        ddl.dropUserSqlString = "drop user _USERNAME";
        ddl.userGrantSqlString = "grant all privileges on _DATABASE.* to '_USERNAME'@'%'";
        ddl.createDataBaseSqlString = "create database _DATABASE";
        ddl.dropDataBaseSqlString = "drop database _DATABASE";
        ddl.countSpaceSQLString = "select round(sum(DATA_LENGTH/1024/1024),4) as space from information_schema.`TABLES` where TABLE_SCHEMA = '_DATABASE'";
        ddl.createSchemaSqlString = "NOT_SUPPORT";
        ddl.dropSchemaSqlString = "NOT_SUPPORT";
        switch (dia) {
            case Oracle:
                ddl.tableTypeString = "";
                ddl.columnComment = "";
                ddl.tableComment = "";
                ddl.supportsCommentOn = Boolean.valueOf(true);
                ddl.createDataBaseSqlString = "NOT_SUPPORT";
                ddl.dropDataBaseSqlString = "NOT_SUPPORT";
                ddl.dropTableString = "drop table _TABLENAME";
                ddl.createTableSpaceSqlString = "create tablespace _TABLESPACENAME datafile '_TABLESPACEFILE' size _TABLESPACESIZE";
                ddl.alterTableSpaceSqlString = "alter tablespace _TABLESPACENAME add datafile  '_TABLESPACEFILE' size _TABLESPACESIZE";
                ddl.tableSpaceAutoextendSqlString = "autoextend on next _AUTOEXTENDSIZE";
                ddl.tableSpaceMaxSizeSqlString = "maxsize _MAXSIZE";
                ddl.createUserSqlString = "create user _USERNAME identified by _PASSWORD";
                ddl.dropUserSqlString = "drop user _USERNAME cascade";
                ddl.createUserDefaultTableSpaceSqlString = "default tablespace _TABLESPACE";
                ddl.createUserDefaultSpaceSqlString = "quota _SPACE on _TABLESPACE";
                ddl.userGrantSqlString = "grant connect, resource to _USERNAME";
                ddl.userGrantByUnLimitedTableSpaceSqlString = "revoke unlimited tablespace from _USERNAME";
                ddl.userGrantByQuotaUnlimitedSqlString = "alter user _USERNAME quota unlimited on _TABLESPACE";
                ddl.countSpaceSQLString = "select round(sum(bytes)/1024/1024,4) as space from user_segments where segment_type = 'TABLE'";
                ddl.createSchemaSqlString = "NOT_SUPPORT";
                ddl.dropSchemaSqlString = "NOT_SUPPORT";
                break;
            case PostgreSQL:
                ddl.tableTypeString = "";
                ddl.columnComment = "";
                ddl.tableComment = "";
                ddl.supportsCommentOn = Boolean.valueOf(true);
                ddl.createUserSqlString = "create role _USERNAME nosuperuser nocreatedb nocreaterole noinherit login password '_PASSWORD'";
                ddl.userGrantSqlString = "grant all privileges on database _DATABASE to _USERNAME";
                ddl.countSpaceSQLString = "select pg_database.datname, pg_database_size(pg_database.datname) as space from pg_database where tatname = '_DATABASE'";
                ddl.createSchemaSqlString = "create schema _SCHEMA authorization _USERNAME";
                ddl.dropSchemaSqlString = "drop schema _SCHEMA";
                break;
        }
    }
}
