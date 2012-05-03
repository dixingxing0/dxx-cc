/**
 * TempClassHolder.java 5:47:53 PM Apr 26, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.tx.aop.common;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * <p></p>
 * 
 * @author dixingxing	
 * @date Apr 26, 2012
 */
public class TempClassHolder {
	private static final Logger LOG = Logger.getLogger(TempClassHolder.class);
	private static final Map<String, Class<?>> map = new HashMap<String, Class<?>>();
	
	
	public static synchronized void put(String key, Class<?> cls) {
		LOG.debug(String.format("put class : %s",key));
		map.put(key, cls);
	}
	
	public static boolean contains(String key) {
		return map.containsKey(key);
	}
	public static synchronized  Class<?> get(String key) {
		LOG.debug(String.format("get class : %s",key));
		return map.get(key);
	}
}
