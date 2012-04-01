/**
 * BaseDao.java 7:24:12 PM Jan 17, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.core.dao;

import java.lang.reflect.ParameterizedType;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.log4j.Logger;
import org.cc.core.common.Page;
import org.cc.core.common.ReflectUtils;
import org.cc.core.conf.JdbcConfig;



/**
 * dbutils dao 基类
 *
 * @author dixingxing
 * @date Jan 17, 2012
 */
public abstract class Dao<T> {
	public static final Logger LOG = Logger.getLogger(Dao.class);
	public static final String MESSAGE = "sql执行失败";
	
	
	/** 数据源 使用前需要初始化*/
	public static final DataSource DS;
	/** dbutils queryRunner 使用前需要初始化*/
	public static final QueryRunner QUERY_RUNNER;

	static {
		// 初始化数据源
		BasicDataSource ds1 = new BasicDataSource();
		ds1.setDriverClassName(JdbcConfig.getDriverClassName());
		ds1.setUrl(JdbcConfig.getUrl());
		
		DS = ds1;
		QUERY_RUNNER = new QueryRunner(DS);
	}
	
	private static final ScalarHandler SCALE_HANDLER = new ScalarHandler() {
		@Override
		public Object handle(ResultSet rs) throws SQLException {
			Object obj = super.handle(rs);
			if (obj instanceof BigInteger) {
				return ((BigInteger) obj).longValue();
			}
			return obj;
		}
	};
	
	/**
	 *
	 * 使用自定义的 MyBeanProcessor
	 *
	 * @see DbUtilsBeanProcessor
	 * @param clazz
	 * @return
	 */
	private BeanListHandler<T> getBeanListHandler() {
		return new BeanListHandler<T>(poClass(), new BasicRowProcessor(
				new DbUtilsBeanProcessor()));
	}

	/**
	 * 使用自定义的 MyBeanProcessor
	 *
	 * @see DbUtilsBeanProcessor
	 * @param clazz
	 * @return
	 */
	private BeanHandler<T> getBeanHandler() {
		return new BeanHandler<T>(poClass(), new BasicRowProcessor(
				new DbUtilsBeanProcessor()));
	}

	/**
	 * 获取在子类中定义的泛型
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Class<T> poClass() {
		// 使用cglib代理，获取实际类型为getSuperclass()
		return (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	/**
	 * 从默认的数据源中获取一个数据库连接
	 *
	 * @return
	 */
	public static Connection getConn() {
		try {
			return DS.getConnection();
		} catch (Exception e) {
			throw new DbException("获取数据库连接失败", e);
		}
	}

	/**
	 *
	 * 查询返回列表
	 *
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<T> queryList(String sql, Object... params) {
		LOG.debug(new SqlHolder(sql, params));
		try {
			return (List<T>) QUERY_RUNNER.query(sql, getBeanListHandler(), params);
		} catch (SQLException e) {
			throw new DbException(MESSAGE, e);
		}
	}

	/**
	 * 查询返回单个对象
	 *
	 * @param sql
	 * @param params
	 * @return
	 */
	public T query(String sql, Object... params) {
		LOG.debug(new SqlHolder(sql, params));

		try {
			return (T) QUERY_RUNNER.query(sql, getBeanHandler(), params);
		} catch (SQLException e) {
			throw new DbException(MESSAGE, e);
		}
	}

	/**
	 * 查询long型数据
	 *
	 * @param sql
	 * @param params
	 * @return
	 */
	public Long queryLong(String sql, Object... params) {
		LOG.debug(new SqlHolder(sql, params));
		try {
			Number n = (Number) QUERY_RUNNER.query(sql, SCALE_HANDLER, params);
			return n.longValue();
		} catch (SQLException e) {
			throw new DbException(MESSAGE, e);
		}
	}

	/**
	 * 查询int型数据
	 *
	 *
	 * @param sql
	 * @param params
	 * @return
	 */
	public Integer queryInt(String sql, Object... params) {
		LOG.debug(new SqlHolder(sql, params));
		try {
			Number n = (Number) QUERY_RUNNER.query(sql, SCALE_HANDLER, params);
			return n.intValue();
		} catch (SQLException e) {
			throw new DbException(MESSAGE, e);
		}
	}

	/**
	 * 执行INSERT/UPDATE/DELETE语句
	 *
	 * @param conn
	 * @param sql
	 * @param params
	 * @return
	 */
	public int update(String sql, Object... params) {
		LOG.debug(new SqlHolder(sql, params));
		try {
            Connection conn = getConn();
            conn.setAutoCommit(false);
			int i = QUERY_RUNNER.update(conn, sql, params);
            DbUtils.commitAndCloseQuietly(conn);
            return i;
		} catch (SQLException e) {
			throw new DbException(MESSAGE, e);
		}
	}

	/**
	 * update
	 * @return
	 */
	public int update() {
		SqlHolder holder = SqlBuilder.buildUpdate(this);
		return update( holder.getSql(), holder.getParams());
	}

	/**
	 * insert
     * 默认为自动生成id
	 * @return
	 */
	public Long insert() {
		SqlHolder holder = SqlBuilder.buildInsert(this);
		update(holder.getSql(), holder.getParams());
        // 此处查询sqllite3 上一个生成的主键id TODO 不使用自动生成时 此处会出错
        Long id = queryLong(SqlBuilder.buildGetInsertId(this));
        ReflectUtils.set(this, "id", id);

        return id;
	}

	/**
	 *
	 * 查询列表
	 *
	 * @param sql
	 * @return Map<String, Object>
	 */
	public List<Map<String, Object>> queryMapList(String sql) {
		LOG.debug(sql);
		try {
			List<Map<String, Object>> results = (List<Map<String, Object>>) QUERY_RUNNER
					.query(sql, new MapListHandler());
			return results;
		} catch (SQLException e) {
			throw new DbException(MESSAGE, e);
		}
	}

	/**
	 * 分页查询
	 * 
	 * @param sql
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public Page<T> queryPage(String sql, int currentPage, int pageSize) {
		Page<T> page = new Page<T>(sql, currentPage, pageSize);
		page.setTotalResult(queryInt(page.getCountSql()));
		page.setResult(queryList(page.getPageSql()));
		return page;
	}
}