/**
 * PathVarBinder.java 5:00:44 PM Mar 31, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.core.web.binding;

import java.lang.annotation.Annotation;

import org.cc.core.web.WebMethod;
import org.cc.core.web.annotation.PathVar;

/**
 * 获取 {@link #PathVar} 的参数值
 * 
 * @author dixingxing	
 * @date Mar 31, 2012
 */
public interface PathVarBinder {
	
	/**
	 * 参数是否标注了PathVar注解
	 * 
	 * @param annotations
	 * @return
	 */
	boolean isPathVar(Annotation[] annotations);
	
	/**
	 * 获取PathVar注解
	 * 
	 * @param annotations
	 * @return
	 */
	PathVar getPathVarAnnotation(Annotation[] annotations);
	
	/**
	 * 根据 {@link #PathVar} 的定义，从uri中解析出相应的值
	 * 
	 * @param serletPath
	 * @param webMethod
	 * @param cls
	 * @param annotations
	 * @return
	 */
	Object getValue(String serletPath,WebMethod webMethod ,Class<?> cls ,Annotation[] annotations) ;
}
