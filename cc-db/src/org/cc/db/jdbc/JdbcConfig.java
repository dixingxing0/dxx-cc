package org.cc.db.jdbc;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.cc.core.common.ReflectUtils;
import org.cc.db.transaction.Provider;

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
	private static Properties p  = null;

	private static String driverClassName = null;
	private static String userName = null;
	private static String password = null;
	private static String url = null;

	private static String connectionProviderClassName = null;
	private static ConnectionProvider connectionProvider = null;

	private static String jndiDataSourceName = null;
	
	private static String transactionProviderClassName = null;
	private static Provider transactionProvider = null;

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
				if ("connectionProvider".equals(key)) {
					connectionProviderClassName = (String) p.get(key);
				} else if ("transactionProvider".equals(key)) {
					transactionProviderClassName = (String) p.get(key);
				}  else {
					ReflectUtils.set(config, key, p.get(key));
				}
			}

			initConnectionProvider(connectionProviderClassName);
			initTransactionProvider(transactionProviderClassName);
		} catch (Exception e) {
			LOG.debug(fileName, e);
		}
	}

	/**
	 * 获取配置文件中的transactionProvider
	 * @param providerClassName
	 */
	private static void initTransactionProvider(String providerClassName) {
		if(providerClassName != null && !"".equals(providerClassName)) {
			try {
				transactionProvider = (Provider) Class.forName(providerClassName).newInstance();
			} catch (Exception e) {
				throw new RuntimeException("请配置属性transactionProvider",e);
			} 
		}
		
	}

	private static void initConnectionProvider(String providerClassName) {
		if (JdbcConnectionProvider.class.getName().equals(providerClassName)) {
			LOG.debug("使用jdbcConnectionProvider");
			connectionProvider = new JdbcConnectionProvider(driverClassName,userName,password,url);
		} else if (JndiConnectionProvider.class.getName().equals(
				providerClassName)) {
			LOG.debug("使用jndicConnectionProvider");
			connectionProvider = new JndiConnectionProvider(jndiDataSourceName);
		} else {
			throw new RuntimeException("请配置属性connectionProvider");
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

	public static ConnectionProvider getConnectionProvider() {
		return connectionProvider;
	}

	public static Provider getTransactionProvider() {
		return transactionProvider;
	}
}
