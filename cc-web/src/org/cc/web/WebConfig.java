package org.cc.web;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.cc.core.common.ReflectUtils;
import org.cc.web.binding.Binder;
import org.cc.web.binding.BinderImpl;
import org.cc.web.binding.ObjectBuilder;
import org.cc.web.binding.ObjectBuilderImpl;
import org.cc.web.binding.PathVarBinder;
import org.cc.web.binding.PathVarBinderImpl;

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
	private static final String KEY_BINDER= "binder";
	private static final String KEY_PATH_VAR_BINDER= "pathVarBinder";
	private static final String KEY_OBJECT_BUILDER= "objectBuilder";
	

	/** controller 所在的包名 */
	private static String controllerLocation;
	
	private static Binder binder;
	
	private static PathVarBinder pathVarBinder;

	private static ObjectBuilder objectBuilder;
	
	
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
				String value = p.getProperty(key);
				if(KEY_BINDER.equals(key)) {
					binder = (Binder) Class.forName(value).newInstance();
					LOG.debug("自定义Binder : " + value);
					
				} else if (KEY_OBJECT_BUILDER.equals(key)){
					objectBuilder = (ObjectBuilder) Class.forName(value).newInstance();
					LOG.debug("自定义ObjectBuilder : " + value);
					
				} else if (KEY_PATH_VAR_BINDER.equals(key)){
					pathVarBinder = (PathVarBinder) Class.forName(value).newInstance();
					LOG.debug("自定义PathVarBinder : " + value);
				} else {
					ReflectUtils.set(config, key, value);
				}
			}
		} catch (Exception e) {
			LOG.debug(WEB_CONFIG_FILE, e);
		} 
		
	}

	public static String getControllerLocation() {
		return controllerLocation;
	}

	/**
	 * 获取{@link Binder}的实现类
	 * 
	 * @return
	 */
	public static Binder getBinder() {
		if(binder == null) {
			binder = new BinderImpl();
		}
		return binder;
	}

	/**
	 * 获取{@link PathVarBinder}的实现类
	 * 
	 * @return
	 */
	public static PathVarBinder getPathVarBinder() {
		if(pathVarBinder == null) {
			pathVarBinder = new PathVarBinderImpl();
		}
		return pathVarBinder;
	}

	/**
	 * 获取{@link ObjectBuilder}的实现类
	 * 
	 * @return
	 */
	public static ObjectBuilder getObjectBuilder() {
		if(objectBuilder == null) {
			objectBuilder = new ObjectBuilderImpl();
		}
		return objectBuilder;
	}
}
