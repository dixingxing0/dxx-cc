/**
 * WebContext.java 12:04:14 PM Feb 7, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package web.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

/**
 * 
 * 
 * @author dixingxing
 * @date Feb 7, 2012
 */
public class WebContext {
	private final static Logger LOG = Logger.getLogger(WebContext.class);
	// 
	public final static List<WebMethod> mappings = new ArrayList<WebMethod>();

	// controller ��ʵ��
	public final static Map<String, Object> handlers = new HashMap<String, Object>();

	/**
	 * ���ӳ���ȡ��Ӧ��webMethod
	 * 
	 * 
	 * @param request
	 * @return
	 */
	public static WebMethod getHandler(HttpServletRequest request) {
		for (WebMethod m : mappings) {
			if (m.match(request)) {
				LOG.debug("ƥ�䵽handler ��" + m);
				return m;
			}
		}
		return null;
	}
}
