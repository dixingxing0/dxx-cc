/**
 * Mapper.java 6:44:18 PM Apr 10, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.db.dao;

import java.lang.reflect.Field;
import java.util.Map;

import org.cc.core.common.Exceptions;
import org.cc.core.common.ReflectUtils;


/**
 * 
 * 
 * @author dixingxing	
 * @date Apr 10, 2012
 */
public class Mapper<T> {
	private Class<T> clazz ;
	
	public Mapper(Class<T> clazz) {
		this.clazz = clazz;
	}
	
	public T mapObject(Map<String, Object> map,Field[] fields ) {
		T po = null;
		try {
			po = clazz.newInstance();
		} catch (Exception e) {
			Exceptions.uncheck(e);
		} 
		
		for(Field f : fields) {
			ReflectUtils.set(po, f, map.get(f.getName()));
		}
		return po;
	}

}
