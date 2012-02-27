package service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import po.Ad;
import dao.Dao;

/**
 * dao 测试类
 * 
 * @author dixingxing
 * @date Jan 18, 2012
 */
public class AdManagerTest extends TestCase {
	private final static Logger logger = Logger.getLogger(AdManagerTest.class);

	private AdManager adManager = new AdManager();

	/**
	 * Test method for
	 * {@link dao.Dao#queryList(java.lang.String, java.lang.Class, java.lang.Object[])}
	 * .
	 */
	public void testQueryForListStringClassOfTObjectArray() {
		String sql = "select * from AD";
		List<Ad> ads = adManager.queryList(sql);
		logger.debug("测试多表查询映射成对象");
		sql = "select c.name as providerName,a.* from AD a ,PROVIDER c where a.provider_id = c.id";
		ads = adManager.queryList(sql);
		for (Ad ad : ads) {
			assertTrue("映射providerName属性失败！", ad.getProviderName() != null);
		}
	}

	/**
	 * Test method for
	 * {@link dao.Dao#query(java.lang.String, java.lang.Class, java.lang.Object[])}
	 * .
	 */
	public void testQuery() {
		Ad ad = adManager.query("select * from AD where name= ? ", "name123");
		assertTrue(ad.getName().equals("name123"));

	}

	public void testQueryForLong() {
		// 测试查询序列
		String s = "select id from ad where name='name123'";
		Long nextval = adManager.queryLong(s);
		assertTrue(nextval > 0L);

		s = "select count(*) from ad";
		Long count = adManager.queryLong(s);
		assertTrue(count >= 0L);
	}

	/**
	 * Test method for {@link dao.Dao#update(Connection, String, Object...)}.
	 * 
	 * @throws SQLException
	 */
	public void testUpdate() throws SQLException {
		int i = adManager.update(Dao.getConn(),
				"update AD set detail = ? where name = ?", "this is detail2",
				"name123");
		assertTrue("更新失败", i > 0);

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
		assertTrue("更新失败", i > 0);
	}

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
	 * Test method for {@link dao.Dao#queryList(java.lang.String)}.
	 */
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

	public void testChangeStateRollback() throws SQLException {
		adManager.changeStateRollback(102L, 2L);
		Ad ad = adManager.query("select * from ad where id = ?", 102L);
		assertTrue("事务回滚失败", ad.getState() == 1L);
	}

	public void testChangeStateCommit() throws SQLException {
		adManager.changeStateCommit(103L, 2L);
		Ad ad = adManager.query("select * from ad where id = ?", 103L);
		assertTrue("事务提交失败", ad.getState() == 2L);
	}

}
