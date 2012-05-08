/**
 * AbstractConnectionProvider.java 3:42:50 PM Apr 12, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.db.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.cc.core.common.Exceptions;
import org.cc.db.transaction.Provider;

/**
 * <p>
 * 如果定义了transactionProvider则把connection的控制权交给transactionProvider
 * </p>
 * 
 * @author dixingxing
 * @date Apr 12, 2012
 */
public abstract class AbstractConnectionProvider implements ConnectionProvider {

	private static final Logger LOG = Logger
			.getLogger(AbstractConnectionProvider.class);

	/**
	 * 
	 * <p>设置事务隔离级别</p>
	 *
	 * @param conn
	 */
	public static void setTransactionIsolation(Connection conn) {
		try {
			if ("org.sqlite.Conn".equals(conn.getClass().getName())) {
				conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);

			} else if ("oracle.jdbc.driver.T4CConnection".equals(conn
					.getClass().getName())) {
				conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			}
		} catch (SQLException e) {
			Exceptions.uncheck(e);
		}
	}

	public Connection getConn() {
		Connection conn = null;
		Provider provider = JdbcConfig.getTransactionProvider();
		
		if (provider != null) {
			conn = provider.getConnection();
			if (conn == null) {
				conn = createConn();
				setTransactionIsolation(conn);
			}
		} else {
			conn = createConn();
			setTransactionIsolation(conn);
		}
		return conn;
	}

	public void release(Connection conn) {
		Provider provider = JdbcConfig.getTransactionProvider();
		// 如果当前数据库连接是托管在transactionProvider中的那么不做处理，transactionProvider最终会释放连接
		if (provider != null && provider.hasConn(conn)) {
			LOG.debug(String.format("conn %s 托管在transactionProvider中，不做释放",
					conn));
			return;
		}

		try {
			if(!conn.getAutoCommit()) {
				conn.commit();
			}
			LOG.debug(String.format("conn 提交并释放 %s", conn));
		} catch (SQLException e) {
			Exceptions.uncheck(e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				Exceptions.uncheck(e);
			}
		}

	}

	public abstract Connection createConn();

}
