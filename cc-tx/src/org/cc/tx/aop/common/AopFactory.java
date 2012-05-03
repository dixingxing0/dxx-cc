/**
 * AopFactory.java 4:35:24 PM Apr 29, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.tx.aop.common;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;
import org.cc.core.common.ReflectUtils;

/**
 * <p></p>
 * 
 * @author dixingxing	
 * @date Apr 29, 2012
 */
public abstract class AopFactory {
	private static final Logger LOG = Logger.getLogger(AopFactory.class);
	public static final String SUFIX = "$EnhancedByCc";
	
	public static final String BEFORE = "org.cc.tx.TxHandler.before(\"%s\");";
	public static final String AFTER = "org.cc.tx.TxHandler.after(\"%s\");";
	
	@SuppressWarnings("unchecked")
	public <T> T proxy(T obj)  {
		Class<T> cls = (Class<T>)obj.getClass();
		Class<T> enhancedCls = null;
		if(TempClassHolder.contains(cls.getName())) {
			enhancedCls = (Class<T>) TempClassHolder.get(cls.getName());
		} else {
			enhancedCls = getEnhancedClass(cls);
			
			TempClassHolder.put(cls.getName(), enhancedCls);
		}
		
		try {
			T eObj = enhancedCls.newInstance();
			
			// copy所有成员变量的值
			Field[] fields = ReflectUtils.getVariableFields(cls);
			for(Field f : fields) {
				Object v = ReflectUtils.get(obj, f);
				LOG.debug(String.format("copy field %s : %s",f.getName(),v));
				// 此处会有两个同名field，要分别赋值 TODO
				ReflectUtils.set(eObj, enhancedCls.getDeclaredField(f.getName()), v);
				ReflectUtils.set(eObj, f, v);
			}
			return eObj;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	protected abstract <C> Class<C> getEnhancedClass(Class<C> cls);
}
