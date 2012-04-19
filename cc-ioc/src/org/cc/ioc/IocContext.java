/**
 * IocContext.java 4:00:26 PM Apr 7, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.ioc;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.cc.core.CcException;
import org.cc.core.common.ReflectUtils;
import org.cc.core.common.ScanUtils;
import org.cc.ioc.annotation.Inject;
import org.cc.ioc.annotation.Ioc;

/**
 * <li>ioc 容器 所有对象都是单例的</li>
 * <li>保证加Ioc 的类要有默认的构造器</li>
 * <li>不根据setter注入</li>
 * 
 * <li>注入接口必须保证该接口的实现类有且仅有一个定义了 Ioc注解</li>
 * TODO 循环依赖
 * 
 * @author dixingxing
 * @date Apr 7, 2012
 */
public final class IocContext {
	private static final Logger LOG = Logger.getLogger(IocContext.class);
	
	/**
	 * 存储实例化好的bean
	 */
	private static Map<Class<?>, Object> map = new HashMap<Class<?>, Object>();
	
	/**
	 * 存储接口和实现类
	 */
	public static final Map<Class<?>,List<Class<?>>> I_MAP = new HashMap<Class<?>,List<Class<?>>>();
	
	/** 一个计数器 看注入bean的个数 */
	private static int instanceCount = 0;
	
	/**
	 * 一个标记，防止重复初始化
	 */
	private static boolean initialized = false;

	private IocContext() {}
	
	/**
	 * 
	 * @param <T>
	 * @param clazz 可以是接口类
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(Class<T> clazz) {
		return get(clazz, false);
	}
	
	/**
	 * 初始化容器
	 */
	private static void init() {
		LOG.debug("开始初始化IocContext");
		initialized = true;
		long start = System.currentTimeMillis();
		String packages = IocConfig.getIocPackages();
		if(packages == null || "".equals(packages)) {
			throw new CcException("没有指定IocPackages");
		}
		ScanUtils helper = new ScanUtils(true, true, null);
		
		// 获取指定包路径下的所有class文件
		Set<Class<?>> clazzSet = new LinkedHashSet<Class<?>>();
		for(String pkg : packages.split(",")) {
			if(pkg != null && !pkg.equals("")) {
				LOG.debug(String.format("扫描 %s 包",pkg));
				clazzSet.addAll(helper.getPackageAllClasses(pkg,true));
			}
		}
		
		initInterfaceMap(clazzSet);
		LOG.debug(String.format("扫描%d个class文件,其中有%d个接口",clazzSet.size(),I_MAP.size()));
		
		for (Class<?> clazz : clazzSet) {
			if (clazz.isAnnotation() || clazz.isInterface()) {
				continue;
			}
			// getInstance 方法也会往map中放入实例好的对象,这里避免重复实例化
			if(!map.containsKey(clazz)) {
				add(clazz, getInstance(clazz));
			}
		}
		LOG.debug(String.format("初始化IocContext完毕，实例化%d个对象，耗时%d毫秒", instanceCount,System.currentTimeMillis() - start));
	}
	
	/**
	 * 遍历所有class 构造iMap
	 * 
	 * @param clazzSet
	 */
	@SuppressWarnings("unchecked")
	private static void initInterfaceMap(Set<Class<?>> clazzSet) {
		// 把所有接口放到iMap中
		for (Class<?> clazz : clazzSet) {
			if(clazz.isInterface() && !I_MAP.containsKey(clazz)) {
				I_MAP.put(clazz, new ArrayList());
			}
		}
		// 再次遍历
		for (Class<?> clazz : clazzSet) {
			// 不再处理接口 ，不处理没有标注Ioc注解的类
			if(clazz.isInterface() || !clazz.isAnnotationPresent(Ioc.class)) {
				continue;
			}
			Class<?>[] interfaces = clazz.getInterfaces();
			// 如果类实现了接口 那么对应的放到iMa中
			for(Class<?> i : interfaces) {
				if(I_MAP.containsKey(i)) {
					List<Class<?>> impls = I_MAP.get(i);
					if(!impls.contains(clazz)) {
						impls.add(clazz);
					}
				}
			}
		}
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
		Field[] fields = ReflectUtils.getNotFinalFields(clazz);
		T obj = getInside(clazz);
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
		obj = newInstance(clazz);
		
		for (Field f : fields) {
			// 不处理
			if (!f.isAnnotationPresent(Inject.class)) {
				continue;
			}
			Class<?> c = f.getType();
			
			if(c.isInterface()) {
				// 取出接口的实现类
				List<Class<?>> impls = I_MAP.get(c);
				if(impls == null || impls.size() == 0) {
					LOG.debug(String.format("没有找到%s的实现类，或者实现类没有定义Ioc注解",c.getName()));
					continue;
				} else  if (impls.size() > 1) {
					StringBuilder sb = new StringBuilder();
					for(Class<?> impl : impls) {
						sb.append(impl.getName()).append(",");
					}
					
					LOG.debug(String.format("找到多个%s的实现类 %s (都有Ioc注解)，不能确定使用哪个实现类",c.getName(),sb.toString()));
					continue;
				} else {
					Class<?> impl = impls.get(0);
					Object fValue = getInside(impl);
					if(fValue == null) {
						fValue = getInstance(impl);
						// 要把fValue放入容器中，避免重复实例化
						add(impl, fValue);
					}
					
					ReflectUtils.set(obj, f, fValue);
					LOG.debug(String.format("%d注入%s.%s (实现类-%s)", ++instanceCount,clazz.getSimpleName(),f.getName(),impl.getSimpleName()));
				}
				
				continue;
			}
			
			// 如果要注入的类没有Ioc标记
			if(!c.isAnnotationPresent(Ioc.class)) {
				String error = String.format("%s 没有定义Ioc Annotation，不能注入到 %s.%s", 
						c.getName(),clazz.getName(),f.getName());
				LOG.error(error);
				throw new CcException(error);
			}
			
			// 取容器中已实例化好的对象
			Object fValue = getInside(c);
			// 如果还没有实例化，则递归方法本身进行实例化
			if(fValue == null) {
				fValue = getInstance(c);
				// 要把fValue放入容器中，避免重复实例化
				add(c, fValue);
			}
			
			ReflectUtils.set(obj, f, fValue);
			instanceCount++;
			LOG.debug(String.format("%d注入%s.%s", instanceCount,clazz.getSimpleName(),f.getName()));
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

	/**
	 * 
	 * @param <T>
	 * @param clazz 可以是接口类
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static <T> T get(Class<T> clazz,boolean inside) {
		if(!inside && !initialized) {
			init();
		}
		
		if(clazz.isInterface()) {
			List<Class<?>> impls = I_MAP.get(clazz);
			if(impls.size() == 1) {
				return (T) map.get(impls.get(0));
			}
		}
		T obj =  (T) map.get(clazz);
		for(Decorator d : IocConfig.getDecorators()) {
			obj = (T) d.doDecorator(obj);
		}
		return obj;
	}
	
	/**
	 * 在 init 方法中调用
	 * 
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	private static <T> T getInside(Class<T> clazz) {
		return get(clazz, true);
	}
	
}
