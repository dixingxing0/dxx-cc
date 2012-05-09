/**
 * PathVarBinder.java 5:00:44 PM Mar 31, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.web.binding;

import java.lang.annotation.Annotation;

import org.apache.log4j.Logger;
import org.cc.web.WebMethod;
import org.cc.web.annotation.PathVar;

/**
 * 获取 {@link #PathVar} 的参数值
 * 
 * @author dixingxing	
 * @date Mar 31, 2012
 */
public final class PathVarBinderImpl implements PathVarBinder{
	private static final Logger LOG = Logger.getLogger(PathVarBinderImpl.class);

	private ObjectBuilder objectBuilder = BindUtils.getObjectBuilder();
	/**
	 * 参数是否标注了PathVar注解
	 * 
	 * @param annotations
	 * @return
	 */
	public boolean isPathVar(Annotation[] annotations) {
		for (Annotation a : annotations) {
			if (PathVar.class.equals(a.annotationType())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获取PathVar注解
	 * 
	 * @param annotations
	 * @return
	 */
	public PathVar getPathVarAnnotation(Annotation[] annotations) {
		for (Annotation a : annotations) {
			if (PathVar.class.equals(a.annotationType())) {
				return (PathVar) a;
			}
		}
		return null;
	}
	
	/**
	 * 根据 {@link #PathVar} 的定义，从uri中解析出相应的值
	 * 
	 * @param serletPath
	 * @param webMethod
	 * @param cls
	 * @param annotations
	 * @return
	 */
	public Object getValue(String serletPath,WebMethod webMethod ,Class<?> cls ,Annotation[] annotations) {
		String[] variables = webMethod.getPathVars(serletPath);
		Object returnValue = null;
		PathVar pv = getPathVarAnnotation(annotations);
		
		if (pv.value() >= variables.length) {
			LOG.warn("PathVar.value()值为 :" + pv.value() + ",应该小于匹配到的参数个数 : " + variables.length);
		} else {
			String v = variables[pv.value()];
			returnValue = objectBuilder.build(cls, v);
		}
		
		if(returnValue == null) {
			try {
				returnValue = cls.newInstance();
			} catch (Exception e) {
				// nothing to do 
			} 
		}
		
		return returnValue;
		
	}
}
