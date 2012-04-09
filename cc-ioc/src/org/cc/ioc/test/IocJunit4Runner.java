/**
 * CcRunner.java 10:32:35 AM Apr 9, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.ioc.test;

import org.cc.ioc.IocContext;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 * <p>使Junit4 有Ioc的能力</p>
 * <li>
 *  {@link #iocTarget} 是最终执行测试方法的实例，
 *  {@link IocContext} 中实例化好的对象
 * </li>
 * <li>
 * 	如果当前测试类没有定义{@link Inject} 属性，那么iocTarget为空，使用Junit4默认的test对象
 * </li>
 * @author dixingxing	
 * @date Apr 9, 2012
 */
public class IocJunit4Runner extends BlockJUnit4ClassRunner{
	
	/**
	 * 是{@link IocContext}中实例化好的对象，如果当前测试类没有定义{@link Inject} 属性,那么iocTarget 为空
	 */
	private Object iocTarget;
	
	public IocJunit4Runner(Class<?> klass) throws InitializationError{
		super(klass);
		this.iocTarget = IocContext.get(klass);
	}
	
	@Override
	protected Object createTest() throws Exception {
		return iocTarget != null ? iocTarget : super.createTest();
	}
	
	

}
