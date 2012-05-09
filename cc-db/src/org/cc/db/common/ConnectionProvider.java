/**
 * ConnectionProvider.java 12:00:04 PM Apr 11, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.db.common;

import java.sql.Connection;

/**
 * 
 * 
 * @author dixingxing	
 * @date Apr 11, 2012
 */
public interface ConnectionProvider {
	/**
	 * 获取数据库连接
	 * 
	 * @return
	 */
	Connection getConn() ;
	
	/**
	 * 释放数据库连接
	 * 
	 * @param conn
	 */
	void release(Connection conn);
}
