package com.tian.adapter.sql;

import com.tian.adapter.meta.scheme.*;

public interface DMLOperate {
    String toInsertDML(InsertModel paramInsertModel);

    String toQueryDQL(QueryModel paramQueryModel);

    String toUpdateDML(UpdateModel paramUpdateModel);

    String toDeleteDQL(DeleteModel paramDeleteModel);

    String toCountSpaceDML(DatabaseModel paramDatabaseModel);
}
