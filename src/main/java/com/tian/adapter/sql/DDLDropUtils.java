package com.tian.adapter.sql;

import com.tian.adapter.meta.scheme.TableModel;

import java.util.ArrayList;
import java.util.List;

public class DDLDropUtils {
    public static String[] toDropDDL(Dialect dialect, TableModel... tables) {
        List<String> stringResultList = new ArrayList<>();
        for (TableModel table : tables)
            transferTableToStringList(dialect, table, stringResultList);
        String[] result = stringResultList.<String>toArray(new String[stringResultList.size()]);
        return result;
    }

    private static void transferTableToStringList(Dialect dialect, TableModel t, List<String> objectResultList) {
        StringBuilder buf = new StringBuilder();
        String tableName = t.getTableName();
        buf.append(dialect.dropTableDDL(tableName));
        objectResultList.add(buf.toString());
    }
}
