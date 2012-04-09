/**
 * WebMethod.java 12:09:02 PM Feb 7, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.web;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.cc.web.annotation.RequestMapping;
import org.cc.web.annotation.RequestMethod;


/**
 * controller中方法的封装，是处理web请求的最终单位
 * 
 * @author dixingxing
 * @date Feb 7, 2012
 */
public class WebMethod {
	private static final Logger LOG = Logger.getLogger(WebMethod.class);
	
	/** controller */
	private Object handler;
	
	/** the original method */
	private Method method;

	/** {@link RequestMapping#value()} on handler*/
	private String[] urlPathMain;

	/** {@link RequestMapping#value()} on method*/
	private String[] urlPath;

	/** {@link RequestMapping#method()} */
	private RequestMethod[] requestMethod;

	/** write response as normal string*/
	private boolean responseBody = false;

	/**
	 * 是否匹配web请求
	 * 
	 * @param request
	 * @return
	 */
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
		LOG.debug("解析出pathVariable :" + StringUtils.join(s, ","));
		return s;
	}
	
	
	public Object getHandler() {
		return handler;
	}

	public void setHandler(Object handler) {
		this.handler = handler;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public String[] getUrlPathMain() {
		return urlPathMain;
	}

	public void setUrlPathMain(String[] urlPathMain) {
		if(urlPathMain != null) {
			this.urlPathMain = Arrays.copyOf(urlPathMain, urlPathMain.length);
		}
	}

	public String[] getUrlPath() {
		return urlPath;
	}

	public void setUrlPath(String[] urlPath) {
		if(urlPath != null) {
			this.urlPath = Arrays.copyOf(urlPath, urlPath.length);
		}
	}

	public RequestMethod[] getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(RequestMethod[] requestMethod) {
		if(requestMethod != null) {
			this.requestMethod = Arrays.copyOf(requestMethod, requestMethod.length);
		}
	}

	public boolean isResponseBody() {
		return responseBody;
	}

	public void setResponseBody(boolean responseBody) {
		this.responseBody = responseBody;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("method : ").append(method.getName()).append(",urlPathMain:")
				.append(StringUtils.join(urlPathMain,","));
		sb.append(",urlPath:").append(StringUtils.join(urlPath, ",")).append(",requestMethod:")
				.append(StringUtils.join(requestMethod, ","));
		return sb.toString();
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