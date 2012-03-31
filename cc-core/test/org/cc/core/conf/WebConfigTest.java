/**
 * WebConfigTest.java 10:32:54 AM Mar 31, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.core.conf;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * 
 * 
 * @author dixingxing	
 * @date Mar 31, 2012
 */
public class WebConfigTest {

	/**
	 * Test method for {@link org.cc.core.conf.WebConfig#getControllerLocation()}.
	 */
	@Test
	public void testGetControllerLocation() {
		assertNotNull(WebConfig.getControllerLocation());
	}

}
