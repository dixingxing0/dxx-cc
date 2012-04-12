/**
 * AbstractConnectionProvider.java 3:42:50 PM Apr 12, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.db.jdbc;

import java.sql.Connection;

import org.cc.db.transaction.Provider;

/**
 * <p>如果定义了transactionProvider则把connection的控制权交给transactionProvider</p>
 * 
 * @author dixingxing	
 * @date Apr 12, 2012
 */
public abstract class AbstractConnectionProvider implements ConnectionProvider{

	public Connection getConn() {
		try {
			Connection conn = null;
			Provider provider = JdbcConfig.getTransactionProvider();
			if(provider != null) {
				conn = provider.getConnection();
				if(conn == null) {
					conn = createConn();
					provider.putConnection(conn);
				}
			} else {
				conn = createConn();
			}
			
			return conn;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public abstract Connection createConn();
	
}
