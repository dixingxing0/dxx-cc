package org.cc.ioc;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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
	public static final String DEFAULT_IOC_CONFIG_FILE= "ioc.properties";
	
	private static Properties p = null;
	/** 所在的包名 */
	private static String iocPackages;
	
	private static String decoratorNames;
	
	private static List<Decorator> decorators = new ArrayList<Decorator>();
	
	private IocConfig() {}
	
	/**
	 * 
	 * <p>加载默认的配置文件{@link #DEFAULT_IOC_CONFIG_FILE}，并初始化。</p>
	 *
	 */
	public static void init() {
		init(DEFAULT_IOC_CONFIG_FILE);
	}
	
	/**
	 * 初始化配置文件
	 * 
	 * @param fileName
	 */
	public static void init(String fileName) {
		if(p != null) {
			return;
		}
		try {
			p = new Properties();
			InputStream in = IocConfig.class.getResourceAsStream("/" + fileName);
			p.load(in);
			IocConfig config = new IocConfig();
			for (Object s : p.keySet()) {
				String key = s.toString();
				String value = p.getProperty(key);
				if("decorators".equals(key)) {
					decoratorNames = value;
					initDecorators();
				} else {
					ReflectUtils.set(config, key, value);
				}
				
			}
		} catch (Exception e) {
			LOG.debug(DEFAULT_IOC_CONFIG_FILE, e);
		} 
		
	}

	/**
	 * 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * 
	 */
	private static void initDecorators() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		if(decoratorNames == null) {
			return ;
		}
		String[] names  = decoratorNames.split(",");
		for(String name : names) {
			decorators.add((Decorator)Class.forName(name).newInstance());
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

	/**
	 * 
	 * <p>获取配置文件中指定的decorators</p>
	 *
	 * @return
	 */
	protected static List<Decorator> getDecorators() {
		return decorators;
	}
}
