/**
 * TranMethod.java 9:57:46 AM Apr 25, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.transaction;

import java.lang.reflect.Method;
import java.sql.Connection;

import org.apache.log4j.Logger;

/**
 * <p>非线程安全，需在threadLocal中存放</p>
 * <li>存放一个线程中的所有数据库连接
 * <li>提供一个简单的堆栈存放数据库连接，保证提交（回滚）的规则：LIFO
 * 
 * @author dixingxing	
 * @date Apr 25, 2012
 */
public class TranContext {
	private static final Logger LOG = Logger.getLogger(TranContext.class);
	
	/**
	 * 开启事务的方法，最终要由这个方法来结束事务，其他方法不能结束此事务。
	 */
	private Method method;
	
	/**
	 * 
	 * <p>简单堆栈LIFO</p>
	 * 
	 * @author dixingxing	
	 * @date Apr 28, 2012
	 */
	static class TranStack {
		private int top = -1;
		private Connection[] conns = new Connection[10];
		
		void push(Connection conn) {
			conns[++top] = conn;
			LOG.debug("push: " + top);
		}
		Connection pop() {
			LOG.debug("pop: " + top);
			top--;
			return conns[top + 1];
		}
		Connection current() {
			return top >=0 ? conns[top]:null;
		}
		
		void setCurrent(Connection conn) {
			if(top >=0) {
				conns[top] = conn;
			}
		} 
	}
	
	private TranStack tranStack;
	
	/**
	 * 
	 *  
	 * @param m 开启事务(aop before)的方法，作为主控方法，整个事务必须由此方法结束(aop after)。
	 * @param conn
	 */
	public TranContext(Method m,Connection conn) {
		this.method = m;
		tranStack = new TranStack();
		tranStack.push(conn);
	}
	
	/**
	 * 
	 * <p>判断是否是开启事务的方法，开启事务的方法的after方法要提交或回滚事务。</p>
	 *
	 * @param m
	 * @return
	 */
	public boolean isOwner(Method m) {
		return this.method.equals(m);
	} 
	
	/**
	 * 
	 * <p>压入新的数据库连接</p>
	 *
	 * @param conn
	 */
	public void push(Connection conn) {
		tranStack.push(conn);
	}
	
	/**
	 * 
	 * <p>获取当前的数据库连接</p>
	 *
	 * @return
	 */
	public Connection current() {
		return tranStack.current();
	}
	
	/**
	 * 
	 * <p>给刚刚开启的事务（{@link TxUtils#begin()}）一个新的数据库连接</p>
	 * 
	 *
	 * @param conn
	 */
	public void setCurrent(Connection conn){
		tranStack.setCurrent(conn);
	}
	
	/**
	 * 
	 * <p>没有数据库连接</p>
	 *
	 * @return
	 */
	public boolean isEmpty() {
		return tranStack.top == -1;
	}
	
	/**
	 * 
	 * <p>是最后一个连接</p>
	 *
	 * @return
	 */
	public boolean isLast(){
		return tranStack.top == 0;
	}

	/**
	 * <p>数据库连接是否正在由TranContext管理</p>
	 *
	 * @param conn
	 * @return
	 */
	public boolean contains(Connection conn) {
		for(Connection c : tranStack.conns) {
			if(c!= null && c == conn) {
				return true;
			}
		}
		return false;
	}

	/**
	 * <p>标记为开始新的子事务，添加空连接</p>
	 *
	 */
	public void addInternalTx() {
		push(null);
	}

	/**
	 * <p>使用下一个数据库连接</p>
	 *
	 */
	public void next() {
		tranStack.pop();
	}
	
	/**
	 * 判断是否有手动开始的事务
	 * <p></p>
	 *
	 */
	public boolean hasManualTx() {
		return tranStack.top > 0;
	}

}
