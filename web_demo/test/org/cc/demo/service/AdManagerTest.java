package org.cc.demo.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.cc.core.dao.Dao;
import org.cc.demo.po.Ad;
import org.cc.demo.service.AdManager;
import org.junit.Assert;
import org.junit.Test;


/**
 * dao 测试类
 * 
 * @author dixingxing
 * @date Jan 18, 2012
 */
public class AdManagerTest {
	private final static Logger logger = Logger.getLogger(AdManagerTest.class);

	private AdManager adManager = new AdManager();

	/**
	 * Test method for
	 * {@link org.cc.core.dao.Dao#queryList(java.lang.String, java.lang.Class, java.lang.Object[])}.
	 */
	@Test
	public void testQueryForListStringClassOfTObjectArray() {
		String sql = "select * from AD";
		List<Ad> ads = adManager.queryList(sql);
		logger.debug("测试多表查询映射成对象");
		sql = "select c.name as providerName,a.* from AD a ,PROVIDER c where a.provider_id = c.id";
		ads = adManager.queryList(sql);
		for (Ad ad : ads) {
			Assert.assertTrue("映射providerName属性失败！",
					ad.getProviderName() != null);
		}
	}

	/**
	 * Test method for
	 * {@link org.cc.core.dao.Dao#query(java.lang.String, java.lang.Class, java.lang.Object[])}.
	 */
	public void testQuery() {
		Ad ad = adManager.query("select * from AD where name= ? ", "name_tes3");
		Assert.assertTrue(ad.getName().equals("name_tes3"));

	}

	@Test
	public void testQueryForLong() {
		// 测试查询序列
		String s = "select ad_seq.nextval from dual";
		Long nextval = adManager.queryLong(s);
		Assert.assertTrue(nextval > 0L);

		s = "select count(*) from ad";
		Long count = adManager.queryLong(s);
		Assert.assertTrue(count >= 0L);
	}

	/**
	 * Test method for {@link org.cc.core.dao.Dao#update(Connection, String, Object...)}.
	 * 
	 * @throws SQLException
	 */
	@Test
	public void testUpdate() throws SQLException {
		int i = adManager.update(Dao.getConn(),
				"update AD set detail = ? where name = ?", "this is detail2",
				"name_tes3");
		Assert.assertTrue("更新失败", i > 0);

		Ad ad = new Ad();
		ad.setId(100001L);
		ad.setName("321");
		ad.setPosition(1L);
		ad.setState(1L);
		ad.setPublishTime(new Date());
		ad.setPublishMan("publishtest");
		ad.setUpdateTime(new Date());
		ad.setUpdateMan("updatetest");
		ad.setDetail("detail");
		ad.setProviderId(100001790735L);
		i = adManager.update(Dao.getConn(), ad);
		Assert.assertTrue("更新失败", i > 0);
	}

	@Test
	public void testInsert() {

		// String insert = "insert into ad
		// values(100,123,1,1,sysdate,'publishtest',sysdate,'publishtest','detail',100001790735)";

		Ad ad = new Ad();
		ad.setId(100001L);
		ad.setName("123");
		ad.setPosition(1L);
		ad.setState(1L);
		ad.setPublishTime(new Date());
		ad.setPublishMan("publishtest");
		ad.setUpdateTime(new Date());
		ad.setUpdateMan("updatetest");
		ad.setDetail("detail");
		ad.setProviderId(100001790735L);
		adManager.insert(Dao.getConn(), ad);
	}

	/**
	 * Test method for {@link org.cc.core.dao.Dao#queryList(java.lang.String)}.
	 */
	@Test
	public void testQueryForListString() {
		logger.debug("测试多表查询返回map");
		String sql = "select c.name as providerName,a.* from ad a ,PROVIDER c where a.provider_id = c.id";
		List<Map<String, Object>> list = adManager.queryMapList(sql);

		for (Map<String, Object> map : list) {
			Set<String> keySet = map.keySet();
			StringBuilder sb = new StringBuilder();
			for (String key : keySet) {
				sb.append(key).append(" : ").append(map.get(key)).append(",");
			}
			// logger.debug(sb.deleteCharAt(sb.length() - 1));
		}
	}

	@Test
	public void testChangeStateRollback() throws SQLException {
		adManager.changeStateRollback(102L, 2L);
		Ad ad = adManager.query("select * from ad where id = ?", 102L);
		Assert.assertTrue("事务回滚失败", ad.getState() == 1L);
	}

	@Test
	public void testChangeStateCommit() throws SQLException {
		adManager.changeStateCommit(103L, 2L);
		Ad ad = adManager.query("select * from ad where id = ?", 103L);
		Assert.assertTrue("事务提交失败", ad.getState() == 2L);
	}

}
