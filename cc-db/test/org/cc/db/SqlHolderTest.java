/**
 * SqlHolderTest.java 4:02:14 PM Apr 1, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.db;

import static org.junit.Assert.assertTrue;

import org.cc.db.dao.SqlHolder;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * 
 * @author dixingxing	
 * @date Apr 1, 2012
 */
public class SqlHolderTest {
	private SqlHolder holder;
	private String sql = "select * from memo where name = ?";
	
	@Before
	public void setUp() {
		holder = new SqlHolder();
		holder.setSql(sql);
	}
	
	
	/**
	 * Test method for {@link org.cc.db.dao.SqlHolder#addParam(java.lang.Object)}.
	 */
	@Test
	public void testAddParam() {
		holder.addParam("name");
		assertTrue(holder.getParams().length == 1);
	}

	/**
	 * Test method for {@link org.cc.db.dao.SqlHolder#getParams()}.
	 */
	@Test
	public void testGetParams() {
		assertTrue(holder.getParams().length == 0);
	}

	/**
	 * Test method for {@link org.cc.db.dao.SqlHolder#getSql()}.
	 */
	@Test
	public void testGetSql() {
		assertTrue(holder.getSql().equals(sql));
	}

	/**
	 * Test method for {@link org.cc.db.dao.SqlHolder#setSql(java.lang.String)}.
	 */
	@Test
	public void testSetSql() {
		holder.setSql(sql);
		assertTrue(holder.getSql().equals(sql));
	}

}
