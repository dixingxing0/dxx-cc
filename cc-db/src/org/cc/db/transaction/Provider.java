/**
 * TransactionProvider.java 3:08:21 PM Apr 12, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.db.transaction;

import java.sql.Connection;

import org.cc.db.jdbc.ConnectionProvider;

/**
 * <li>将从{@link ConnectionProvider} 获取的连接保存在当前线程中（如果当前线程没有定义Transaction注解，那么不保存连接）</li>
 * <li>从当前线程中获取数据库连接，保证同一事务中使用同一个连接</li>
 * 
 * @author dixingxing	
 * @date Apr 12, 2012
 */
public interface Provider {
	/**
	 * <p>从当前线程中获取数据库连接，保证同一事务中使用同一个连接</p>
	 * 
	 */
	Connection getConnection() ;
	
	/**
	 * 是否管理此连接
	 * 
	 * @param conn
	 * @return
	 */
	boolean hasConn(Connection conn);
}
