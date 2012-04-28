/**
 * IDao.java 6:23:09 PM Apr 10, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.db.dao;

import java.util.List;

/**
 * 
 * 通用dao接口
 * 
 * @author dixingxing
 * @date Apr 10, 2012
 */
public interface IDao<T> {
	List<T> queryList(String sql, Object... params);

	Long insert(T po);

	void update(T po);

	T query(Long id);

	T query(String sql, Object... params);

	void execute(String sql, Object... params);
}
