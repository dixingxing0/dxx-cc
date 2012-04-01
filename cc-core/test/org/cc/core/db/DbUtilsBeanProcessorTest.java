/**
 * DbUtilsBeanProcessorTest.java 4:40:06 PM Mar 30, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.core.db;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * 
 * @author dixingxing	
 * @date Mar 30, 2012
 */
public class DbUtilsBeanProcessorTest {
	/**
	 * Test method for {@link org.cc.core.db.DbUtilsBeanProcessor#j2db(java.lang.String)}.
	 */
	@Test
	public void testProp2column() {
		Assert.assertEquals("publish_time_a_b", DbUtilsBeanProcessor.j2db("publishTimeAB"));
	}

}
