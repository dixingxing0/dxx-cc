/**
 * JavassistFactoryTest.java 10:47:43 AM May 9, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.tx.aop.javassit;

import static org.junit.Assert.assertEquals;

import org.cc.core.common.ReflectUtils;
import org.cc.tx.fortest.RoleService;
import org.junit.Before;
import org.junit.Test;

/**
 * <p></p>
 * 
 * @author dixingxing	
 * @date May 9, 2012
 */
public class JavassistFactoryTest {

	/**
	 * <p></p>
	 *
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link org.cc.tx.aop.AopFactory#proxy(java.lang.Object)}.
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
	 */
	@Test
	public void testProxy() throws SecurityException, NoSuchFieldException {
		RoleService original = new RoleService();
		String s = "this is field 1";
		original.setField1(s);
		RoleService obj1 = new JavassistFactory().proxy(original);
		assertEquals(s, ReflectUtils.get(obj1, obj1.getClass().getDeclaredField("field1")));
	}

}
