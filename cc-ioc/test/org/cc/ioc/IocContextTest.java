/**
 * IocContextTest.java 4:58:20 PM Apr 7, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.ioc;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.log4j.Logger;
import org.cc.ioc.annotation.Inject;
import org.cc.ioc.test.IocTestCase;
import org.junit.Test;

/**
 * 
 * 
 * @author dixingxing	
 * @date Apr 7, 2012
 */
public class IocContextTest extends IocTestCase{
	private static final Logger LOG = Logger.getLogger(IocContextTest.class);

	/** 可直接使用 */
	@Inject
	private Order orderInjected;
	
	/** 可直接使用 */
	@Inject
	private Product productInjected;
	
	/** 可直接使用 */
	@Inject
	private ProductDetail productDetailInjected;
	
	@Test
	public void testInject() {
		// Inject是否成功
		assertNotNull(orderInjected);
		assertTrue(orderInjected.getProduct() == productInjected);
		assertTrue(orderInjected.getProduct().getProductDetail() == productDetailInjected);
		
		// Inject的对象 和 IocContext.get 得到的对象应该是相同的 
		assertTrue(orderInjected == IocContext.get(Order.class));
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
		
		Order order = IocContext.get(Order.class);
		assertNotNull(order);
		
		User user = IocContext.get(User.class);
		User userImpl = IocContext.get(UserMan.class);
		assertNotNull(user);
		assertNotNull(userImpl);
		assertTrue(user == userImpl);
		
		assertTrue(order.getUser() == user);
		
		for(Class<?> key : IocContext.I_MAP.keySet()) {
			List<Class<?>> impls = IocContext.I_MAP.get(key);
			for(Class<?> impl : impls) {
				LOG.debug(key.getName() + ":" + impl.getName());
			}
		}
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
}
