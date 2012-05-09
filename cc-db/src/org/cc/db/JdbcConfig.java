package org.cc.db;

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
	private static final Logger LOG = Logger.getLogger(JdbcConfig.class);
	public static final String JDBC_CONFIG_FILE = "jdbc.properties";
	private static Properties p;

	private static String driverClassName;
	private static String userName;
	private static String password;
	private static String url;

	private static String connectionProvider;
	private static String jndiDataSourceName = null;

	private static String transactionProvider;
	
	private JdbcConfig() {
	}

	/**
	 * 初始化配置文件
	 * 
	 * @param fileName
	 */
	public static void init(String fileName) {
		if(p!=null) {
			return;
		}
		try {
			p = new Properties();
			InputStream in = JdbcConfig.class.getResourceAsStream("/"+ fileName);
			p.load(in);
			JdbcConfig config = new JdbcConfig();
			for (Object s : p.keySet()) {
				String key = s.toString();
				ReflectUtils.set(config, key, p.get(key));
			}
		} catch (Exception e) {
			LOG.error(fileName, e);
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

	public static String getConnectionProvider() {
		return connectionProvider;
	}
	
	public static String getJndiDataSourceName() {
		return jndiDataSourceName;
	}

	public static String getTransactionProvider() {
		return transactionProvider;
	}
}
