/**
 * AnnotationUtils.java 6:37:19 PM Apr 1, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.core.common;


/**
 * 
 * Annotation 工具类
 * @author dixingxing	
 * @date Apr 1, 2012
 */
public final class AnnotationUtils {
	
	private AnnotationUtils() {}
	
	/**
	 * 没有则返回 null
	 * 
	 * @param <A>
	 * @param obj
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <A> A get(Object obj,Class clazz) {
		if(obj.getClass().isAnnotationPresent(clazz)) {
			return (A)obj.getClass().getAnnotation(clazz);
		}
		return null;
	}
	
}
