/**
 * Convertor.java 5:22:22 PM Mar 31, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.web.binding;


/**
 * 把String 转换成相应类型
 * 
 * @author dixingxing	
 * @date Mar 31, 2012
 */
public interface ObjectBuilder {
	/**
	 * 
	 * 把String 转换成相应类型
	 * 
	 * @param cls
	 * @param value
	 * @param v
	 * @return
	 */
	Object build(Class<?> cls, String v);
	
	/**
	 * 是否是可以转换的类型
	 * 
	 * @param cls
	 * @return
	 */
	boolean isNativeClass(Class<?> cls);
}
