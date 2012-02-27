/**
 * Binder.java 5:23:54 PM Feb 7, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.core.web;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.cc.core.common.ReflectUtils;
import org.cc.core.web.annotation.PathVar;



/**
 * 数据绑定类
 * 
 * @author dixingxing
 * @date Feb 7, 2012
 */
public class Binder {
	private final static Logger logger = Logger.getLogger(Binder.class);

	/**
	 * 
	 * controller中方法参数的绑定
	 * 
	 * @param request
	 * @param response
	 * @param webMethod
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static Object[] bind(HttpServletRequest request,
			HttpServletResponse response, WebMethod webMethod)
			throws InstantiationException, IllegalAccessException {
		logger.debug("绑定controller中方法参数");
		// 方法的参数
		Class<?>[] paramClasses = webMethod.method.getParameterTypes();

		Annotation[][] annotations = webMethod.method.getParameterAnnotations();

		Object[] paramValues = new Object[paramClasses.length];

		if (paramClasses.length == 0) {
			return paramValues;
		}

		for (int i = 0; i < paramClasses.length; i++) {
			Class<?> cls = paramClasses[i];
			// request
			if (cls.isAssignableFrom(HttpServletRequest.class)) {
				paramValues[i] = request;
			}
			// response
			else if (cls.isAssignableFrom(HttpServletResponse.class)) {
				paramValues[i] = response;
			}
			// model
			else if (cls.isAssignableFrom(Model.class)) {
				paramValues[i] = new Model();
			}
			// @PathVariable
			else if (annotations[i] != null && isPathVar(annotations[i])) {
				// 从uri中解析出来的path variable
				String[] variables = webMethod.getPathVars(request
						.getServletPath());
				PathVar pv = (PathVar) getPathVarAnnotation(annotations[i]);
				if (pv.value() >= variables.length) {
					logger.warn("PathVar.value()值为 :" + pv.value()
							+ ",应该小于匹配到的参数个数 : " + variables.length);
					break;
				}
				String v = variables[pv.value()];
				Object value = convertValue(cls, v);
				if (value != null) {
					logger.debug("设置pathVariable成功，值为:" + value);
					paramValues[i] = value;
				} else {
					paramValues[i] = cls.newInstance();
				}
			}
			// pojo
			else {
				paramValues[i] = bind(request, cls.newInstance());
			}
		}
		return paramValues;
	}

	/**
	 * 将对象绑定到request中,供视图(jsp)使用
	 * 
	 * @param req
	 * @param objects
	 */
	public static void bind2Request(HttpServletRequest req, Object[] objects) {
		logger.debug("将对象绑定到request中,供视图(jsp)使用");
		for (Object o : objects) {
			Class<?> clazz = o.getClass();
			if (clazz.isAssignableFrom(Model.class)) {
				bindModel2Request(req, (Model) o);
			} else {
				bindObject2Request(req, o);
			}
		}
	}

	/**
	 * 通过反射把request中的参数赋给obj对应的属性
	 * 
	 * @param request
	 * @param obj
	 * @throws IllegalAccessException
	 */
	private static Object bind(HttpServletRequest request, Object obj) {
		for (Field f : ReflectUtils.getVariableFields(obj.getClass())) {

			String param = request.getParameter(f.getName());

			if (param == null) {
				continue;
			}
			Class<?> clazz = f.getType();
			Object value = convertValue(clazz, param);
			if (value == null) {
				continue;
			}

			ReflectUtils.set(obj, f, value);
		}
		return obj;
	}

	/**
	 * 
	 * 把model中的对象放到request中，<br/>如果是调用model.addAttribute(Object)方法放到model中的，那么把此object的所有属性值放到request中
	 * 
	 * @see Model#addAttribute(Object)
	 * @param req
	 * @param m
	 */
	private static void bindModel2Request(HttpServletRequest req, Model m) {
		for (String key : m.keySet()) {
			Object o = m.get(key);
			// 绑定对象里的属性
			if (key.startsWith(Model.DEFAULT_KEY)) {
				for (Field f : ReflectUtils.getVariableFields(o.getClass())) {
					req.setAttribute(f.getName(), ReflectUtils.get(o, f));
				}
			}
			// 直接绑定对象
			else {
				req.setAttribute(key, o);
			}
		}
	}

	/**
	 * controller中方法参数的绑定<br/> 通过反射把obj对应的属性放到request中
	 * 
	 * @param req
	 * @param o
	 */
	private static void bindObject2Request(HttpServletRequest req, Object o) {
		// request 和 response参数不需要处理
		if (o.getClass().isAssignableFrom(HttpServletRequest.class)
				|| o.getClass().isAssignableFrom(HttpServletResponse.class)) {
			return;
		}
		for (Field f : ReflectUtils.getVariableFields(o.getClass())) {
			req.setAttribute(f.getName(), ReflectUtils.get(o, f));
		}
	}

	/**
	 * 解析date
	 * 
	 * 
	 * @param s
	 * @return
	 */
	private static Date parseDate(String s) {
		Date d = null;
		try {
			d = new SimpleDateFormat("yyyy-MM-dd").parse(s);
		} catch (ParseException e) {
		}

		if (d != null) {
			return d;
		}

		try {
			d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(s);
		} catch (ParseException e1) {
			throw new RuntimeException("解析date出错", e1);
		}
		return d;
	}

	/**
	 * 获取PathVar注解
	 * 
	 * @param annotations
	 * @return
	 */
	private static Annotation getPathVarAnnotation(Annotation[] annotations) {
		for (Annotation a : annotations) {
			if (PathVar.class.equals(a.annotationType())) {
				return a;
			}
		}
		return null;
	}

	/**
	 * 是加了PathVar注解的参数
	 * 
	 * @param annotations
	 * @return
	 */
	private static boolean isPathVar(Annotation[] annotations) {
		for (Annotation a : annotations) {
			if (PathVar.class.equals(a.annotationType())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * 把String 转换成相应类型
	 * 
	 * @param cls
	 * @param value
	 * @param v
	 * @return
	 */
	private static Object convertValue(Class<?> cls, String v) {
		Object value = null;
		try {
			if (cls.equals(Long.TYPE) || cls.equals(Long.class)) {
				value = Long.valueOf(v);

			} else if (cls.equals(Double.TYPE) || cls.equals(Double.class)) {
				value = Double.valueOf(v);

			} else if (cls.equals(Integer.TYPE) || cls.equals(Integer.class)) {
				value = Integer.valueOf(v);

			} else if (cls.equals(String.class)) {
				value = v;

			} else if (cls.equals(Date.class)) {
				value = parseDate(v);
			} else {
				logger.error("不能绑定到PathVariable 参数");
			}
		} catch (Exception e) {
			logger.error("类型转换错误", e);
			throw new WebException("类型转换错误", e);
		}
		return value;
	}

}
