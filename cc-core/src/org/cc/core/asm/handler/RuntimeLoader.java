/**
 * ClassLoader.java 9:57:15 AM Apr 26, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.core.asm.handler;
/**
 * <p></p>
 * 
 * @author dixingxing	
 * @date Apr 26, 2012
 */
public class RuntimeLoader extends ClassLoader {
	byte[] data;
	
	public RuntimeLoader(byte[] data) {
		this.data = data;
	}
	
	@SuppressWarnings("unchecked")
	public <T> Class<T> defineClass(String name) {
		return (Class<T>) super.defineClass(name, data, 0, data.length);
	}
}
