/**
 * Copyright (c) 2005-2012 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package org.cc.demo.test;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * jetty 工具类
 * 
 * 
 * @author dixingxing	
 * @date Apr 6, 2012
 */
public class JettyUtils {

	private static final int port = 1988;
	
	public static String BASE_URL = null;
	
	private static Server server = null;
	
	
	static {
		BASE_URL = "http://localhost:" + port;
		server = buildNormalServer(port, "");
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
