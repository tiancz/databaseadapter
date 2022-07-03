package com.tian.load.handle;

import com.tian.load.content.DataBaseTradeHandle;
import com.tian.load.param.req.DDLExecReqModel;
import com.tian.load.param.req.TradeExecReqModel;
import com.tian.load.param.resp.TradeExecRespModel;
import com.tian.utils.JDBCUtil;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;

public class DDLExecuteHandle extends DataBaseTradeHandle {
    protected TradeExecRespModel handle(TradeExecReqModel reqModel, Connection conn) throws SQLException {
        DDLExecReqModel ddlExecReqModel = (DDLExecReqModel)reqModel;
        if (StringUtils.isEmpty(ddlExecReqModel.getSql()))
            throw new SQLException("执行sql不存在");
        try {
            JDBCUtil.executeUpdate(conn, ddlExecReqModel.getSql(), null);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
        return null;
    }
}
