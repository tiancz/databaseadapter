package com.tian.load.content;

import com.tian.enums.DataSourceEnum;
import com.tian.load.param.req.TradeExecReqModel;
import com.tian.load.param.resp.TradeExecRespModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DataBaseTradeHandle {
    private DynamicLoadContent dynamicLoadContent = DynamicLoadContent.getInstance();

    protected static final Logger logger = LoggerFactory.getLogger(DataBaseTradeHandle.class);

    public TradeExecRespModel execute(TradeExecReqModel reqModel) throws Exception {
        logger.info("数据库交易处理-开始" );
        DataSourceEnum dataSource = null;
        Connection conn = null;
        try {
            dataSource = DataSourceEnum.getDataSourceEnum(reqModel.getDatabaseType(), reqModel.getVersion());
            if (dataSource == null)
                throw new Exception("获取不到数据源配置");
            logger.info("数据库交易处理-数据源配置为：" + dataSource.getCode() + " " + dataSource.getVersion());
            this.dynamicLoadContent.dynamicLoad(dataSource);
            String url = String.format(dataSource.getDriverUrl(), new Object[] { reqModel.getIp(), reqModel.getPort(), reqModel.getDatabaseName() });
            conn = this.dynamicLoadContent.getConnection(dataSource, url, reqModel.getUsername(), reqModel.getPassword());
            logger.info("数据库交易处理-获取数据库连接成功-开始处理业务" );
            return handle(reqModel, conn);
        } catch (Exception e1) {
            logger.info("数据库交易处理-错误:" + e1.getMessage());
            throw new Exception(e1.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
                if (dataSource != null)
                    this.dynamicLoadContent.dynamicUnLoad(dataSource);
            } catch (SQLException e) {
                logger.info("数据库连接关闭-错误:" + e.getMessage());
                throw new Exception(e.getMessage());
            }
        }
    }

    protected abstract TradeExecRespModel handle(TradeExecReqModel paramTradeExecReqModel, Connection paramConnection) throws SQLException;
}
