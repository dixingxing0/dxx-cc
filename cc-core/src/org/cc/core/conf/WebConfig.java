package org.cc.core.conf;

import java.io.IOException;
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
	private static String controllerLocation = null;
	
	
	static {
		loadProperities(WEB_CONFIG_FILE);
	}

	private WebConfig() {}
	
	/**
	 * 初始化配置文件
	 * 
	 * @param fileName
	 */
	private static void loadProperities(String fileName) {
		try {
			Properties p = new Properties();
			InputStream in = WebConfig.class.getResourceAsStream("/" + fileName);
			p.load(in);
			WebConfig config = new WebConfig();
			for (Object s : p.keySet()) {
				String key = s.toString();
				ReflectUtils.set(config, key, p.get(key));
			}
		} catch (IOException e) {
			LOG.debug(WEB_CONFIG_FILE, e);
		}
	}

	public static String getControllerLocation() {
		return controllerLocation;
	}
}
