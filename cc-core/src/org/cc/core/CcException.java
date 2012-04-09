package org.cc.core;


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
