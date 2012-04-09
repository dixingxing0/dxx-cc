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
 * 标记为可以被其他类注入。既其他类可通过 {@link Inject} 来使用Ioc注解的类型 
 * 
 * <p>定义UserService类并标注{@link　Ioc}注解。</p>
 * <p>@Ioc</p>
 * <p>public class UserService{</p>
 * <p>public int someMethod(){</p>
 * <p>....do something </p>
 * <p>return 1; </p>
 * <p>}</p>
 * <p>}</p>
 * 
 * <p>使用方法</p>
 * <p>public class UserController {</p>
 * <p>@Inject </p>
 * <p>private UserService service;</p>
 * <p>@Test</p>
 * <p>public void testInject() {</p>
 * <p>assertNotNull(service);</p>
 * <p>assertTrue(1 == service.someMethod());</p>
 * <p>}</p>
 * 
 * @author dixingxing	
 * @date Apr 7, 2012
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Ioc {
}
