package org.cc.ioc;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.cc.core.common.ReflectUtils;

/**
 * <p>加载ioc配置文件中的配置</p>
 * <li>iocPackages : ioc 扫描的包，多个用","分隔</li>
 * 
 * @author dixingxing	
 * @date Mar 30, 2012
 */
public final class IocConfig {
	private static final Logger LOG = Logger.getLogger(IocConfig.class);
	private static final String IOC_CONFIG_FILE= "ioc.properties";
	

	/** 所在的包名 */
	private static String iocPackages;
	
	
	static {
		loadProperities(IOC_CONFIG_FILE);
	}

	private IocConfig() {}
	
	/**
	 * 初始化配置文件
	 * 
	 * @param fileName
	 */
	private static void loadProperities(String fileName) {
		try {
			Properties p = new Properties();
			InputStream in = IocConfig.class.getResourceAsStream("/" + fileName);
			p.load(in);
			IocConfig config = new IocConfig();
			for (Object s : p.keySet()) {
				String key = s.toString();
				String value = p.getProperty(key);
				ReflectUtils.set(config, key, value);
			}
		} catch (Exception e) {
			LOG.debug(IOC_CONFIG_FILE, e);
		} 
		
	}

	/**
	 * ioc 扫描的包，多个用","分隔
	 * 
	 * @return
	 */
	public static String getIocPackages() {
		return iocPackages;
	}
}
