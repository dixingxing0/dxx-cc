/**
 * TransactionTool.java 9:38:29 AM Apr 25, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.tx;

import java.sql.SQLException;
import java.sql.Savepoint;

import org.apache.log4j.Logger;
import org.cc.core.CcException;

/**
 * <p>事务管理的工具类，此类使用的前提是必须已经开始了事务，否则抛出异常。</p>
 * <li> 在当前事务中再创建一个新的、完全独立的事务。
 * <p>见： {@link #begin()} ,{@link #commit()} ,{@link #rollback()}</p>
 * <li> 在当前事务中设置savepoint {@link #createSavepoint()}，
 * 可调用 {@link #rollbackToSavepoint(Savepoint)} 来回滚到创建savepoint时的状况。
 * 
 * @author dixingxing
 * @date Apr 25, 2012
 */
public final class TxUtils {
	private static final Logger LOG = Logger.getLogger(TxUtils.class);
	
	private static TxProvider p = new TxProvider();
	
	private TxUtils() {}
	/**
	 * 
	 * <p>如果当前没有开始事务则抛出异常</p>
	 *
	 */
	private static void check() {
		TxContext context = TxProvider.getContext();
		if(context == null) {
			throw new CcException("还没有开启任何事务，");
		}
	}
	
	/**
	 * 
	 * <p>开始一个新事务，此事务必须由{@link #commit()} 或者{@link #rollback()}来结束</p>
	 * <li><font color="red">注意：如果前面已经对表A进行更新(未commit/rollback)，新开启的事务再对表A进行更新操作，那么会产生数据库死锁。</font>
	 *
	 */
	public static void begin() {
		LOG.debug("--- 手动开始事务 ---");
		check();
		TxContext context = TxProvider.getContext();
		context.addInternalTx();
	}
	
	/**
	 * 
	 * <p>提交最近一次创建的事务。{@link #begin()}</p>
	 *
	 */
	public static void commit() {
		LOG.debug("--- 手动提交事务 ---");
		check();
		p.commitCurrent();
	}
	
	/**
	 * 
	 * <p>回滚最近一次创建的事务。{@link #begin()}</p>
	 *
	 */
	public static void rollback() {
		LOG.debug("--- 手动回滚事务 ---");
		check();
		p.rollbackCurrent();
	}
	
	/**
	 * 
	 * <p>创建一个savepoint</p>
	 *
	 * @see Connection#setSavepoint()
	 * @return
	 */
	public static Savepoint createSavepoint() {
		check();
		TxContext context = TxProvider.getContext();
		try {
			LOG.debug("--- 手动创建savepoint ---");
			return context.current().setSavepoint();
		} catch (SQLException e) {
			throw new CcException("创建savepoint异常",e);
		}
	}
	
	/**
	 * 
	 * <p>回滚至savepoint。</p>
	 *
	 * @see Connection#rollback(Savepoint)
	 * @param sp
	 */
	public static void rollbackToSavepoint(Savepoint sp) {
		check();
		TxContext context = TxProvider.getContext();
		try {
			LOG.debug("--- 回滚savepoint ---");
			context.current().rollback(sp);
		} catch (SQLException e) {
			throw new CcException("回滚savepoint异常",e);
		}
	}
}
