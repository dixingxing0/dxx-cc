/**
 * Test.java 5:18:56 PM Apr 26, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package com.fujitsu.fnst.asm.demo;

import com.fujitsu.fnst.asm.utility.EnhanceException;
import com.fujitsu.fnst.asm.utility.EnhanceFactory;

/**
 * <p></p>
 * 
 * @author dixingxing	
 * @date Apr 26, 2012
 */
public class Test {
	public static void main(String[] args) throws EnhanceException {
		
		 // 不用 new 关键字，而使用 EnhanceFactory.newInstance 创建增强类的实例
		 SubClass1 obj1 = EnhanceFactory.newInstance(SubClass1.class, 
		 TimeRetriever.class,FibonacciComputer.class); 
		 // 调用待增强类中的方法
		 obj1.methodInSuperClass(); 
		 obj1.methodDefinedInSubClass1();  
		 // 调用实现类中的方法
		 System.out.println("The Fibonacci number of 10 is "+obj1.compute(10)); 
		 System.out.println("Now is :"+obj1.tellMeTheTime()); 
		 System.out.println("--------------------------------------"); 
		 // 对于 SubClass2 的增强类实例的创建也是一样的
		 SubClass2 obj2 = EnhanceFactory.newInstance(SubClass2.class, 
		 TimeRetriever.class,FibonacciComputer.class); 
		 // 调用待增强类中的方法
		 obj2.methodInSuperClass(); 
		 obj2.methodDefinedInSubClass2(); 
		 // 调用实现类中的方法
		 System.out.println("The Fibonacci number of 10 is "+obj1.compute(10)); 
		 System.out.println("Now is :"+obj1.tellMeTheTime()); 
	}
}
