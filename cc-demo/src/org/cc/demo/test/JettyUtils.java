/**
 * Copyright (c) 2005-2012 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package org.cc.demo.test;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * jetty 工具类
 * 
 * 
 * @author dixingxing	
 * @date Apr 6, 2012
 */
public final class JettyUtils {
	private static final Logger LOG = Logger.getLogger(JettyUtils.class);

	private static final int PORT = 1988;
	
	public static final String BASE_URL;
	
	private static Server server = null;
	
	private JettyUtils() {}
	
	static {
		BASE_URL = "http://localhost:" + PORT;
		buildNormalServer(PORT, "");
	}

	/**
	 * 创建用于开发运行调试的Jetty Server, 以web目录为Web应用目录.
	 */
	private static Server buildNormalServer(int port, String contextPath) {
		server = new Server(port);
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
			LOG.error("启动jetty server 出错", e);
		}
	}
}
