/**
 * AbstractConnectionProvider.java 3:42:50 PM Apr 12, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.db.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.cc.db.transaction.Provider;

/**
 * <p>如果定义了transactionProvider则把connection的控制权交给transactionProvider</p>
 * 
 * @author dixingxing	
 * @date Apr 12, 2012
 */
public abstract class AbstractConnectionProvider implements ConnectionProvider{

	private static final Logger LOG = Logger.getLogger(AbstractConnectionProvider.class);
	
	public Connection getConn() {
		try {
			Connection conn = null;
			Provider provider = JdbcConfig.getTransactionProvider();
			if(provider != null) {
				conn = provider.getConnection();
				if(conn == null) {
					conn = createConn();
					if(!provider.putConnection(conn)) {
//						conn.setAutoCommit(false);
					}
				}
			} else {
				conn = createConn();
//				conn.setAutoCommit(false);
			}
			
			return conn;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	public void release(Connection conn) {
		Provider provider = JdbcConfig.getTransactionProvider();
		// 如果当前数据库连接是托管在transactionProvider中的那么不做处理，transactionProvider最终会释放连接
		if(provider != null && provider.hasConn(conn)) {
			LOG.debug(String.format("connection %s 托管在 transactionProvider中，不做释放",conn));
			return;
		}
		
		try {
			conn.commit();
			LOG.debug(String.format("提交并释放 connection %s",conn));
//			conn.rollback();
//			LOG.debug(String.format("回滚并释放 connection %s",conn));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		
	}



	public abstract Connection createConn();
	
}
