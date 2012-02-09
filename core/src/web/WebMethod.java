/**
 * WebMethod.java 12:09:02 PM Feb 7, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package web;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import web.annotation.RequestMethod;

/**
 * web请求映射规则
 * 
 * @author dixingxing
 * @date Feb 7, 2012
 */
public class WebMethod {
	private final static Logger logger = Logger.getLogger(WebMethod.class);
	public Object handler;
	public Method method;

	public String[] urlPathMain;

	public String[] urlPath;

	public RequestMethod[] requestMethod;

	public boolean isResponseBody = false;

	public boolean match(HttpServletRequest request) {
		String servletPath = request.getServletPath();
		if (!match(servletPath)) {
			return false;
		}
		if (requestMethod == null || requestMethod.length == 0) {
			return true;
		}
		for (RequestMethod rm : requestMethod) {
			if (String.valueOf(rm).equalsIgnoreCase(request.getMethod())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * 是否匹配当前请求，如果匹配则由此当前方法处理请求
	 * 
	 * @param servletPath
	 * @return
	 */
	public boolean match(String servletPath) {
		for (String main : urlPathMain) {
			if (urlPath == null || urlPath.length == 0) {
				Pattern p = Pattern.compile(main);
				Matcher m = p.matcher(servletPath);
				if (m.matches()) {
					return true;
				}
			}
			for (String url : urlPath) {
				Pattern p = Pattern.compile(main + url);
				Matcher m = p.matcher(servletPath);
				if (m.matches()) {
					return true;
				}
			}
		}
		return false;
	}

	private static String a2s(Object[] a) {
		if (a == null || a.length == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (Object s : a) {
			sb.append(s).append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("method : ").append(method.getName()).append(",urlPathMain:")
				.append(a2s(urlPathMain));
		sb.append(",urlPath:").append(a2s(urlPath)).append(",requestMethod:")
				.append(a2s(requestMethod));
		return sb.toString();
	}

	/**
	 * 解析path variable
	 * 
	 * @param servletPath
	 * @return
	 */
	public String[] getPathVars(String servletPath) {
		for (String main : urlPathMain) {
			if (urlPath == null || urlPath.length == 0) {
				Pattern p = Pattern.compile(main);
				Matcher m = p.matcher(servletPath);
				if (m.matches()) {
					return getMatchResults(m);
				}
			}
			for (String url : urlPath) {
				Pattern p = Pattern.compile(main + url);
				Matcher m = p.matcher(servletPath);
				if (m.matches()) {
					return getMatchResults(m);
				}
			}
		}
		return new String[0];
	}

	/**
	 * match groups
	 * 
	 * @param m
	 */
	private static String[] getMatchResults(Matcher m) {
		int count = m.groupCount();
		if (count == 0) {
			return new String[0];
		}
		String[] s = new String[count];
		for (int i = 0; i < m.groupCount(); i++) {
			s[i] = m.group(i + 1);
		}
		logger.debug("解析出pathVariable :" + StringUtils.join(s, ","));
		return s;
	}

	public static void main(String[] args) {
		String rex = "/ad/a([a-z])([a-z])x";
		String servletPath = "/ad/ajax";
		Pattern p = Pattern.compile(rex);
		Matcher m = p.matcher(servletPath);
		if (m.matches()) {
			getMatchResults(m);
		}
	}

}
