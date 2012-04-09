/**
 * LoginController.java 2:39:42 PM Mar 31, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.demo.web;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.cc.web.annotation.Controller;
import org.cc.web.annotation.RequestMapping;

/**
 * 
 * 
 * @author dixingxing	
 * @date Mar 31, 2012
 */
@Controller
@RequestMapping("")
public class LoginController {
	@RequestMapping(value = "/login")
	public String login() {
		AuthenticationToken token = new UsernamePasswordToken("root", "secret");// username和password是从表单提交过来的
		Subject currentUser = SecurityUtils.getSubject();
		currentUser.login(token);

		return "secure.jsp";
	}

	@RequestMapping("/logout")
	public String logout() {
		Subject currentUser = SecurityUtils.getSubject();
		currentUser.logout();

		return "secure.jsp";
	}
}
