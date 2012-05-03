/**
 * Handler.java 9:29:30 AM Apr 26, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.tx;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.cc.core.common.Exceptions;
import org.cc.db.jdbc.JdbcConfig;

/**
 * <p>
 * </p>
 * 
 * @author dixingxing
 * @date Apr 26, 2012
 */
@SuppressWarnings("serial")
public class TxHandler {
	private static TxProvider p = new TxProvider();

	public static void before(String methodInfo) {
		Method m = getMethod(methodInfo);
		
		//判断是否需要开始一个新事务 
		if (TxProvider.needToNewTx() ) {
			Connection conn = JdbcConfig.getConnectionProvider().getConn();
			p.putConnection(m, conn);
		}
	}

	public static void after(String methodInfo) {
		Method method = getMethod(methodInfo);
		TxContext context = TxProvider.getContext();
		// 如果事务不是自己开启的则不能对当前事务进行操作。
		if(context == null || !context.isOwner(method)) {
			return;
		}
		Transactional tran = getDefinition(method);
		if (tran == null || tran.readonly()) {
			TxProvider.rollback(method);
		} else {
			TxProvider.commit(method);
		}
	}

	/**
	 * <p>
	 * </p>
	 * 
	 * @param method
	 */
	private static Method getMethod(String method) {
//		LOG.debug("method info : " + method);
		String desc = method.substring(method.indexOf('|') + 1);
		method = method.substring(0,method.indexOf('|'));
		String clazz = method.substring(0, method.lastIndexOf('.'));
		String name = method.substring(method.lastIndexOf('.') + 1);
//		LOG.debug(String.format("clazz : %s,name : %s,desc : %s", clazz, name,desc));
		Method m = null;
		try {
			Class<?> cls = Class.forName(clazz);
			m = cls.getMethod(name, getParameterTyps(desc));
		} catch (Exception e) {
			Exceptions.uncheck(e);
		}

		return m;

	}

	/**
	 * 
	 * <p>
	 * 根据asm method desc获取方法的参数类型数组
	 * </p>
	 * 
	 * @param desc
	 * @return
	 */
	private static Class<?>[] getParameterTyps(String desc) {
		if (desc == null || desc.isEmpty()) {
			return null;
		}
		String[] types = desc.split(",");
		Class<?>[] classes = new Class<?>[types.length];
		
		for(int i = 0;i<types.length;i++) {
			try {
				if(PRIMITIVE_MAP.containsKey(types[i])) {
					classes[i] = PRIMITIVE_MAP.get(types[i]);
				} else {
					classes[i] = Class.forName(types[i]);
				}
			} catch (ClassNotFoundException e) {
				Exceptions.uncheck(e);
			}
		}

		return classes;
	}
	


    /** 所有基本类型 */
	private static final Map<String, Class<?>> PRIMITIVE_MAP = new HashMap<String, Class<?>>(){{
		put("boolean",Boolean.TYPE );
		put("char", Character.TYPE); 
		put("byte", Byte.TYPE); 
		put("short", Short.TYPE); 
		put("int", Integer.TYPE); 
		put("long", Long.TYPE); 
		put("float", Float.TYPE); 
		put("double", Double.TYPE); 
		}};
	
	/**
	 * <p>
	 * 获取权限定义{@link Transactional}
	 * </p>
	 * 
	 * @param m
	 * @return
	 */
	public static Transactional getDefinition(Method m) {
		if (m.isAnnotationPresent(Transactional.class)) {
			return m.getAnnotation(Transactional.class);
		}
		return m.getDeclaringClass().getAnnotation(Transactional.class);
	}
}
