/**
 * LinkTool.java 3:32:56 PM Apr 6, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.demo.web;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.velocity.tools.view.LinkTool;

/**
 * 自定义的velocity tool
 * 
 * @author dixingxing	
 * @date Apr 6, 2012
 */
public class CcLinkTool extends LinkTool{
	private String pageRegex = "/p(\\d+)";
	private Pattern p = Pattern.compile(pageRegex);
	
	/**
	 * 获取当前页数
	 * 
	 * @param uri
	 * @return
	 */
	public int getPage(String uri) {
		Matcher m = p.matcher(uri);
		while(m.find()) {
			return Integer.valueOf(m.group(1));
		}
		return 1;
	}
	
	/**
	 * 获取分页链接
	 * 
	 * @param uri
	 * @param i 第几页
	 * @return
	 */
	public String getPageLink(String uri,int i) {
		if(uri == null) {
			return null;
		}
		String suffix = "/p";
		uri = uri.replaceFirst(pageRegex, suffix);
		if(!uri.endsWith(suffix)) {
			uri = uri + suffix;
		}
		return uri + i;
	}
}
