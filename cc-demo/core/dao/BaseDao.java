/**
 * BaseDao.java 7:24:12 PM Jan 17, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package dao;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.log4j.Logger;


/**
 * dbutils dao ����
 * 
 * @author dixingxing
 * @date Jan 17, 2012
 */
public class BaseDao<T> {
	private final static Logger LOG = Logger.getLogger(BaseDao.class);
	private static QueryRunner runner;

	protected static DataSource ds;

	static {
		ds = initDataSource();
		runner = new QueryRunner(ds);
	}

	/**
	 * ��ʼ��dhcp���Դ
	 * 
	 * @return
	 */
	private static synchronized DataSource initDataSource() {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		ds.setUsername("newhc");
		ds.setPassword("bfdds06fd");
		ds.setUrl("jdbc:oracle:thin:@192.168.20.203:1521:mktdb4");
		return ds;
	}

	private final static ScalarHandler scaleHandler = new ScalarHandler() {
		@Override
		public Object handle(ResultSet rs) throws SQLException {
			Object obj = super.handle(rs);
			if (obj instanceof BigInteger)
				return ((BigInteger) obj).longValue();

			return obj;
		}
	};

	/**
	 * 
	 * ʹ���Զ���� MyBeanProcessor
	 * 
	 * @see MyBeanProcessor
	 * @param clazz
	 * @return
	 */
	private BeanListHandler<T> getBeanListHandler(Class<T> clazz) {
		return new BeanListHandler<T>(clazz, new BasicRowProcessor(
				new MyBeanProcessor()));
	}

	/**
	 * ʹ���Զ���� MyBeanProcessor
	 * 
	 * @see MyBeanProcessor
	 * @param clazz
	 * @return
	 */
	private BeanHandler<T> getBeanHandler(Class<T> clazz) {
		return new BeanHandler<T>(clazz, new BasicRowProcessor(
				new MyBeanProcessor()));
	}

	/**
	 * ��Ĭ�ϵ����Դ�л�ȡһ����ݿ�l��
	 * 
	 * @return
	 */
	public static Connection getConn() {
		try {
			return ds.getConnection();
		} catch (Exception e) {
			LOG.error("��ȡ��ݿ�l��ʧ��!", e);
			return null;
		}
	}

	/**
	 * ��ӡ��־
	 * 
	 * @param sql
	 */
	private static void showSql(String sql) {
		if (true) {
			LOG.debug(sql);
		}
	}

	/**
	 * 
	 * ��ѯ�����б�
	 * 
	 * @param sql
	 * @param clazz
	 * @param params
	 * @return
	 */
	public List<T> queryList(String sql, Class<T> clazz, Object... params) {
		showSql(sql);
		try {
			return (List<T>) runner.query(sql, getBeanListHandler(clazz),
					params);
		} catch (SQLException e) {
			LOG.debug("��ѯʧ��", e);
			return new ArrayList<T>();
		}
	}

	/**
	 * ��ѯ���ص������
	 * 
	 * @param sql
	 * @param clazz
	 * @param params
	 * @return
	 */
	public T query(String sql, Class<T> clazz, Object... params) {
		showSql(sql);
		try {
			return (T) runner.query(sql, getBeanHandler(clazz), params);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ����long�����
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public Long queryLong(String sql, Object... params) {
		showSql(sql);
		try {
			Number n = (Number) runner.query(sql, scaleHandler, params);
			return n.longValue();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ִ��INSERT/UPDATE/DELETE���
	 * 
	 * @param conn
	 * @param sql
	 * @param params
	 * @return
	 */
	public int update(Connection conn, String sql, Object... params) {
		showSql(sql);
		try {
			return runner.update(conn, sql, params);
		} catch (SQLException e) {
			LOG.debug("���²���ʧ��", e);
			return 0;
		}
	}

	/**
	 * 
	 * ��ѯ�б�
	 * 
	 * @param sql
	 * @return Map<String, Object>
	 */
	public List<Map<String, Object>> queryList(String sql) {
		showSql(sql);
		try {
			List<Map<String, Object>> results = (List<Map<String, Object>>) runner
					.query(sql, new MapListHandler());
			return results;
		} catch (SQLException e) {
			LOG.error("��ѯʧ��", e);
			return new ArrayList<Map<String, Object>>();
		}
	}

}
