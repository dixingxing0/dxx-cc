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
import org.cc.db.common.DbUtils;

/**
 * <p>
 * aop拦截时执行before 和 after的类（判断并开始和结束事务）
 * </p>
 * 
 * @author dixingxing
 * @date Apr 26, 2012
 */
@SuppressWarnings("serial")
public final class TxHandler {
	private static AopTxProvider p = new AopTxProvider();

	private TxHandler(){}
	
	/**
	 * 
	 * <p>前置方法</p>
	 *
	 * @param methodInfo
	 */
	public static void before(String methodInfo) {
		Method m = getMethod(methodInfo);
		//判断是否需要开始一个新事务 
		if (AopTxProvider.needToNewTx() ) {
			Connection conn = DbUtils.getConnProvider().getConn();
			p.putConnection(m, conn);
		}
	}

	/**
	 * 
	 * <p>后置方法</p>
	 *
	 * @param methodInfo
	 */
	public static void after(String methodInfo) {
		Method method = getMethod(methodInfo);
		TxContext context = AopTxProvider.getContext();
		// 如果事务不是自己开启的则不能对当前事务进行操作。
		if(context == null || !context.isOwner(method)) {
			return;
		}
		Transactional tran = getDefinition(method);
		if (tran == null || tran.readonly()) {
			p.rollback(method);
		} else {
			p.commit(method);
		}
	}

	/**
	 * <p>
	 * 根据methodInfo创建java.lang.reflect.Method
	 * </p>
	 * 
	 * @param method
	 */
	private static Method getMethod(String method) {
//		LOG.debug("method info : " + method);
		// 参数类型
		String desc = method.substring(method.indexOf('|') + 1);
		String temp= method.substring(0,method.indexOf('|'));
		// 类名
		String clazz = temp.substring(0, temp.lastIndexOf('.'));
		// 方法名
		String name = temp.substring(temp.lastIndexOf('.') + 1);
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
