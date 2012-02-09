/**
 * Colum.java 3:02:56 PM Feb 6, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dao.SqlBuilder;

/**
 * 
 * 定义po某个属性对应到数据库列的详细规则
 * 
 * @see SqlBuilder#buildUpdate(Object, String)
 * @author dixingxing
 * @date Feb 6, 2012
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
	boolean updatable() default true;
}
