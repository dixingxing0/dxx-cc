/**
 * Exceptions.java 2:41:38 PM Apr 20, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.core.common;

import org.cc.core.CcException;

/**
 * uncheck Exception
 * 
 * @author dixingxing
 * @date Apr 20, 2012
 */
public final class Exceptions {
	private Exceptions() {
	}

	/**
	 * throw new CcException(e)
	 * 
	 * @param e
	 */
	public static void uncheck(Exception e) {
		throw new CcException(e);
	}
}
