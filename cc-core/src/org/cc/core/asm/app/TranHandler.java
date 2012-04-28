/**
 * Handler.java 9:29:30 AM Apr 26, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.core.asm.app;

import org.cc.core.asm.handler.Factory;

/**
 * <p></p>
 * 
 * @author dixingxing	
 * @date Apr 26, 2012
 */
public class TranHandler {
	public static void before(String method){
		System.out.println("before method : " + method);
	}
	
	public static void after(String method){
		System.out.println("after method : " + method);
	}
	
	public static boolean isTran(String desc) {
		return desc != null && "Lorg/junit/After;".equals(desc);
	}
	
	public static void main(String[] args) {
		Factory.get(UserService.class, TranHandler.class).run();
	}
}
