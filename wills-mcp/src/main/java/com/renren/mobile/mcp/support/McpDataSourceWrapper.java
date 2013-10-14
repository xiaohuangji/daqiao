///**
// * $Id: McpDataSourceWrapper.java 2880 2013-01-21 07:23:49Z wei.cheng3 $
// * Copyright 2009-2012 Oak Pacific Interactive. All rights reserved.
// */
//package com.renren.mobile.mcp.support;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//
//import javax.sql.DataSource;
//
//import org.springframework.jdbc.datasource.AbstractDataSource;
//import org.springframework.transaction.support.TransactionSynchronizationManager;
//
//import com.xiaonei.xce.XceAdapter;
//
//public class McpDataSourceWrapper extends AbstractDataSource {
//
//    private String bizName;
//
//    @Override
//    public Connection getConnection() throws SQLException {
//        return getCurrentDataSource().getConnection();
//    }
//
//    @Override
//    public Connection getConnection(String username, String password) throws SQLException {
//        return getCurrentDataSource().getConnection(username, password);
//    }
//
//    public void setBizName(String bizName) {
//        this.bizName = bizName;
//    }
//
//    /**
//     * check if current transaction read only
//     * 
//     * @return
//     */
//    private boolean isCurrentTransactionReadOnly() {
//        return TransactionSynchronizationManager.isCurrentTransactionReadOnly();
//    }
//
//    /**
//     * get read data source
//     * 
//     * @return
//     * @throws SQLException
//     */
//    private DataSource getReadDataSource() throws SQLException {
//        return XceAdapter.getInstance().getReadDataSource(bizName);
//    }
//
//    /**
//     * get write data source
//     * 
//     * @return
//     * @throws SQLException
//     */
//    private DataSource getWriteDataSource() throws SQLException {
//        return XceAdapter.getInstance().getWriteDataSource(bizName);
//    }
//
//    /**
//     * get current data source
//     * 
//     * @return
//     * @throws SQLException
//     */
//    private DataSource getCurrentDataSource() throws SQLException {
//        return isCurrentTransactionReadOnly() ? getReadDataSource() : getWriteDataSource();
//    }
//
//}
