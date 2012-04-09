/**
 * Binder.java 11:43:09 AM Apr 1, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.web.binding;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cc.web.WebMethod;

/**
 * 数据绑定接口
 * 
 * @author dixingxing	
 * @date Apr 1, 2012
 */
public interface Binder {

	
	/**
	 * 
	 * 将request中的参数绑定到 controller中方法定义的参数
	 * 
	 * @param request
	 * @param response
	 * @param webMethod
	 * @return
	 */
	Object[] fromRequest(HttpServletRequest request,HttpServletResponse response, WebMethod webMethod);
	
	/**
	 * 将对象绑定到request中,供视图使用
	 * 
	 * @param req
	 * @param objects
	 */
	void toRequest(HttpServletRequest req, Object[] objects);
}
