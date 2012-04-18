package org.cc.core.common;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import org.cc.core.CcException;

/**
 * 反射工具类
 * 
 * @author dixingxing
 * @date Feb 8, 2012
 */
public final class ReflectUtils {
	
	private ReflectUtils(){}
	/**
	 * 获取obj对象fieldName的Field
	 * 能获取父类的属性，不能访问接口中的属性
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	public static Field getFieldByName(Object obj, String fieldName) {
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				return superClass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
			}
		}
		return null;
	}

	/**
	 * 获取所有field，包括父类(但不包括Object类和接口中的属性) ,不包括常量static,final
	 * 
	 * @param clazz
	 * @return
	 */
	public static Field[] getVariableFields(Class<?> clazz) {
		ArrayList<Field> list = new ArrayList<Field>();
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			Field[] fields = superClass.getDeclaredFields();
			for (Field f : fields) {
				if (!isConstant(f)) {
					list.add(f);
				}
			}
		}
		return list.toArray(new Field[0]);

	}
	
	/**
	 * 获取所有field，包括父类(但不包括Object类和接口中的属性) ,不包括final属性
	 * 
	 * @param clazz
	 * @return
	 */
	public static Field[] getNotFinalFields(Class<?> clazz) {
		ArrayList<Field> list = new ArrayList<Field>();
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try{
			Field[] fields = superClass.getDeclaredFields();
			for (Field f : fields) {
				if (!Modifier.isFinal(f.getModifiers())) {
					list.add(f);
				}
			}
			}catch (Exception e) {
				break;
			}
		}
		return list.toArray(new Field[0]);

	}
	

	/**
	 * 获取obj对象fieldName的Field
	 * 能获取父类的属性，不能访问接口中的属性
	 * 
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	public static Object getValueByFieldName(Object obj, String fieldName) {
		try {
			return get(obj, getFieldByName(obj, fieldName));
		} catch (Exception e) {
			throw new CcException(e);
		}
	}

	/**
	 * 设置对象指定属性的值
	 * 
	 * @param obj
	 * @param f
	 * @param value
	 */
	public static void set(Object obj, Field f, Object value) {
		try {
			if (f.isAccessible()) {
				f.set(obj, value);
			} else {
				f.setAccessible(true);
				f.set(obj, value);
				f.setAccessible(false);
			}
		} catch (Exception e) {
			throw new CcException(e);
		}
	}

	/**
	 * 
	 * 获取对象的指定属性
	 * 
	 * @param obj
	 * @param f
	 * @return the obj
	 */
	public static Object get(Object obj, Field f) {
		if (f == null) {
			return null;
		}
		Object value;
		try {
			if (f.isAccessible()) {
				value = f.get(obj);
			} else {
				f.setAccessible(true);
				value = f.get(obj);
				f.setAccessible(false);
			}
		} catch (Exception e) {
			throw new CcException(e);
		}
		return value;
	}

	/**
	 * 设置对象指定属性的值
	 * 
	 * @see #getFieldByName(Object, String)
	 * 
	 * @param obj
	 * @param fieldName
	 * @param value
	 */
	public static void set(Object obj, String fieldName, Object value) {
		try {
            Field f = getFieldByName(obj, fieldName);
			if(f == null) {
				return;
			}
			if (f.isAccessible()) {
				f.set(obj, value);
			} else {
				f.setAccessible(true);
				f.set(obj, value);
				f.setAccessible(false);
			}
		} catch (Exception e) {
			throw new CcException(e);
		}
	}

	/**
	 * 此属性是否是常量(static 或者 final)
	 * 
	 * 
	 * @param f
	 * @return
	 */
	private static boolean isConstant(Field f) {
		return Modifier.isStatic(f.getModifiers())
				|| Modifier.isFinal(f.getModifiers());
	}
	
	
	
	/**
	 * 获取所有method，包括父类(但不包括Object类的方法)
	 * 
	 * @param clazz
	 * @return
	 */
	public static Method[] getVariableMethods(Class<?> clazz) {
		ArrayList<Method> list = new ArrayList<Method>();
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			Method[] methods = superClass.getDeclaredMethods();
			for (Method m : methods) {
				list.add(m);
			}
		}
		return list.toArray(new Method[0]);

	}
}