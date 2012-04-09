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
 * 标记是需要注入的属性，需要配合{@link Ioc} 使用 。
 * 
 * @see Ioc
 * 
 * @author dixingxing	
 * @date Apr 7, 2012
 */
@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Inject {
}
