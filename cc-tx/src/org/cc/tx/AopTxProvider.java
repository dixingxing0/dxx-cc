/**
 * Provider.java 3:54:28 PM Apr 12, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.tx;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.cc.core.common.Exceptions;
import org.cc.db.common.TxProvider;

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
public class AopTxProvider implements TxProvider{
	private static final Logger LOG = Logger.getLogger(AopTxProvider.class);
	/**保存数据库连接，目前不支持嵌套事务，所以其中最多只有一个数据库连接*/
	private static ThreadLocal<TxContext> holder = new ThreadLocal<TxContext>();
	
	/**
	 * 
	 * <p>判断是否需要开始一个新事务，以下两种情况会创建新事物</p>
	 * <li> 1.进入最外层定义了{@link Transactional}的方法。
	 * <li> 2.手动开始事务后，进入第一个定义了{@link Transactional}的方法。
	 *
	 * @return
	 */
	public static boolean needToNewTx() {
		return holder.get() == null || holder.get().current() == null;
	}
	
	/**
	 * <p>获取{@link #holder}中的数据库连接</p>
	 * 
	 * @return
	 * @see org.cc.db.common.TxProvider#getConnection()
	 */
	public Connection getConnection() {
		TxContext context = getContext();
		if(context == null || context.isEmpty()) {
			return null;
		}
		// 取第一个连接TODO此处假设没有嵌套事务
		return context.current();
	}
	
	/**
	 * 
	 * <p>holder.get()</p>
	 *
	 * @return
	 */
	protected static TxContext getContext() {
		return holder.get();
	}
	
	public boolean hasConn(Connection conn) {
		TxContext context = getContext();
		return context != null && context.contains(conn);
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
		LOG.debug(String.format("开始事务(%s - %s)：获取新连接 %s", m.getName(),m.getDeclaringClass(),conn));
		TxContext context = holder.get();
		if (context == null) {
			context = new TxContext(m,conn);
			holder.set(context);
		} else if(context.current() == null){
			context.setCurrent(conn);
		}
		

		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			Exceptions.uncheck(e);
		}
		return true;
	}

	/**
	 * 
	 * <p>回滚方法对应的数据库事务</p>
	 *
	 * @param m
	 */
	public void rollback(Method m) {
		Connection conn = getConnection();
		if (conn != null) {
			try {
				LOG.debug(String.format("结束事务(%s - %s)：开始回滚事务", m.getName(),m.getDeclaringClass()));
				conn.rollback();
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
	public void commit(Method m) {
		Connection conn = getConnection();
		if (conn != null) {
			try {
				LOG.debug(String.format("结束事务(%s - %s)：开始提交事务",m.getName(), m.getDeclaringClass()));
				conn.commit();
			} catch (SQLException e) {
				Exceptions.uncheck(e);
			} finally {
				closeAndRemove(conn);
			}
		}
	}
	
	/**
	 * 
	 * <p>手动提交事务</p>
	 *
	 */
	protected void commitCurrent() {
		if(!hasManualTx()) {
			return;
		}
		Connection conn = getConnection();
		if (conn != null) {
			try {
				conn.commit();
				LOG.debug(String.format(" ** 手动提交事务:%s",conn));
			} catch (SQLException e) {
				Exceptions.uncheck(e);
			} finally {
				closeAndRemove(conn);
			}
		}
	}
	
	/**
	 * 
	 * <p>判断是否有手动开始的事务</p>
	 *
	 * @return
	 */
	private static boolean hasManualTx() {
		TxContext context = holder.get();
		if(context == null || !context.hasManualTx()) {
			LOG.warn(String.format("没有手动开始的事务，不能手动进行commit/rollback"));
			return false;
		}
		return true;
	}
	
	
	/**
	 * 
	 * <p>手动回滚事务</p>
	 *
	 */
	protected void rollbackCurrent() {
		if(!hasManualTx()) {
			return;
		}
		Connection conn = getConnection();
		if (conn != null) {
			try {
				conn.commit();
				LOG.debug(String.format(" ** 手动回滚事务%s",conn));
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
	private void closeAndRemove(Connection conn) {
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
	private void remove() {
		TxContext context = holder.get();
		if(context == null) {
			return;
		}
		boolean isLast  = context.isLast();
		if(!context.isEmpty()) {
			context.next();
		}
		if(isLast){
			LOG.debug("TxContext最后一个数据库连接已经被移除，清空TxContext");
			holder.set(null);
		}
	}
}
