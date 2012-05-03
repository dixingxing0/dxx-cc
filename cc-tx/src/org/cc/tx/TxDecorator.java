/**
 * TransactionDecorator.java 4:26:28 PM Apr 12, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.tx;

import java.lang.reflect.Method;

import org.cc.core.common.ReflectUtils;
import org.cc.ioc.Decorator;
import org.cc.tx.aop.common.Aops;

/**
 * 
 * <p>
 * 事务装饰器
 * </p>
 * @author dixingxing	
 * @date Apr 12, 2012
 */
public class TxDecorator implements Decorator{
	@SuppressWarnings("unchecked")
	public <T> T doDecorator(Object obj) {
		if(obj == null) {
			return (T) obj;
		}
		Class<?> inter = getInterfaceClass(obj);
		if(inter == null) {
			return (T) obj;
		}
		
		if(!isTransactional(obj)) {
			return (T) obj;
		}
		T enhanced = (T) Aops.proxy(obj);
		return enhanced;
	}
	
	/**
	 * <p>方法是否标记了{@link Transactional} </p>
	 * 
	 * @param obj
	 * @return
	 */
	private boolean isTransactional(Object obj) {
		if(obj.getClass().isAnnotationPresent(Transactional.class)) {
			return true;
		}
		
		Method[] methods = ReflectUtils.getVariableMethods(obj.getClass());
		for(Method m : methods) {
			if(m.isAnnotationPresent(Transactional.class)) {
				return true;
			}
		}
		return false;
		
	}
	
	/**
	 * 
	 * <p>获取实现的接口（多个接口时返回第一个）</p>
	 * @param obj
	 * @return
	 */
	private static Class<?> getInterfaceClass(Object obj){
		Class<?>[] cls = obj.getClass().getInterfaces();
		if (cls == null || cls.length == 0) {
			return null;
		}
		return cls[0];
	}
}
