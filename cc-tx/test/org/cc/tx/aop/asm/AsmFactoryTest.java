/**
 * AsmFactoryTest.java 10:33:24 AM May 3, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.tx.aop.asm;

import static org.junit.Assert.assertEquals;

import org.cc.core.common.ReflectUtils;
import org.cc.tx.aop.AopFactory;
import org.cc.tx.fortest.RoleService;
import org.junit.Test;

/**
 * <p>
 * </p>
 * 
 * @author dixingxing
 * @date May 3, 2012
 */
public class AsmFactoryTest {

	/**
	 * Test method for
	 * {@link org.cc.tx.aop.asm.AsmFactory#getEnhancedClass(java.lang.Class)}.
	 */
	@Test
	public void testGetEnhancedClassClassOfT() {
		Class<?> cls = new AsmFactory().getEnhancedClass(RoleService.class);
		assertEquals(RoleService.class.getName() + AopFactory.SUFIX,cls.getName());
	}

	/**
	 * Test method for
	 * {@link org.cc.tx.aop.AopFactory#proxy(java.lang.Object)}.
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
	 */
	@Test
	public void testProxy() throws SecurityException, NoSuchFieldException {
		RoleService original = new RoleService();
		String s = "this is field 1";
		original.setField1(s);
		RoleService obj1 = new AsmFactory().proxy(original);
		assertEquals(s, ReflectUtils.get(obj1, obj1.getClass().getDeclaredField("field1")));
	}

}
