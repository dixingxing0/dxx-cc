/**
 * WebException.java 3:01:04 PM Feb 7, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.core.db;

/**
 * db 异常类
 * 
 * @author dixingxing
 * @date Feb 7, 2012
 */
@SuppressWarnings("serial")
public class DbException extends RuntimeException {
	
	public DbException(String message, Throwable e) {
		super(message, e);
	}

	public DbException(String message) {
		super(message);
	}

	public DbException(Exception e) {
		super(e);
	}
}
