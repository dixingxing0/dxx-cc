package org.cc.core.conf;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.cc.core.common.ReflectUtils;
import org.cc.core.web.binder.Binder;
import org.cc.core.web.binder.BinderImpl;
import org.cc.core.web.binder.ObjectBuilderImpl;
import org.cc.core.web.binder.PathVarBinderImpl;
import org.cc.core.web.binder.ObjectBuilder;
import org.cc.core.web.binder.PathVarBinder;

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
		
		if(binder == null) {
			binder = new BinderImpl();
		}
		if(objectBuilder == null) {
			objectBuilder = new ObjectBuilderImpl();
		}
		if(pathVarBinder == null) {
			pathVarBinder = new PathVarBinderImpl();
		}
	}

	public static String getControllerLocation() {
		return controllerLocation;
	}

	public static Binder getBinder() {
		return binder;
	}

	public static void setBinder(Binder binder) {
		WebConfig.binder = binder;
	}

	public static PathVarBinder getPathVarBinder() {
		return pathVarBinder;
	}

	public static void setPathVarBinder(PathVarBinder pathVarBinder) {
		WebConfig.pathVarBinder = pathVarBinder;
	}

	public static ObjectBuilder getObjectBuilder() {
		return objectBuilder;
	}

	public static void setObjectBuilder(ObjectBuilder objectBuilder) {
		WebConfig.objectBuilder = objectBuilder;
	}
}
