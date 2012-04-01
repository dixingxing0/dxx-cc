/**
 * CacheUtils.java 9:49:57 AM Feb 15, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.core.cache;


/**
 * 缓存接口
 * @author dixingxing
 * @date Feb 15, 2012
 */
public interface CacheClient {

	/**
	 * 放入缓存
	 * 
	 * @param key
	 * @param exp
	 *            秒
	 * @param value
	 *            要实现Serializable
	 */
	void set(final String key, final int exp, final Object value);

	void set(final String key, final Object value);
	
	/**
	 * 获得缓存对象
	 * 
	 * @param key
	 * @return
	 */
	<T> T get(final String key) ;
	
	/**
	 * 删除
	 * 
	 * @param key
	 */
	void delete(final String key) ;
}
