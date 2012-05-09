/**
 * ClassesTest.java 10:12:36 AM May 9, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.tx.aop.javassit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * <p></p>
 * 
 * @author dixingxing	
 * @date May 9, 2012
 */
public class ClassesTest {

	/**
	 * Test method for {@link org.cc.tx.aop.javassit.Classes#getMethodParamNames(java.lang.Class, java.lang.String, java.lang.Class<?>[])}.
	 */
	@Test
	public void testGetMethodParamNamesClassOfQStringClassOfQArray() {
		String[] paramNames = Classes.getMethodParamNames(Classes.class, "foo",String.class);
		assertEquals("bar", paramNames[0]);
	}

	/**
	 * Test method for {@link org.cc.tx.aop.javassit.Classes#getMethodParamNames(java.lang.Class, java.lang.String)}.
	 */
	@Test
	public void testGetMethodParamNamesClassOfQString() {
		String[] paramNames = Classes.getMethodParamNames(Classes.class, "foo");
		assertEquals(0, paramNames.length);
		
	}

}
