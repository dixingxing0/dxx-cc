/**
 * Decorator.java 4:13:44 PM Apr 12, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.ioc;
/**
 * 
 * <p>扩展型接口，IOC实例化对象后，调用{@link #doDecorator(Object)}方法对实例进行处理。</p>
 * 
 * @author dixingxing	
 * @date Apr 20, 2012
 */
public interface Decorator {
	/**
	 * 
	 * <p>Ioc实例化的对象</p>
	 * @param <T>
	 * @param obj
	 * @return
	 */
	<T> T doDecorator(Object obj );
}
