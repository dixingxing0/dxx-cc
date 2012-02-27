package web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import po.Ad;
import service.AdManager;
import web.annotation.Controller;
import web.annotation.PathVar;
import web.annotation.RequestMapping;
import web.annotation.RequestMethod;
import web.annotation.ResponseBody;

import common.Page;

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
	public String save() {
		return "welcome.jsp";
	}
}
