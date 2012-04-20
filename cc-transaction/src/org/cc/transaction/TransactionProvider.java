/**
 * Provider.java 3:54:28 PM Apr 12, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.transaction;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.cc.core.common.Exceptions;
import org.cc.db.jdbc.ConnectionProvider;
import org.cc.db.transaction.Provider;

/**
 * 
 * <p>提供数据库事务的支持。</p>
 * <li>不支持事务的嵌套。</li>
 * <li>只管理定义了Transaction注解，并定义Ioc注解的类。</li>
 * <li>基于jdk的代理来实现，故一定要实现接口。</li>
 * <li>管理数据库连接</li>
 * 
 * @author dixingxing	
 * @date Apr 20, 2012
 */
public class TransactionProvider implements Provider {
	private static final Logger LOG = Logger
			.getLogger(TransactionProvider.class);
	/**保存数据库连接，目前不支持嵌套事务，所以其中最多只有一个数据库连接*/
	private static ThreadLocal<Map<Method, Connection>> holder = new ThreadLocal<Map<Method, Connection>>();

	/**
	 * 
	 * <p>当前{@link #holder}中没有数据库连接</p>
	 *
	 * @return
	 */
	public static boolean noTransaction() {
		return getMap().values().isEmpty();
	}
	
	public Connection getConnection() {
		return getConn();
	}
	
	/**
	 * 
	 * <p>获取{@link #holder}中的数据库连接</p>
	 *
	 * @return
	 */
	public static Connection getConn() {
		if( getMap().values().size() == 0) {
			return null;
		}
		// 取第一个连接TODO此处假设没有嵌套事务
		return getMap().values().iterator().next();
	}

	public boolean hasConn(Connection conn) {
		return getMap().containsValue(conn);
	}

	/**
	 * <p>
	 * 将从{@link ConnectionProvider} 获取的连接保存在当前线程中
	 * </p>
	 * <p>
	 * 如果当前线程没有定义Transaction注解，那么返回false，说明不对连接做管理。
	 * </p>
	 * 
	 * @param m
	 * @param conn
	 * @return
	 */
	public boolean putConnection(Method m, Connection conn) {
		Map<Method, Connection> map = holder.get();
		if (map == null) {
			map = new HashMap<Method, Connection>();
			holder.set(map);
		}

		if (m == null) {
			// LOG.debug(String.format("put connection failed %s ",conn));
			return false;
		}
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			Exceptions.uncheck(e);
		}
		// 定义transaction注解
		map.put(m, conn);
		return true;
	}

	/**
	 * 
	 * <p>回滚方法对应的数据库事务</p>
	 *
	 * @param m
	 */
	public static void rollback(Method m) {
		Connection conn = getConn();
		if (conn != null) {
			try {
				conn.rollback();
				LOG.debug(String.format("回滚方法%s.%s的事务", m.getDeclaringClass()
						.getName(), m.getName()));
			} catch (SQLException e) {
				Exceptions.uncheck(e);
			} finally {
				closeAndRemove(conn);
			}
		}
	}

	/**
	 * 
	 * <p>提交方法对应的数据库事务</p>
	 *
	 * @param m
	 */
	public static void commit(Method m) {
		Connection conn = getConn();
		if (conn != null) {
			try {
				conn.commit();
				LOG.debug(String.format("提交方法%s.%s的事务", m.getDeclaringClass()
						.getName(), m.getName()));
			} catch (SQLException e) {
				Exceptions.uncheck(e);
			} finally {
				closeAndRemove(conn);
			}
		}
	}

	/**
	 * 关闭连接并把连接从holder中移除
	 * 
	 * @param conn
	 */
	private static void closeAndRemove(Connection conn) {
		try {
			conn.close();
			remove();
			LOG.debug(String.format("释放数据库连接 %s",conn));
		} catch (SQLException e) {
			Exceptions.uncheck(e);
		}
	}

	/**
	 * 每个事务完成后要把map中的conn移除，。
	 * 
	 * @param m
	 */
	private static void remove() {
		getMap().clear();
	}


	/**
	 * 
	 * <p>返回{@link #holder}中的map，如果没有则创建一个</p>
	 *
	 * @return
	 */
	private static Map<Method, Connection> getMap() {
		Map<Method, Connection> map = holder.get();
		if (map == null) {
			map = new HashMap<Method, Connection>();
			holder.set(map);
		}
		return map;
	}

}
