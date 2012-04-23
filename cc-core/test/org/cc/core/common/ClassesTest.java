/**
 * ClassesTest.java 10:19:11 AM Apr 23, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.core.common;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * <p>
 * </p>
 * 
 * @author dixingxing
 * @date Apr 23, 2012
 */
public class ClassesTest {

	/**
	 * <p>
	 * </p>
	 * 
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for
	 * {@link org.cc.core.common.Classes#getMethodParamNames(java.lang.reflect.Method)}.
	 * 
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	@Test
	public void testGetMethodParamNames() throws SecurityException,
			NoSuchMethodException {
		String[] names = Classes.getMethodParamNames(Foo.class.getMethod("m1",String.class));
		assertArrayEquals(new String[] { "s" }, names);

		names = Classes.getMethodParamNames(Foo.class.getMethod("m2",Integer.TYPE,String.class));
		assertArrayEquals(new String[] { "i","s" }, names);
		
		names = Classes.getMethodParamNames(Foo.class.getMethod("m3",Integer.class,String[].class));
		assertArrayEquals(new String[] { "i","s" }, names);
		
		names = Classes.getMethodParamNames(Foo.class.getMethod("m3",Integer[].class,String[].class));
		assertArrayEquals(new String[] { "i","s" }, names);
		
		names = Classes.getMethodParamNames(Foo.class.getMethod("m4",Object[].class,Integer[].class));
		assertArrayEquals(new String[] { "objs","integers" }, names);
	}
}
