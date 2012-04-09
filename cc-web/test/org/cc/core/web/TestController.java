/**
 * LoginController.java 2:39:42 PM Mar 31, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.core.web;

import org.cc.web.annotation.Controller;
import org.cc.web.annotation.PathVar;
import org.cc.web.annotation.RequestMapping;
import org.cc.web.annotation.RequestMethod;

/**
 * 
 * 
 * @author dixingxing	
 * @date Mar 31, 2012
 */
@Controller
@RequestMapping("/test")
public class TestController {
	@RequestMapping(value = "/([0-9]+)" ,method=RequestMethod.GET)
	public String testPathVar(@PathVar(0) Long id) {
		return "secure.jsp";
	}

	@RequestMapping("/logout")
	public String logout() {
		return "secure.jsp";
	}
}
