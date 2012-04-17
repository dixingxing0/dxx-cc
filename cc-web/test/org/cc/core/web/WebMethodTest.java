/**
 * WebMethodTest.java 7:07:23 PM Mar 31, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.core.web;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.cc.core.common.Strings;
import org.cc.web.WebMethod;
import org.cc.web.annotation.RequestMethod;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * 
 * 
 * @author dixingxing	
 * @date Mar 31, 2012
 */
@RunWith(MockitoJUnitRunner.class)
public class WebMethodTest {
	private static final Logger LOG = Logger.getLogger(WebMethodTest.class);
	
	private WebMethod webMethod;
	
	@Mock
	private HttpServletRequest request;
	
	@Before
	public void setUp() {
		TestController c = new TestController();
		webMethod = new WebMethod();
		webMethod.setHandler(c);
		try {
			webMethod.setMethod(c.getClass().getMethod("testPathVar",Long.class));
		} catch (Exception e) {
			LOG.error(null,e);
		}
		webMethod.setUrlPathMain(new String[]{"/test"});
		webMethod.setUrlPath(new String[]{"/([0-9]+)"});
		webMethod.setRequestMethod(new RequestMethod[]{RequestMethod.GET});
		
	}

	/**
	 * Test method for {@link org.cc.web.WebMethod#match(javax.servlet.http.HttpServletRequest)}.
	 */
	@Test
	public void testMatchHttpServletRequest() {
		when(request.getServletPath()).thenReturn("/test/123");
		
		when(request.getMethod()).thenReturn("GET");
		
		assertEquals(true, webMethod.match(request));
		
		when(request.getMethod()).thenReturn("POST");
		assertEquals(false, webMethod.match(request));
	}
	
	/**
	 * Test method for {@link org.cc.web.WebMethod#match(java.lang.String)}.
	 */
	@Test
	public void testMatchString() {
		boolean match = webMethod.match("/test/12345");
		assertEquals(true, match);
		
		match = webMethod.match("/test/12345d");
		assertEquals(false, match);
	}

	/**
	 * Test method for {@link org.cc.web.WebMethod#getPathVars(java.lang.String)}.
	 */
	@Test
	public void testGetPathVars() {
		assertEquals("123", Strings.join(webMethod.getPathVars("/test/123")));
	}

}
