/**
 * BaseDao.java 7:24:12 PM Jan 17, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.core.db;

import java.util.List;
import java.util.Map;

import org.cc.core.cache.CacheClient;



/**
 * cached dao 基类
 *
 * @author dixingxing
 * @date Jan 17, 2012
 */
@SuppressWarnings("unchecked")
public abstract class CachedDao<T> extends Dao<T> {
	private CacheClient client;
	
	
	/**
	 * 根据id查询
	 *
	 * @param sql
	 * @param params
	 * @return
	 */
	public T query(Long id) {
		String sql = SqlBuilder.buildQueryById(this,id);
		LOG.debug(sql);
		T t = (T) client.get(sql);
		
		if(t != null) {
			return t;
		}
		
		t = query(sql);
		client.set(sql, t);
		return t;
	}
	/**
	 * 查询返回单个对象
	 *
	 * @param sql
	 * @param params
	 * @return
	 */
	@Override
	public T query(String sql, Object... params) {
		SqlHolder holder = new SqlHolder(sql, params);
		String key = holder.cacheKey();
		T t = (T) client.get(key);
		if(t != null){
			return t;
		}
		t = super.query(sql, params);
		
		client.set(key,t);
		return t;
	}
	
	/**
	 *
	 * 查询返回列表
	 *
	 * @param sql
	 * @param params
	 * @return
	 */
	@Override
	public List<T> queryList(String sql, Object... params) {
		SqlHolder holder = new SqlHolder(sql, params);
		String key = holder.cacheKey();
		List<T> list = client.get(key);
		if(list != null){
			return list;
		}
		list = super.queryList(sql, params);
		client.set(key, list);
		return list;
	}

	/**
	 * 查询long型数据
	 *
	 * @param sql
	 * @param params
	 * @return
	 */
	@Override
	public Long queryLong(String sql, Object... params) {
		SqlHolder holder = new SqlHolder(sql, params);
		String key = holder.cacheKey();
		Long num = client.get(key);
		if(num != null){
			return num;
		}
		num = super.queryLong(sql, params);
		client.set(key, num);
		return num;
	}

	/**
	 * 查询int型数据
	 *
	 *
	 * @param sql
	 * @param params
	 * @return
	 */
	@Override
	public Integer queryInt(String sql, Object... params) {
		SqlHolder holder = new SqlHolder(sql, params);
		String key = holder.cacheKey();
		Integer num = client.get(key);
		if(num != null){
			return num;
		}
		num = super.queryInt(sql, params);
		client.set(key, num);
		return num;
	}

	/**
	 * update
	 * @return
	 */
	@Override
	public int update() {
		SqlHolder holder = SqlBuilder.buildUpdate(this);
		return update( holder.getSql(), holder.getParams());
	}

	/**
	 *
	 * 查询列表
	 *
	 * @param sql
	 * @return Map<String, Object>
	 */
	@Override
	public List<Map<String, Object>> queryMapList(String sql) {
		SqlHolder holder = new SqlHolder(sql, new Object[]{});
		String key = holder.cacheKey();
		List<Map<String, Object>> list = client.get(key);
		if(list != null){
			return list;
		}
		list = super.queryMapList(sql);
		client.set(key, list);
		return list;
	}

	/**
	 * 分页查询
	 * 
	 * @param sql
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@Override
	public Page<T> queryPage(String sql, int currentPage, int pageSize) {
		Page<T> page = new Page<T>(sql, currentPage, pageSize);
		String pageSql = page.getPageSql();
		String countSql = page.getCountSql();
		
		
		SqlHolder holder = new SqlHolder(sql, new Object[]{});
		String key = holder.cacheKey();
		Page<T> cachedPage = client.get(key);
		if(cachedPage != null){
			return cachedPage;
		}
		page.setTotalResult(queryInt(pageSql));
		page.setResult(queryList(countSql));
		client.set(key, page);
		return page;
	}
	public void setClient(CacheClient client) {
		this.client = client;
	}
	
	
}