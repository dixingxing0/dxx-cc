/*
 * 2012-02-16
 * 1.添加正则验证支持
 * 2.添加自定义message
 * 
 */

package org.cc.core.validation;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Pattern;

public class Validator {

	public static void validate(Object target) {
		if (target == null)
			throw getNullArgException("target");
		validate(target, "target");
	}

	private static void validate(Object target, String fieldName) {
		if (!requireValidate(target))
			return;

		if (target instanceof Collection) {
			validateCollectionElements(target, fieldName);
			return;
		}

		if (target.getClass().isArray()) {
			validateArrayItems(target, fieldName);
			return;
		}

		validateFields(target);
	}

	private static boolean requireValidate(Object target) {
		Class<?> type = target.getClass();

		return !(type.isPrimitive() || type.isEnum()
				|| target instanceof String || target instanceof Number
				|| target instanceof Boolean || target instanceof Byte || target instanceof Charset);
	}

	private static void validateCollectionElements(Object target,
			String fieldName) {

		Iterator<?> it = ((Collection<?>) target).iterator();
		while (it.hasNext()) {
			Object item = it.next();
			if (item == null)
				throw getNullArgException(fieldName + " Collection Items");
			validate(item);
		}
	}

	private static void validateArrayItems(Object target, String fieldName) {
		int arrayLength = Array.getLength(target);
		for (int i = 0; i < arrayLength; i++) {
			Object item = Array.get(target, i);
			if (item == null)
				throw getNullArgException(fieldName + " Array Items");
			validate(item);
		}
	}

	private static void validateFields(Object target) {
		Field[] fields = target.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (Field f : fields) {
				f.setAccessible(true);

				Annotation[] annotations = f.getDeclaredAnnotations();
				if (annotations.length != 0)
					validateFieldByAnnotations(target, f);

				Object value = getValue(f, target);
				// should always check null
				if (value != null)
					validate(value, f.getName());
			}
		}
	}

	private static void validateFieldByAnnotations(Object target, Field f) {

		if (f.isAnnotationPresent(NotNull.class))
			handleNotNull(f, target);

		if (f.isAnnotationPresent(NotEmpty.class))
			handleNotEmpty(f, target);

		if (f.isAnnotationPresent(Size.class))
			handleSize(f, target);

		if (f.isAnnotationPresent(Min.class))
			handleMin(f, target);

		if (f.isAnnotationPresent(Max.class))
			handleMax(f, target);

		if (f.isAnnotationPresent(Match.class))
			handleRegex(f, target);
	}

	private static void handleNotNull(Field f, Object target) {
		Object value = getValue(f, target);
		debug(f.getName() + "�? handleNotNull:" + value);
		if (value == null)
			throw new ArgumentException("[" + f.getName() + "]"
					+ f.getAnnotation(NotEmpty.class).message());
	}

	/**
	 * 验证Object Null 和String Empty
	 * 
	 * @param f
	 * @param target
	 */
	private static void handleNotEmpty(Field f, Object target) {
		Object value = getValue(f, target);
		if (value == null)
			throw new ArgumentException("[" + f.getName() + "]"
					+ f.getAnnotation(NotEmpty.class).message());

		debug(f.getName() + "，handleNotEmpty:" + value);
		if (value instanceof String) {
			if (((String) value).trim().length() == 0) {
				throw new ArgumentException("[" + f.getName() + "]"
						+ f.getAnnotation(NotEmpty.class).message());
			}
		}
	}

	/**
	 * 验证字符传，Collection,Array类型
	 * 
	 * @param f
	 * @param target
	 */
	private static void handleSize(Field f, Object target) {
		Object value = getValue(f, target);

		debug(f.getName() + "，handleSize:" + value);
		Size size = f.getAnnotation(Size.class);
		// Assert.notNull(size);

		if (value == null && size.min() <= 0)
			return;
		if (value == null && size.min() > 0)
			throw getNullArgException(f.getName());

		int length = 0;
		if (value instanceof String) {
			try {
				length = ((String) value).getBytes("UTF-8").length;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		if (value instanceof Collection) {
			Collection<?> coll = (Collection<?>) value;
			length = coll.size();
		}

		if (value.getClass().isArray())
			length = Array.getLength(value);

		String name = String.format("[%s]", f.getName());

		if (length < size.min()) {
			String tips = size.messageForMin();
			if (tips.contains("%"))
				tips = String.format(name + tips, size.min());
			throw new ArgumentException(tips);
		}
		if (length > size.max()) {
			String tips = size.messageForMax();
			if (tips.contains("%"))
				tips = String.format(name + tips, size.max());
			throw new ArgumentException(tips);
		}

	}

	/**
	 * 只处理数字类�?
	 * 
	 * @param f
	 * @param target
	 */
	private static void handleMax(Field f, Object target) {
		Object value = getValue(f, target);
		if (value == null)
			return;
		debug(f.getName() + "，handleMax:" + value);
		Max max = f.getAnnotation(Max.class);
		// Assert.notNull(max);

		String message = String.format("[%s]" + max.message(), f.getName());
		if (message.contains("%"))
			message = String.format(message, max.value());

		if (value instanceof Integer) {
			if ((Integer) value > max.value())
				throw new ArgumentException(message);
		}

		if (value instanceof Long) {
			if ((Long) value > max.value())
				throw new ArgumentException(message);
		}

		if (value instanceof BigDecimal) {

			BigDecimal decimal = (BigDecimal) value;
			BigDecimal maxDecimal = BigDecimal.valueOf(max.value());
			debug("handle max decimal value:" + maxDecimal);
			if (decimal.compareTo(maxDecimal) > 0)
				throw new ArgumentException(message);
		}
	}

	/**
	 * 只处理数字类�?
	 * 
	 * @param f
	 * @param target
	 */
	private static void handleMin(Field f, Object target) {
		Object value = getValue(f, target);
		if (value == null)
			throw getNullArgException(f.getName());
		debug(f.getName() + "，handleMin:" + value);
		Min min = f.getAnnotation(Min.class);

		String message = String.format("[%s]" + min.message(), f.getName());
		if (message.contains("%"))
			message = String.format(message, min.value());

		if (value instanceof Integer) {
			if ((Integer) value < min.value())
				throw new ArgumentException(message);
		}

		if (value instanceof Long) {
			if ((Long) value < min.value())
				throw new ArgumentException(message);
		}

		if (value instanceof BigDecimal) {

			BigDecimal decimal = (BigDecimal) value;
			BigDecimal maxDecimal = BigDecimal.valueOf(min.value());
			debug("handle max decimal value:" + maxDecimal);
			if (decimal.compareTo(maxDecimal) < 0)
				throw new ArgumentException(message);
		}
	}

	private static Object getValue(Field f, Object target) {
		try {
			return f.get(target);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw e;
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private static void handleRegex(Field f, Object target) {
		Object value = getValue(f, target);
		if (value == null)
			return;

		if (!(value instanceof String))
			return;

		Match match = f.getAnnotation(Match.class);
		if (match == null)
			return;
		if (match.regex() == null )
			return;

		Pattern pattern = Pattern.compile(match.regex(),
				Pattern.CASE_INSENSITIVE);
		if (!pattern.matcher((String) value).matches()) {
			throw new ArgumentException(String.format("[%s]" + match.message(),
					f.getName()));
		}

	}

	private static ArgumentException getNullArgException(String name) {
		return new ArgumentException("参数�?" + name + " 不能�? null");
	}

	static void debug(String msg) {
		// System.out.println("validation:" + msg);
	}
}
