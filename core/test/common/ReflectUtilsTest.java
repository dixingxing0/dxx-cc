/**
 * ReflectUtilsTest.java 10:24:25 AM Feb 9, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package common;

import java.lang.reflect.Field;

import junit.framework.Assert;
import junit.framework.TestCase;
import data.B;

/**
 * 
 * 
 * @author dixingxing
 * @date Feb 9, 2012
 */
public class ReflectUtilsTest extends TestCase {

	public void testGetFieldByName() {
		Field f = ReflectUtils.getFieldByName(new B(), "id");
		Assert.assertNotNull(f);
	}

	public void testGetVariableFields() {
		Field[] fields = ReflectUtils.getVariableFields(B.class);
		for (Field f : fields) {
			System.out.println(f.getDeclaringClass().getSimpleName() + " : "
					+ f.getName());
		}
	}
}
