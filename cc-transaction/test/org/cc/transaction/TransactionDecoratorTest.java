/**
 * TransactionDecoratorTest.java 4:52:17 PM Apr 12, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.transaction;


import org.junit.Before;
import org.junit.Test;

/**
 * 
 * 
 * @author dixingxing	
 * @date Apr 12, 2012
 */
public class TransactionDecoratorTest {
	private TransactionDecorator decorator = new TransactionDecorator();
	/**
	 * 
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void testDoDecorate() {
		UserService us = new UserServiceImpl();
		us = decorator.doDecorator(us);
		us.print();
	}
	
	
}
