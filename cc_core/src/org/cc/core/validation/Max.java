/**
 * 
 */
package org.cc.core.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 校验数字类型的最大�?�，支持Long,Integer,BigDecimal
 * 
 * @author liangzhonghua
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Max {
	
	double value() default 1000000000.0;
	String message() default "不能大于%f";
}
