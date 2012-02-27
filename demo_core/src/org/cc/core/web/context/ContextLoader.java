/**
 * ContextLoader.java 11:55:24 AM Feb 7, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.core.web.context;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.cc.core.common.ScanUtils;
import org.cc.core.web.WebException;
import org.cc.core.web.WebMethod;
import org.cc.core.web.annotation.Controller;
import org.cc.core.web.annotation.RequestMapping;
import org.cc.core.web.annotation.ResponseBody;



/**
 * 初始化web环境
 * 
 * @author dixingxing
 * @date Feb 7, 2012
 */
public class ContextLoader implements ServletContextListener {
	private final static Logger logger = Logger.getLogger(ContextLoader.class);

	public void contextDestroyed(ServletContextEvent arg0) {
		logger.debug("销毁servletContext!");

	}

	public void contextInitialized(ServletContextEvent arg0) {
		logger.debug("初始化servletContext!");
		ScanUtils helper = new ScanUtils(true, true, null);

		Set<Class<?>> calssList = helper.getPackageAllClasses("web", true);
		try {

			for (Class<?> clazz : calssList) {
				if (clazz.isAnnotationPresent(Controller.class)) {
					addMappings(clazz.newInstance());
				}
			}
		} catch (Exception e) {
			System.out.println("11111");
			throw new WebException("解析controller时出错", e);
		}
	}

	/**
	 * 增加映射关系到WebContext
	 * 
	 * @param clazz
	 * @return
	 */
	private static List<WebMethod> addMappings(Object handler) {
		Class<?> clazz = handler.getClass();
		List<WebMethod> list = new ArrayList<WebMethod>();
		Method[] methods = clazz.getDeclaredMethods();

		String[] urlPathMain = null;
		if (clazz.isAnnotationPresent(RequestMapping.class)) {
			RequestMapping rm = clazz.getAnnotation(RequestMapping.class);
			urlPathMain = rm.value();
		}
		for (Method m : methods) {
			if (m.isAnnotationPresent(RequestMapping.class)) {

				RequestMapping rm = m.getAnnotation(RequestMapping.class);
				WebMethod webMethod = new WebMethod();
				webMethod.handler = handler;
				webMethod.method = m;
				webMethod.urlPathMain = urlPathMain;
				webMethod.urlPath = rm.value();
				webMethod.requestMethod = rm.method();
				webMethod.isResponseBody = m
						.isAnnotationPresent(ResponseBody.class);
				logger.debug("初始化url映射 - " + clazz.getName() + "."
						+ m.getName() + ":" + webMethod);
				WebContext.addMapping(webMethod);
			}
		}

		return list;
	}

}
