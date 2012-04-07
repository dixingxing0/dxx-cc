/**
 * IocContext.java 4:00:26 PM Apr 7, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.ioc;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.cc.core.common.CcException;
import org.cc.core.common.ReflectUtils;
import org.cc.core.common.ScanUtils;
import org.cc.ioc.annotation.Inject;
import org.cc.ioc.annotation.Ioc;

/**
 * ioc 容器 所有对象都是单例的
 * 保证加Ioc 的类要有默认的构造器
 * 不根据setter注入
 * TODO 接口
 * TODO 循环依赖
 * 
 * @author dixingxing
 * @date Apr 7, 2012
 */
public final class IocContext {
	private static final Logger LOG = Logger.getLogger(IocContext.class);
	
	private static Map<Class<?>, Object> map = new HashMap<Class<?>, Object>();

	private IocContext() {}
	
	/**
	 * 初始化容器
	 */
	public static void init() {
		ScanUtils helper = new ScanUtils(true, true, null);

		Set<Class<?>> calssList = helper.getPackageAllClasses("org.cc.ioc",
				true);

		for (Class<?> clazz : calssList) {
			if (clazz.isAnnotation() || clazz.isInterface()) {
				continue;
			}
			// getInstance 方法也会往map中放入实例好的对象,这里避免重复实例化
			if(!map.containsKey(clazz)) {
				add(clazz, getInstance(clazz));
			}
		}
	}

	/**
	 * 清空容器内所有实例
	 */
	public static synchronized void clear() {
		map = new HashMap<Class<?>, Object>();
	}
	
	/**
	 * 把实例好的对象放入容器中
	 * 
	 * @param clazz
	 * @param obj
	 */
	private static synchronized void add(Class<?> clazz,Object obj) {
		if(!map.containsKey(clazz)) {
			map.put(clazz, obj);
		}
	}

	/**
	 * 实例化指定类型的对象
	 * 
	 * @param clazz
	 */
	private static <T> T getInstance(Class<T> clazz) {
		Field[] fields = ReflectUtils.getVariableFields(clazz);
		T obj = get(clazz);
		if(obj != null) {
			return obj;
		}
		
		// 不需要注入则直接返回clazz.newInstance()
		if (!needInject(fields)) {
			// 如果和ioc完全没有关系 则不用实例化 (既没有标记Inject 也没有标记 ioc ) 
			if(!clazz.isAnnotationPresent(Ioc.class)) {
				return null;
			}
			
			obj = newInstance(clazz);
			return obj;
		}
		LOG.debug(String.format("构造 %s 对象",clazz.getSimpleName()));
		obj = newInstance(clazz);
		
		StringBuilder sb = new StringBuilder();
		sb.append(clazz.getSimpleName()).append("的属性需要注入---------------\r\n");
		
		for (Field f : fields) {
			// 不处理
			if (!f.isAnnotationPresent(Inject.class)) {
				continue;
			}
			Class<?> c = f.getType();
			
			if(c.isInterface()) {
				LOG.debug("不能注入接口");
			}
			
			// 如果要注入的类没有Ioc标记
			if(!c.isAnnotationPresent(Ioc.class)) {
				String error = String.format("%s 没有定义Ioc Annotation，不能注入到 %s.%s", 
						c.getName(),clazz.getName(),f.getName());
				LOG.error(error);
				throw new CcException(error);
			}
			
			// 取容器中已实例化好的对象
			Object fValue = get(c);
			// 如果还没有实例化，则递归方法本身进行实例化
			if(fValue == null) {
				fValue = getInstance(c);
				// 要把fValue放入容器中，避免重复实例化
				add(c, fValue);
			}
			sb.append("                                      ");
			sb.append(f.getName()).append(":").append(fValue);
			ReflectUtils.set(obj, f, fValue);
			LOG.debug(sb.toString());
		}
		return obj;
	}

	/**
	 * <p>
	 * 调用默认构造方法实例化对象
	 * </p>
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	private static <T> T newInstance(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new CcException(e);
		}
	}

	/**
	 * 判断是否需要注入
	 * 
	 * @param fields
	 * @return
	 */
	private static boolean needInject(Field[] fields) {
		for (Field f : fields) {
			if (f.isAnnotationPresent(Inject.class)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(Class<T> clazz) {
		return (T) map.get(clazz);
	}
}
