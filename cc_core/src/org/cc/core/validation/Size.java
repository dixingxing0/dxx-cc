/**
 * 
 */
package org.cc.core.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 驗證字符串，Collection, Array的大�?
 * @author liangzhonghua
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Size {
	int min() default 0;
	int max() default 1000000000;
	String messageForMin() default "长度不符合规范，不能小于%d";
	String messageForMax() default "长度不符合规范，不能大于%d";
}
