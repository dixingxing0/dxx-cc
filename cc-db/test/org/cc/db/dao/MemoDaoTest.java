/**
 * MemoTest.java 9:38:50 AM Apr 11, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.db.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.util.List;

import org.cc.db.DbException;
import org.cc.db.jdbc.JdbcConfig;
import org.junit.Before;
import org.junit.Test;


/**
 * 
 * 
 * @author dixingxing
 * @date Apr 11, 2012
 */
public class MemoDaoTest {
	private MemoDao dao;

	/**
	 * 
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		JdbcConfig.init("jdbc.properties");
		dao = new MemoDao();
	}

	/**
	 * Test method for {@link org.cc.db.dao.Dao#query(java.lang.Long)}.
	 */
	@Test
	public void testQuery() {
		assertTrue(dao.query(1L).getName().equals("memo13"));
	}

	/**
	 * Test method for {@link org.cc.db.dao.Dao#insert()}.
	 */
	@Test
	public void testInsert() {
		Memo m = new Memo();
		m.setCreateTime("2012-04-11 00:00:00");
		m.setName("test insert");
		Long returnId = dao.insert(m);
		
		assertNotNull(m.getId()) ;
		assertNotNull(returnId);
		assertTrue(returnId == m.getId());
	}

	/**
	 * Test method for
	 * {@link org.cc.db.dao.Dao#queryList(java.lang.String, java.lang.Object[])}.
	 */
	@Test
	public void testQueryList() {
		List<Memo> memos = dao.queryList("select * from memo");
		assertTrue(memos.size() > 0);
	}

	/**
	 * Test method for
	 * {@link org.cc.db.dao.Dao#queryLong(java.lang.String, java.lang.Object[])}.
	 */
	@Test
	public void testQueryLong() {
		Long count = dao.queryLong("select count(*) from memo");
		assertTrue(count > 0);
	}

	@Test(expected = DbException.class)
	public void testQueryLongExeption() {
		dao.queryLong("select id from memo");
	}

	/**
	 * Test method for {@link org.cc.db.dao.Dao#update()}.
	 */
	@Test
	public void testUpdate() {
		Memo m1 = dao.query(1L);
		m1.setCreateTime(m1.getCreateTime());
		dao.update(m1);
	}

	/**
	 * Test method for {@link org.cc.db.dao.Dao#getConnection()}.
	 */
	@Test
	public void testGetConnection() {
		Connection conn = dao.getConnection();
		assertNotNull(conn);
	}
	
	public void testExecute() {
		dao.execute("update memo set name=? where id=?", "memo2",2L);
		Memo m2 = dao.query(2L);
		assertEquals(m2.getName(), "memo2");
	}

}
