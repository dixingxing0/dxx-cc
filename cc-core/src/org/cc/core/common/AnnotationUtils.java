/**
 * AnnotationUtils.java 6:37:19 PM Apr 1, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.core.common;


/**
 * 
 * <p>Annotation 工具类</p>
 * 
 * @author dixingxing	
 * @date Apr 1, 2012
 */
public final class AnnotationUtils {
	
	private AnnotationUtils() {}
	
	/**
	 * <p>判断对象是否有注解并返回。没有则返回 null</p>
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
