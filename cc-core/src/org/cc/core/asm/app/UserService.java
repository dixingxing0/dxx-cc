/**
 * Account.java 7:03:37 PM Apr 25, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.core.asm.app;

import javax.annotation.Generated;

import org.junit.After;

/**
 * <p></p>
 *  
 * @author dixingxing	
 * @date Apr 25, 2012
 */
@Deprecated
public class UserService {
	
	@After
    public void operation() {
    }
    
	@After
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
