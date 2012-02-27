package org.cc.demo.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.cc.core.common.Page;
import org.cc.core.common.UploadUtils;
import org.cc.core.web.Model;
import org.cc.core.web.annotation.Controller;
import org.cc.core.web.annotation.PathVar;
import org.cc.core.web.annotation.RequestMapping;
import org.cc.core.web.annotation.RequestMethod;
import org.cc.core.web.annotation.ResponseBody;
import org.cc.demo.po.Ad;
import org.cc.demo.service.AdManager;



/**
 * 
 * 
 * @author dixingxing
 * @date Feb 7, 2012
 */
@Controller
@RequestMapping("/ad")
public class AdController {
	@RequestMapping(method = RequestMethod.GET)
	public String ad(HttpServletRequest request, Model model, Ad ad) {
		System.out.println("web-demo");
		Ad ad2 = new Ad();
		ad2.setId(2L);
		ad2.setName("ad_2");
		model.addAttribute(ad2);
		model.addAttribute("hello", "hello world");

		Page<Ad> page = new AdManager().queryPage("select * from ad", 1, 10);
		model.addAttribute("pageStr", page.toString());
		model.addAttribute("page", page);
		return "welcome.jsp";
	}

	@ResponseBody
	@RequestMapping("/a(\\w)(.*)x/(\\d+)")
	public String ajax(@PathVar(1)
	Date d, @PathVar(2)
	long id) {
		System.out.println(d);
		System.out.println(id);
		return "这是个ajax请求";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String save(HttpServletRequest request) {
		String s = UploadUtils.upload(request);
		System.out.println(s);
		return "welcome.jsp";
	}

	@ResponseBody
	@RequestMapping("/ajax2")
	public String ajax2() {
		return "这是个ajax2请求";
	}

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
