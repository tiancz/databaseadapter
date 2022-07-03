package com.tian.load.handle;

import com.tian.db.handler.MapListHandler;
import com.tian.load.content.DataBaseTradeHandle;
import com.tian.load.param.req.DQLExecReqModel;
import com.tian.load.param.req.TradeExecReqModel;
import com.tian.load.param.resp.DQLExecRespModel;
import com.tian.load.param.resp.TradeExecRespModel;
import com.tian.utils.JDBCUtil;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DQLExecuteHandle extends DataBaseTradeHandle {
    protected TradeExecRespModel handle(TradeExecReqModel reqModel, Connection conn) throws SQLException {
        DQLExecReqModel dqlExecReqModel = (DQLExecReqModel)reqModel;
        DQLExecRespModel dqlExecRespModel = new DQLExecRespModel();
        if (StringUtils.isEmpty(dqlExecReqModel.getSql()))
            throw new SQLException("执行sql不存在");
        try {
            ResultSet resultSet = JDBCUtil.queryData(conn, dqlExecReqModel.getSql(), null);
            MapListHandler mapListHandler = new MapListHandler();
            dqlExecRespModel.setResult((List)mapListHandler.handle(resultSet));
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
        return (TradeExecRespModel)dqlExecRespModel;
    }
}
