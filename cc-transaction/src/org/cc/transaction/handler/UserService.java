/**
 * Account.java 7:03:37 PM Apr 25, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.transaction.handler;

import org.cc.transaction.Transactional;

/**
 * <p></p>
 *  
 * @author dixingxing	
 * @date Apr 25, 2012
 */

public class UserService {
	
	@Transactional
    public void operation() {
    }
    
	@Transactional(readonly = true)
    public void run() {
    	operation();
    }
	
	public static boolean getResult() {
		return true;
	}
    
	public void testGetResult() {
		boolean result = getResult();
		System.out.println(result);
	}
    public static void main(String[] args) {
		new UserService().operation(); 
	}
}
