/**
 * Ioc.java 3:50:30 PM Apr 7, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记为可以被其注入。既其他类可通过 {@link Inject} 来使用Ioc注解的类型 
 * 
 * @author dixingxing	
 * @date Apr 7, 2012
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Ioc {
}
