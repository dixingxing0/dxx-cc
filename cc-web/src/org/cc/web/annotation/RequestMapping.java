/**
 * RequestMapping.java 1:26:32 PM Feb 7, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 详细描述可以处理的web请求
 * 
 * @author dixingxing	
 * @date Mar 30, 2012
 */
@Target( { ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
	String[] value() default {};

	RequestMethod[] method() default {};
}
