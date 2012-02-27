package web.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 如果实际servletPath 为/ad/a12011-11-12x RequestMapping定义的映射规则为
 * 
 * RequestMapping("/ad/a(\\w)(.*)x")<br/> 此时PathVar.value=1表明
 * 参数值应该绑定(.*)的值2011-11-12 (即value是从0开始的)
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PathVar {

	/** 对应requestMapping 正则表达式中的第几个group,也就是第几个（）中的数据 */
	int value() default 0;

}