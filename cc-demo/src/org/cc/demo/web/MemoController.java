package org.cc.demo.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.cc.core.db.Page;
import org.cc.demo.domain.Memo;
import org.cc.demo.json.JsonUtils;
import org.cc.web.Model;
import org.cc.web.annotation.Controller;
import org.cc.web.annotation.PathVar;
import org.cc.web.annotation.RequestMapping;
import org.cc.web.annotation.RequestMethod;
import org.cc.web.annotation.ResponseBody;
import org.cc.web.common.UploadUtils;

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
	
	@RequestMapping(value = "(/p(\\d+))?" , method = RequestMethod.GET)
	public String list(@PathVar(1) Integer currentPage ,Model model,HttpServletRequest req) {
		Page<Memo> page = Memo.DB.queryPage("select * from memo order by id desc", currentPage, Page.DEFAULT_SIZE);
		
		model.addAttribute("pageStr", page.toString());
		model.addAttribute("pageSql", page.getPageSql());
		model.addAttribute("page", page);
		return "memo/list.vm";
	}
	
	@RequestMapping(value = "/(\\d+)" , method = RequestMethod.GET)
	public String view(@PathVar(0) Long id,Model model) {
		model.addAttribute(Memo.DB.queryById(id));
		return "memo/edit.vm";
	}
	
	@RequestMapping(value = "/add" , method = RequestMethod.GET)
	public String add() {
		return "memo/edit.vm";
	}

	@RequestMapping(method = RequestMethod.POST)
	public void save(Memo memo,HttpServletResponse res) throws IOException {
		// 新增
		if(memo.getId() == null) {
			memo.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			memo.insert();
		} 
		// 修改
		else {
			memo.update();
		}
		// redirect 跳转
		res.sendRedirect("/memo");
	}
	
	@RequestMapping(value = "/d/(\\d+)")
	public void delete(@PathVar(0) Long id,HttpServletResponse res) throws IOException {
		Memo.DB.delete(id);
		res.sendRedirect("/memo");
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String upload(HttpServletRequest request) {
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