package org.cc.core.common;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

/**
 * 反射工具类
 * 
 * @author dixingxing
 * @date Feb 8, 2012
 */
public class ReflectUtils {
	/**
	 * 获取obj对象fieldName的Field
	 * 
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
	 * 获取�?有field，包括父�?(但不包括Object�?) ,不包括常量static,final
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

	public static Object getValueByFieldName(Object obj, String fieldName) {
		try {
			return get(obj, getFieldByName(obj, fieldName));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 设置对象指定属�?�的�?
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
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * 获取对象的指定属�?
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
			throw new RuntimeException(e);
		}
		return value;
	}

	/**
	 * 设置对象指定属�?�的�?
	 * 
	 * @param obj
	 * @param fieldName
	 * @param value
	 */
	public static void set(Object obj, String fieldName, Object value) {
		try {
			Field f = obj.getClass().getField(fieldName);
			if (f.isAccessible()) {
				f.set(obj, value);
			} else {
				f.setAccessible(true);
				f.set(obj, value);
				f.setAccessible(false);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 此属性是否是常量(static 或�?? final)
	 * 
	 * 
	 * @param f
	 * @return
	 */
	private static boolean isConstant(Field f) {
		return Modifier.isStatic(f.getModifiers())
				|| Modifier.isFinal(f.getModifiers());
	}
}