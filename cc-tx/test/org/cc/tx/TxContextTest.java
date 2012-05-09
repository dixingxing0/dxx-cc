/**
 * TxContextTest.java 10:30:58 AM May 9, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.tx;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.sql.Connection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * <p></p>
 * 
 * @author dixingxing	
 * @date May 9, 2012
 */
@RunWith(MockitoJUnitRunner.class)
public class TxContextTest {
	private Method method;
	
	private TxContext ctx;
	
	@Mock
	private Connection conn;
	/**
	 * <p></p>
	 *
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		method = UserServiceImpl.class.getMethod("print");
		ctx = new TxContext(method,conn);
	}

	/**
	 * Test method for {@link org.cc.tx.TxContext#TxContext(java.lang.reflect.Method, java.sql.Connection)}.
	 */
	@Test
	public void testTxContext() {
		assertTrue(ctx.current().getClass().getName().indexOf("Mockito") > 0);
	}

	/**
	 * Test method for {@link org.cc.tx.TxContext#isOwner(java.lang.reflect.Method)}.
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 */
	@Test
	public void testIsOwner() throws SecurityException, NoSuchMethodException {
		assertTrue(ctx.isOwner(method));
		Method method2 = UserService.class.getMethod("print");
		assertFalse(ctx.isOwner(method2));
	}

	/**
	 * Test method for {@link org.cc.tx.TxContext#push(java.sql.Connection)}.
	 */
	@Test
	public void testPush() {
		ctx.push(null);
		assertTrue(ctx.hasManualTx());
	}

	/**
	 * Test method for {@link org.cc.tx.TxContext#current()}.
	 */
	@Test
	public void testCurrent() {
		assertTrue(conn == ctx.current());
	}

	/**
	 * Test method for {@link org.cc.tx.TxContext#setCurrent(java.sql.Connection)}.
	 */
	@Test
	public void testSetCurrent() {
		ctx.setCurrent(null);
		assertTrue(ctx.current() == null);
	}

	/**
	 * Test method for {@link org.cc.tx.TxContext#isEmpty()}.
	 */
	@Test
	public void testIsEmpty() {
		assertFalse(ctx.isEmpty());
		ctx.next();
		assertTrue(ctx.isEmpty());
	}

	/**
	 * Test method for {@link org.cc.tx.TxContext#isLast()}.
	 */
	@Test
	public void testIsLast() {
		assertTrue(ctx.isLast());
	}

	/**
	 * Test method for {@link org.cc.tx.TxContext#contains(java.sql.Connection)}.
	 */
	@Test
	public void testContains() {
		assertTrue(ctx.contains(conn));
		assertFalse(ctx.contains(null));
	}

	/**
	 * Test method for {@link org.cc.tx.TxContext#addInternalTx()}.
	 */
	@Test
	public void testAddInternalTx() {
		assertTrue(ctx.current() != null);
		ctx.addInternalTx();
		assertTrue(ctx.current() == null);
	}
}
