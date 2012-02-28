package org.cc.core.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 加载application.properties中的配置到对应的属�??<br>
 * 
 * @author dixingxing
 * @CreateDate 2011-4-29
 */
public class Config {
	private static Logger logger = Logger.getLogger(Config.class);

	public static String jdbc_driver_class_name = null;
	public static String jdbc_user_name = null;
	public static String jdbc_password = null;
	public static String jdbc_url = null;
	public static boolean isMysql() {
		return true;
	}
	public static boolean isOracle() {
		return !isMysql();
	}

	static {
		loadProperities("test.properties");
	}

	private static void loadProperities(String fileName) {
		try {
			Properties p = new Properties();
			InputStream in = Config.class.getResourceAsStream("/" + fileName);
			p.load(in);
			Config config = new Config();
			for (Object s : p.keySet()) {
				String key = s.toString();
				try {
					ReflectUtils.set(config, key, p.get(key));
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		} catch (IOException e) {
			logger.debug("加载文件错误", e);
		}
	}

	public static void main(String[] args) {
		System.out.println(Config.jdbc_driver_class_name);
	}
}
