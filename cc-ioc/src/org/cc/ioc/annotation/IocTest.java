/**
 * IocTest.java 2:55:03 PM May 8, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.cc.ioc.IocConfig;

/**
 * <p>定义ioc配置文件的路径</p>
 * <li>默认为{@link IocConfig#DEFAULT_IOC_CONFIG_FILE}
 * 
 * @see IocConfig#DEFAULT_IOC_CONFIG_FILE
 * @author dixingxing	
 * @date May 8, 2012
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface IocTest {
	String value() default IocConfig.DEFAULT_IOC_CONFIG_FILE;
}
