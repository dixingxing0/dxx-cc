/**
 * WebException.java 3:01:04 PM Feb 7, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.core.common;

/**
 * 异常类
 * 
 * @author dixingxing
 * @date Feb 7, 2012
 */
@SuppressWarnings("serial")
public class CcException extends RuntimeException {
	public CcException(){}
	
	public CcException(Throwable e) {
		super(e);
	}
	public CcException(String message, Throwable e) {
		super(message, e);
	}

	public CcException(String message) {
		super(message);
	}
}
