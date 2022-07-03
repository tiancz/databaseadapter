package com.tian.adapter.sql;

import com.tian.adapter.meta.scheme.*;

public interface DDLOperate {
    String pagin(int paramInt1, int paramInt2, String paramString);

    String[] toCreateDDL(TableModel... paramVarArgs);

    String[] toDropDDL(TableModel... paramVarArgs);

    String dropTableDDL(String paramString);

    String translateToDDLType(Type paramType, Integer... paramVarArgs);

    String alterTableSpace(TableSpaceModel paramTableSpaceModel);

    String createDatabase(DatabaseModel paramDatabaseModel);

    String dropDatabase(DatabaseModel paramDatabaseModel);

    String createTableSpace(TableSpaceModel paramTableSpaceModel);

    String[] userGrant(UserGrantModel paramUserGrantModel);

    String createUser(UserModel paramUserModel);

    String dropUser(UserModel paramUserModel);

    String createSchema(SchemaModel paramSchemaModel);

    String dropSchema(SchemaModel paramSchemaModel);
}
