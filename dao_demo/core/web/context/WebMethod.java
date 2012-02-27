/**
 * WebMethod.java 12:09:02 PM Feb 7, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package web.context;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import web.annotation.RequestMethod;

/**
 * 
 * 
 * @author dixingxing
 * @date Feb 7, 2012
 */
public class WebMethod {
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

	private static String array2String(Object[] a) {
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
				.append(array2String(urlPathMain));
		sb.append(",urlPath:").append(array2String(urlPath)).append(
				",requestMethod:").append(array2String(requestMethod));
		return sb.toString();
	}

}
