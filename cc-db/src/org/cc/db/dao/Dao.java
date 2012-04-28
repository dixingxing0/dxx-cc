package org.cc.db.dao;

import static java.lang.String.format;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.cc.core.CcException;
import org.cc.core.common.Exceptions;
import org.cc.core.common.ReflectUtils;
import org.cc.db.jdbc.JdbcConfig;
import org.cc.db.jdbc.JdbcHelper;

/**
 * 
 * <p>通用dao实现</p>
 * 
 * @author dixingxing	
 * @date Apr 24, 2012
 */
public class Dao<T> implements IDao<T> {
	private static final Logger LOG = Logger.getLogger(Dao.class);
	
	@SuppressWarnings("unused")
	private List<T> mapList(ResultSet rs, Class<T> clazz) {
		Field[] fields = ReflectUtils.getVariableFields(clazz);
		List<Map<String, Object>> maps = JdbcHelper.getMap(rs, fields);

		List<T> list = new ArrayList<T>();
		if (maps != null && maps.size() > 0) {
			for (Map<String, Object> map : maps) {
				list.add(new Mapper<T>(clazz).mapObject(map, fields));
			}
		}
		return list;
	}

	private T mapOne(ResultSet rs, Class<T> clazz) {
		Field[] fields = ReflectUtils.getVariableFields(clazz);
		List<Map<String, Object>> maps = JdbcHelper.getMap(rs, fields);
		if (maps == null || maps.size() == 0) {
			return null;
		}
		return new Mapper<T>(clazz).mapObject(maps.get(0), fields);
	}

	/**
	 * 获取在子类中定义的泛型
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Class<T> poClass() {
		try{
		// 使用cglib代理，获取实际类型为getSuperclass()
		 return (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		}catch (Exception e) {
			try {
				return (Class<T> )Class.forName(getType());
			} catch (ClassNotFoundException e1) {
				Exceptions.uncheck(e);
				return null;
			}
		}
	}
	
	protected String getType() {
		return null;
	}

	public T query(Long id) {
		final Connection cn = getConnection();
		SqlHolder holder = SqlBuilder.buildQueryById(poClass(), id);
		LOG.debug(holder);
		PreparedStatement pstmt = JdbcHelper.getPstmt(cn, holder.getSql());
		JdbcHelper.setParams(pstmt, holder.getParams());
		ResultSet rs = JdbcHelper.executeQuery(pstmt);
		T po = mapOne(rs, poClass());
		JdbcHelper.close(rs, pstmt);
		realese(cn);
		return po;
	}

	public Long insert(T po) {
		String getSeq = SqlBuilder.buildGetSeq(po);
		
		Long id  = (Long) ReflectUtils.getValueByFieldName(po, "id");
		// 如果id为空，并定义了序列，那么从数据库序列中查询id的值
		if(id == null && getSeq != null) {
			id = queryLong(getSeq);
			ReflectUtils.set(po, "id", id);
		}
		
		SqlHolder holder = SqlBuilder.buildInsert(po);
		update(holder);
		
		// 如果是自增主键
		if(id == null) {
			// 返回id
			id = queryLong(SqlBuilder.buildGetInsertId(poClass()));
			ReflectUtils.set(po, "id", id);
		}

		return id;
	}

	public List<T> queryList(String sql, Object... params) {
		final Connection cn = getConnection();
		SqlHolder holder = new SqlHolder(sql, params);
		LOG.debug(holder);
		PreparedStatement pstmt = JdbcHelper.getPstmt(cn, holder.getSql());
		JdbcHelper.setParams(pstmt, holder.getParams());
		ResultSet rs = JdbcHelper.executeQuery(pstmt);
		List<T> list = mapList(rs, poClass());
		JdbcHelper.close(rs, pstmt);
		realese(cn);
		return list;
	}

	public T query(String sql, Object... params) {
		List<T> list = queryList(sql, params);
		if (list.size() == 0) {
			return null;
		} else if (list.size() > 1) {
			throw new CcException(format("期望返回1条数据，实际返回%d条记录", list.size()));
		}
		return list.get(0);
	}

	public Long queryLong(String sql, Object... params) {
		final Connection cn = getConnection();
		SqlHolder holder = new SqlHolder(sql, params);
		LOG.debug(holder);
		PreparedStatement pstmt = JdbcHelper.getPstmt(cn, holder.getSql());
		JdbcHelper.setParams(pstmt, holder.getParams());
		ResultSet rs = JdbcHelper.executeQuery(pstmt);
		Long result = JdbcHelper.getNumber(rs, Long.class);
		JdbcHelper.close(rs, pstmt);
		realese(cn);
		return result;
	}

	public void update(T po) {
		SqlHolder holder = SqlBuilder.buildUpdate(po);
		update(holder);
	}

	/**
	 * 执行update
	 * 
	 * @param holder
	 */
	public void update(SqlHolder holder) {
		final Connection cn = getConnection();
		LOG.debug(holder);
		PreparedStatement pstmt = JdbcHelper.getPstmt(cn, holder.getSql());
		JdbcHelper.setParams(pstmt, holder.getParams());
		JdbcHelper.executeUpdate(pstmt);
		JdbcHelper.close(null, pstmt);
		realese(cn);
	}

	/**
	 * 执行sql语句
	 * 
	 * @param sql
	 * @param params
	 * @see org.cc.db.dao.IDao#execute(java.lang.String, java.lang.Object[])
	 */
	public void execute(String sql, Object... params) {
		SqlHolder holder = new SqlHolder(sql,params);
		update(holder);
	}
	
	/**
	 * 
	 * <p>根据ID删除</p>
	 *
	 * @param id
	 */
	public void delete(Long id) {
		SqlHolder holder = SqlBuilder.buildDelete(poClass(), id);
		update(holder);
	}

	/**
	 * 获取连接
	 * 
	 * @return
	 */
	private static Connection getConnection() {
		return JdbcConfig.getConnectionProvider().getConn();
	}
	
	/**
	 * 释放连接
	 * 
	 * @param conn
	 */
	private static void realese(Connection conn) {
		JdbcConfig.getConnectionProvider().release(conn);
	}
}
