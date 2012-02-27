/**
 * WebException.java 3:01:04 PM Feb 7, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package web;

/**
 * 
 * 
 * @author dixingxing
 * @date Feb 7, 2012
 */
@SuppressWarnings("serial")
public class WebException extends RuntimeException {
	public WebException(String message, Throwable e) {
		super(message, e);
	}

	public WebException(String message) {
		super(message);
	}
}
