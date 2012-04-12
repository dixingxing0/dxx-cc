/**
 * SqlBuilderTest.java 3:52:18 PM Apr 1, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.db;

import org.apache.log4j.Logger;
import org.cc.db.SqlBuilderTest;
import org.cc.db.dao.SqlBuilder;
import org.cc.db.data.Child;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * 
 * @author dixingxing	
 * @date Apr 1, 2012
 */
public class SqlBuilderTest {
	private static final Logger LOG = Logger.getLogger(SqlBuilderTest.class);

	private Child child;
	
	@Before
	public void setUp() {
		child =new Child();
		child.setId(1L);
		child.setName("child name");
	}
	
	/**
	 * Test method for {@link org.cc.db.dao.SqlBuilder#buildInsert(java.lang.Object)}.
	 */
	@Test
	public void testBuildInsert() {
		LOG.debug(SqlBuilder.buildInsert(child));
	}

	/**
	 * Test method for {@link org.cc.db.dao.SqlBuilder#buildUpdate(java.lang.Object)}.
	 */
	@Test
	public void testBuildUpdate() {
		LOG.debug(SqlBuilder.buildUpdate(child));
	}

	/**
	 * Test method for {@link org.cc.db.dao.SqlBuilder#buildGetInsertId(java.lang.Object)}.
	 */
	@Test
	public void testBuildGetInsertId() {
		LOG.debug(SqlBuilder.buildGetInsertId(child.getClass()));
	}

	/**
	 * Test method for {@link org.cc.db.dao.SqlBuilder#pageSql(java.lang.String, int, int)}.
	 */
	@Test
	public void testPageSql() {
		LOG.debug(SqlBuilder.pageSql("select * from memo", 0, 9));
	}

	/**
	 * Test method for {@link org.cc.db.dao.SqlBuilder#countSql(java.lang.String)}.
	 */
	@Test
	public void testCountSql() {
		LOG.debug(SqlBuilder.countSql("select * from memo"));
	}

}
