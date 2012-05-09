/**
 * ObjectBuilderTest.java 5:50:43 PM Mar 31, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.core.web.binding;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.cc.web.binding.BindUtils;
import org.cc.web.binding.ObjectBuilder;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * 
 * @author dixingxing	
 * @date Mar 31, 2012
 */
public class ObjectBuilderTest {
	
	private ObjectBuilder objectBuilder;
	
	@Before
	public void setUp() {
		objectBuilder = BindUtils.getObjectBuilder();
	}

	/**
	 * Test method for {@link org.cc.web.binding.ObjectBuilderImpl#build(java.lang.Class, java.lang.String)}.
	 */
	@Test
	public void testBuild() {
		Object o = null;
		o = objectBuilder.build(Long.class, "12L");
		assertEquals(null, o);

		o = objectBuilder.build(Long.class, "12");
		assertEquals(12L, o);
		
		o = objectBuilder.build(Integer.class, "12");
		assertEquals(12, o);
		
		o = objectBuilder.build(Double.class, "12.01");
		assertEquals(12.01, o);
		o = objectBuilder.build(Double.class, "12");
		assertEquals(12.0, o);

		o = objectBuilder.build(Date.class, "2011-11-11");
		assertEquals("2011-11-11", new SimpleDateFormat("yyyy-MM-dd").format(o));
		
		
	}

}
