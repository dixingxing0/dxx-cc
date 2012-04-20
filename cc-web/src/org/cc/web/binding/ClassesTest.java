/**
 * ClassesTest.java 7:57:27 PM Apr 20, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.web.binding;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

/**
 * <p></p>
 * 
 * @author dixingxing	
 * @date Apr 20, 2012
 */
public class ClassesTest {

	/**
	 * Test method for {@link org.cc.web.binding.Classes#getMethodParamNames(java.lang.Class, java.lang.String, java.lang.Class<?>[])}.
	 */
	@Test
	public void testGetMethodParamNamesClassOfQStringClassOfQArray() {
		// 匹配静态方法
		String[] paramNames = Classes.getMethodParamNames(Classes.class, "main",
				String[].class);
		assertArrayEquals(new String[]{"args"}, paramNames);
		// 匹配实例方法
		paramNames = Classes.getMethodParamNames(Classes.class, "foo", String.class);
		assertArrayEquals(new String[]{"bar"}, paramNames);
		// 自由匹配任一个重名方法
		paramNames = Classes.getMethodParamNames(Classes.class, "getMethodParamNames");
		assertArrayEquals(new String[]{"cm"}, paramNames);
		
	}

	/**
	 * Test method for {@link org.cc.web.binding.Classes#getMethodParamNames(java.lang.Class, java.lang.String)}.
	 */
	@Test
	public void testGetMethodParamNamesClassOfQString() {
		// 匹配特定签名的方法
		String[] paramNames = Classes.getMethodParamNames(Classes.class, "getMethodParamNames",
				Class.class, String.class);
		assertArrayEquals(new String[]{"clazz","method"}, paramNames);
	}

}
