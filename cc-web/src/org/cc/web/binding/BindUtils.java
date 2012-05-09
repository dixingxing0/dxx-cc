/**
 * Utils.java 3:29:42 PM May 9, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.web.binding;

import org.apache.log4j.Logger;
import org.cc.core.common.Classes;
import org.cc.core.common.Strings;
import org.cc.web.WebConfig;

/**
 * <p>获取绑定各实现类的工具类</p>
 * 
 * @author dixingxing	
 * @date May 9, 2012
 */
public final class BindUtils {
	private static final Logger LOG = Logger.getLogger(BindUtils.class);
	
	private static Binder binder;
	private static PathVarBinder pathVarBinder;
	private static ObjectBuilder objectBuilder;
	
	private BindUtils() {}
	
	/**
	 * 获取{@link Binder}的实现类
	 * 
	 * @return
	 */
	public static Binder getBinder() {
		if(binder != null) {
			return binder;
		}
		String name = WebConfig.getBinder();
		
		if(Strings.isNotBlank(name)) {
			LOG.debug(String.format("自定义Binder : %s", name));
			binder = Classes.newInstance(name);
		} else {
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
		
		if(pathVarBinder != null) {
			return pathVarBinder;
		}
		String name = WebConfig.getPathVarBinder();
		
		if(Strings.isNotBlank(name)) {
			LOG.debug(String.format("自定义PathVarBinder : %s", name));
			pathVarBinder = Classes.newInstance(name);
		} else {
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
		if(objectBuilder != null) {
			return objectBuilder;
		}
		String name = WebConfig.getObjectBuilder();
		
		if(Strings.isNotBlank(name)) {
			LOG.debug(String.format("自定义ObjectBuilder : %s", name));
			objectBuilder = Classes.newInstance(name);
		} else {
			objectBuilder = new ObjectBuilderImpl();
		}
		return objectBuilder;
	}
}
