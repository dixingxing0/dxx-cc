/**
 * BindUtilsTest.java 3:43:48 PM May 9, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.web.binding;

import static org.junit.Assert.*;

import org.cc.web.WebConfig;
import org.junit.Before;
import org.junit.Test;

/**
 * <p></p>
 * 
 * @author dixingxing	
 * @date May 9, 2012
 */
public class BindUtilsTest {

	/**
	 * <p></p>
	 *
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		WebConfig.init();
	}

	/**
	 * Test method for {@link org.cc.web.binding.BindUtils#getBinder()}.
	 */
	@Test
	public void testGetBinder() {
		assertNotNull(BindUtils.getBinder());
	}

	/**
	 * Test method for {@link org.cc.web.binding.BindUtils#getPathVarBinder()}.
	 */
	@Test
	public void testGetPathVarBinder() {
		assertNotNull(BindUtils.getPathVarBinder());
	}

	/**
	 * Test method for {@link org.cc.web.binding.BindUtils#getObjectBuilder()}.
	 */
	@Test
	public void testGetObjectBuilder() {
		assertNotNull(BindUtils.getObjectBuilder());
	}

}
