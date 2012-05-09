/**
 * TempClassHolder.java 5:47:53 PM Apr 26, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.tx.aop;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * <p>保存aop代理时生成的代理类</p>
 * 
 * @author dixingxing	
 * @date Apr 26, 2012
 */
public final class TempClassHolder {
	private static final Logger LOG = Logger.getLogger(TempClassHolder.class);
	private static final Map<String, Class<?>> map = new HashMap<String, Class<?>>();
	
	private TempClassHolder() {}
	
	/**
	 * 
	 * <p>放入新的class</p>
	 *
	 * @param key
	 * @param cls
	 */
	public static synchronized void put(String key, Class<?> cls) {
		LOG.debug(String.format("put class : %s",key));
		map.put(key, cls);
	}
	
	/**
	 * 
	 * <p>是否包含指定class</p>
	 *
	 * @param key
	 * @return
	 */
	public static boolean contains(String key) {
		return map.containsKey(key);
	}
	
	/**
	 * 
	 * <p>获取class</p>
	 *
	 * @param key
	 * @return
	 */
	public static synchronized  Class<?> get(String key) {
		LOG.debug(String.format("get class : %s",key));
		return map.get(key);
	}
}
