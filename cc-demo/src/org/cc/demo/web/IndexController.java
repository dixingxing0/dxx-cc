package org.cc.demo.web;

import org.cc.web.annotation.Controller;
import org.cc.web.annotation.RequestMapping;

/**
 * 
 * 
 * @author dixingxing
 * @date Feb 7, 2012
 */
@Controller
@RequestMapping("/")
public class IndexController {
	@RequestMapping("")
	public String index() {
		return "index.vm";
	}


}