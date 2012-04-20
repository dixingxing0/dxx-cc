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
 * 
 * @author dixingxing
 * @date Apr 12, 2012
 */
public class TransactionProvider implements Provider {
	private static final Logger LOG = Logger
			.getLogger(TransactionProvider.class);

	private static ThreadLocal<Map<Method, Connection>> holder = new ThreadLocal<Map<Method, Connection>>();

	public static boolean noTransaction() {
		return getMap().values().isEmpty();
	}
	
	public Connection getConnection() {
		return getConn();
	}
	
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


	private static Map<Method, Connection> getMap() {
		Map<Method, Connection> map = holder.get();
		if (map == null) {
			map = new HashMap<Method, Connection>();
			holder.set(map);
		}
		return map;
	}

}
