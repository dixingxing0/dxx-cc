/**
 * Copyright (c) 2005-2012 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package org.cc.demo.test;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * 创建Jetty Server的工厂类.
 * 
 * @author calvin
 */
public class JettyUtils {

	private static Server server = null;
	
	static {
		server = buildNormalServer(1988, "");
	}

	/**
	 * 创建用于开发运行调试的Jetty Server, 以web目录为Web应用目录.
	 */
	private static Server buildNormalServer(int port, String contextPath) {
		Server server = new Server(port);
		WebAppContext webContext = new WebAppContext("web", contextPath);
		server.setHandler(webContext);
		server.setStopAtShutdown(true);
		return server;
	}
	
	/**
	 * 启动调试用的jetty server
	 */
	public static void start() {
		if(server.isRunning()) {
			return;
		}
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
