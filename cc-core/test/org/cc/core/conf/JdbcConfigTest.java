/**
 * JdbcConfigTest.java 10:27:56 AM Mar 31, 2012
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
public class JdbcConfigTest {

	/**
	 * Test method for {@link org.cc.core.conf.JdbcConfig#getDriverClassName()}.
	 */
	@Test
	public void testGetDriverClassName() {
		assertNotNull(JdbcConfig.getDriverClassName());
	}

	/**
	 * Test method for {@link org.cc.core.conf.JdbcConfig#getUserName()}.
	 */
	@Test
	public void testGetUserName() {
		assertNotNull(JdbcConfig.getUserName());
	}

	/**
	 * Test method for {@link org.cc.core.conf.JdbcConfig#getPassword()}.
	 */
	@Test
	public void testGetPassword() {
		assertNotNull(JdbcConfig.getPassword());
	}

	/**
	 * Test method for {@link org.cc.core.conf.JdbcConfig#getUrl()}.
	 */
	@Test
	public void testGetUrl() {
		assertNotNull(JdbcConfig.getUrl());
	}

}
