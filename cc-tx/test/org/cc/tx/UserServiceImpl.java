/**
 * Impl.java 4:39:35 PM Apr 12, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.tx;

import org.cc.tx.Transactional;

/**
 * 
 * 
 * @author dixingxing	
 * @date Apr 12, 2012
 */
@Transactional
public class UserServiceImpl implements UserService{
	@Transactional
	public void print() {
		System.out.println("print");
	}
}
