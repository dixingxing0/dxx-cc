package org.cc.core.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.cc.core.common.ReflectUtils;

/**
 * 加载jdbc配置文件中的配置
 * 
 * 
 * @author dixingxing	
 * @date Mar 30, 2012
 */
public final class JdbcConfig {
	private static Logger LOG = Logger.getLogger(JdbcConfig.class);
	private static final String JDBC_CONFIG_FILE= "jdbc.properties";
	
	private static String driverClassName = null;
	private static String userName = null;
	private static String password = null;
	private static String url = null;
	
	static {
		loadProperities(JDBC_CONFIG_FILE);
	}
	
	private JdbcConfig(){}
	

	/**
	 * 初始化配置文件
	 * 
	 * @param fileName
	 */
	private static void loadProperities(String fileName) {
		try {
			Properties p = new Properties();
			InputStream in = JdbcConfig.class.getResourceAsStream("/" + fileName);
			p.load(in);
			JdbcConfig config = new JdbcConfig();
			for (Object s : p.keySet()) {
				String key = s.toString();
				ReflectUtils.set(config, key, p.get(key));
			}
		} catch (IOException e) {
			LOG.debug(JDBC_CONFIG_FILE, e);
		}
	}


	public static String getDriverClassName() {
		return driverClassName;
	}


	public static String getUserName() {
		return userName;
	}


	public static String getPassword() {
		return password;
	}


	public static String getUrl() {
		return url;
	}
	
	
}
