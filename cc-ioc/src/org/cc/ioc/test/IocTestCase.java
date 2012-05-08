/**
 * IocTestCase.java 1:28:42 PM Apr 9, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.ioc.test;

import org.cc.ioc.annotation.IocTest;
import org.junit.runner.RunWith;

/**
 * <p>仅支持JUnit4</p>
 * <p>继承此类可以使测试类有Ioc能力。</p>
 * <p>如：需定义UserService类并标注{@link　Ioc}注解。</p>
 * <p>public class UserServiceTest <strong>extends IocTestCase </strong>{</p>
 * <p>@Inject </p>
 * <p>private UserService service;</p>
 * <p>@Test</p>
 * <p>public void testInject() {</p>
 * <p>assertNotNull(service);</p>
 * <p>}</p>
 * @author dixingxing	
 * @date Apr 9, 2012
 */
@RunWith(IocJunit4Runner.class)
@IocTest
public class IocTestCase {

}
