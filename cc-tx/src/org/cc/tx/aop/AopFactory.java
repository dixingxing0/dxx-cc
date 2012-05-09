/**
 * AopFactory.java 4:35:24 PM Apr 29, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.tx.aop;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;
import org.cc.core.common.Exceptions;
import org.cc.core.common.ReflectUtils;

/**
 * <p>aop工厂</p>
 * <li> aop handler : org.cc.tx.TxHandler
 * 
 * @see AsmFactory
 * @see JavassistFactory
 * @author dixingxing	
 * @date Apr 29, 2012
 */
public abstract class AopFactory {
	private static final Logger LOG = Logger.getLogger(AopFactory.class);
	public static final String SUFIX = "$EnhancedByCc";
	
	public static final String HANDLER_NAME = "org.cc.tx.TxHandler";
	
	/**
	 * 
	 * <p>代理对象</p>
	 * <li> 生成代理类
	 * <li> 获得代理类的实例（要赋值源对象的属性值）
	 * @param <T>
	 * @param obj
	 * @return
	 */
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
			Exceptions.uncheck(e);
		} 
		return null;
	}
	
	/**
	 * 
	 * <p>获取代理类</p>
	 *
	 * @param <C>
	 * @param cls
	 * @return
	 */
	protected abstract <C> Class<C> getEnhancedClass(Class<C> cls);
}
