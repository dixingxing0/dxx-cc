package org.cc.web;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.cc.core.common.ReflectUtils;

/**
 * 加载web配置文件中的配置
 * 
 * 
 * @author dixingxing	
 * @date Mar 30, 2012
 */
public final class WebConfig {
	private static final Logger LOG = Logger.getLogger(WebConfig.class);
	private static final String WEB_CONFIG_FILE= "web.properties";

	/** controller 所在的包名 */
	private static String controllerLocation;
	
	private static String viewLocation;
	
	private static String binder;
	
	private static String pathVarBinder;

	private static String objectBuilder;
	
	private WebConfig() {}
	
	public static void init() {
		init(WEB_CONFIG_FILE);
	}
	
	/**
	 * 初始化配置文件
	 * 
	 * @param fileName
	 */
	public static void init(String fileName) {
		try {
			Properties p = new Properties();
			InputStream in = WebConfig.class.getResourceAsStream("/" + fileName);
			p.load(in);
			WebConfig config = new WebConfig();
			for (Object s : p.keySet()) {
				String key = s.toString();
				String value = p.getProperty(key);
				ReflectUtils.set(config, key, value);
			}
		} catch (Exception e) {
			LOG.debug(WEB_CONFIG_FILE, e);
		} 
		
	}

	public static String getControllerLocation() {
		return controllerLocation;
	}
	
	public static String getViewLocation() {
		return viewLocation;
	}

	public static String getBinder() {
		return binder;
	}

	public static String getPathVarBinder() {
		return pathVarBinder;
	}

	public static String getObjectBuilder() {
		return objectBuilder;
	}
	
}
