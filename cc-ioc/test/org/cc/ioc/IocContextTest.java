/**
 * IocContextTest.java 4:58:20 PM Apr 7, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.ioc;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * 
 * @author dixingxing	
 * @date Apr 7, 2012
 */
public class IocContextTest {

	/**
	 * 
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		IocContext.init();
	}
	
	@After
	public void tearDown() {
		IocContext.clear();
	}

	/**
	 * Test method for {@link org.cc.ioc.IocContext#init()}.
	 */
	@Test
	public void testInit() {
		IocContext ic = IocContext.get(IocContext.class);
		// 没有标记 ioc 也没有属性需要ioc 
		assertNull(ic);
		
		IocContext ic2 = IocContext.get(IocContext.class);
		assertTrue(ic == ic2);
		
		assertNotNull(IocContext.get(ProductDetail.class));
		assertNotNull(IocContext.get(Product.class));
		assertNotNull(IocContext.get(Order.class));
	}

	/**
	 * Test method for {@link org.cc.ioc.IocContext#get(java.lang.Class)}.
	 */
	@Test
	public void testGet() {
		Order o = IocContext.get(Order.class);
		assertNotNull(o);
		assertNotNull(o.getProduct());
	}
	
	/**
	 * Test method for {@link org.cc.ioc.IocContext#clear()}.
	 */
	@Test
	public void testClear() {
		IocContext.clear();
		assertNull(IocContext.get(IocContext.class));
	}
	
}
