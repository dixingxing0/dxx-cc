/**
 * BinderImplTest.java 7:41:22 PM Apr 20, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.web.binding;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cc.core.web.TestController;
import org.cc.web.WebMethod;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * <p></p>
 * 
 * @author dixingxing	
 * @date Apr 20, 2012
 */
@RunWith(MockitoJUnitRunner.class)
public class BinderImplTest {
	
	private BinderImpl binderImpl;
	@Mock
	private HttpServletRequest request;
	
	@Mock
	private HttpServletResponse response;
	
	private WebMethod webMethod;
	/**
	 * <p></p>
	 *
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		binderImpl = new BinderImpl();
		
		TestController c = new TestController();
		webMethod = new WebMethod();
		
		webMethod.setHandler(c);
		try {
			webMethod.setMethod(c.getClass().getMethod("testParams",String.class,String.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link org.cc.web.binding.BinderImpl#fromRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.cc.web.WebMethod)}.
	 */
	@Test
	public void testFromRequest() {
		when(request.getParameter("id")).thenReturn("id2");
		when(request.getParameter("name")).thenReturn("name2");
		Object[] values = binderImpl.fromRequest(request, response, webMethod);
		// 测试绑定方法中的基本类型参数 int long String date 等
		assertArrayEquals(new Object[]{"id2","name2"}, values);
	}
}
