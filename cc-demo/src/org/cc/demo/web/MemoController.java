package org.cc.demo.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.cc.core.common.Page;
import org.cc.core.common.UploadUtils;
import org.cc.core.web.Model;
import org.cc.core.web.annotation.Controller;
import org.cc.core.web.annotation.PathVar;
import org.cc.core.web.annotation.RequestMapping;
import org.cc.core.web.annotation.RequestMethod;
import org.cc.core.web.annotation.ResponseBody;
import org.cc.demo.domain.Memo;
import org.cc.demo.json.JsonUtils;

/**
 * 
 * 
 * @author dixingxing
 * @date Feb 7, 2012
 */
@Controller
@RequestMapping("/memo")
public class MemoController {
	private static final Logger LOG = Logger.getLogger(MemoController.class);
	
	@RequestMapping(method = RequestMethod.GET)
	public String testAjax(Model model, Memo memo) {
		Memo memo2 = new Memo();
		memo2.setId(2L);
		memo2.setName("memo2");
		model.addAttribute(memo2);
		model.addAttribute("hello", "hello world");

		Page<Memo> page = Memo.DB.queryPage("select * from memo", 1, Page.DEFAULT_SIZE);
		model.addAttribute("pageStr", page.toString());
		model.addAttribute("page", page);
		return "welcome.jsp";
	}

	@ResponseBody
	@RequestMapping("/a(\\w)(.*)x/(\\d+)")
	public String ajax(@PathVar(1) Date d, @PathVar(2)
	long id) {
		LOG.debug(d + " " + id);
		return "这是个ajax请求";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String save(HttpServletRequest request) {
		String s = UploadUtils.upload(request);
		LOG.debug("上传文件:" + s);
		return "welcome.jsp";
	}

	@ResponseBody
	@RequestMapping("/json")
	public String json() {
		List<Memo> memos = new ArrayList<Memo>();
		
		Memo m1 = new Memo();
		m1.setCreateTime("2011-11-11");
		m1.setId(1L);
		m1.setName("memo1");
		
		Memo m2 = new Memo();
		m2.setCreateTime("2011-11-12");
		m2.setId(2L);
		m2.setName("memo2");
		
		memos.add(m1);
		memos.add(m2);
		
		return JsonUtils.toJson(memos);
	}
}