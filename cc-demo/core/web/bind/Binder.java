/**
 * Binder.java 5:23:54 PM Feb 7, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package web.bind;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import web.context.WebMethod;

/**
 * 
 * 
 * @author dixingxing
 * @date Feb 7, 2012
 */
public class Binder {
	private final static Logger LOG = Logger.getLogger(Binder.class);

	private final static SimpleDateFormat d1 = new SimpleDateFormat(
			"yyyy-MM-dd");
	private final static SimpleDateFormat d2 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * 
	 * 
	 * @param request
	 * @param response
	 * @param webMethod
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static Object[] bindParam(HttpServletRequest request,
			HttpServletResponse response, WebMethod webMethod)
			throws InstantiationException, IllegalAccessException {
		// �����Ĳ���
		Class<?>[] mParams = webMethod.method.getParameterTypes();
		Object[] params = new Object[mParams.length];
		if (mParams.length > 0) {
			for (int i = 0; i < mParams.length; i++) {
				Class<?> cls = mParams[i];
				if (cls.isAssignableFrom(HttpServletRequest.class)) {
					params[i] = request;
				} else if (cls.isAssignableFrom(HttpServletResponse.class)) {
					params[i] = response;
				} else if (cls.isAssignableFrom(Model.class)) {
					params[i] = new Model(request);
				} else {
					Object obj = cls.newInstance();
					bindPojo(request, obj);
					params[i] = obj;
				}
			}
		}
		return params;
	}

	/**
	 * 
	 * 
	 * @param request
	 * @param obj
	 * @throws IllegalAccessException
	 */
	private static void bindPojo(HttpServletRequest request, Object obj)
			throws IllegalAccessException {
		for (Field f : obj.getClass().getDeclaredFields()) {
			String param = request.getParameter(f.getName());
			if (param == null) {
				continue;
			}
			Object value = null;
			Class<?> clazz = f.getType();
			try {
				if (clazz.equals(Long.TYPE) || clazz.equals(Long.class)) {
					value = Long.valueOf(param);

				} else if (clazz.equals(Double.TYPE)
						|| clazz.equals(Double.class)) {
					value = Double.valueOf(param);

				} else if (clazz.equals(Integer.TYPE)
						|| clazz.equals(Integer.class)) {
					value = Integer.valueOf(param);

				} else if (clazz.equals(String.class)) {
					value = param;

				} else if (clazz.equals(Date.class)) {
					try {
						value = d1.parseObject(param);
					} catch (Exception e) {
						value = d2.parseObject(param);
					}
				} else {
					continue;
				}

				if (f.isAccessible()) {
					f.set(obj, value);
				} else {
					f.setAccessible(true);
					f.set(obj, value);
					f.setAccessible(false);
				}
			} catch (Exception e) {
				LOG.warn("������ʱ�����쳣", e);
			}
		}
	}

	public static void bind2Request(HttpServletRequest req, Object[] objects) {
		for (Object o : objects) {
			Class<?> clazz = o.getClass();
			if (clazz.isAssignableFrom(Model.class)) {
				bindModel(req, (Model) o);
			} else {
				bindObject(req, o);
			}
		}
	}

	/**
	 * 
	 * Model
	 * 
	 * @param req
	 * @param m
	 */
	private static void bindModel(HttpServletRequest req, Model m) {
		for(String key : m.keySet()) {
			Object o = m.get(key);
			// �󶨶����������
			if(key.startsWith(Model.DEFAULT_KEY)) {
				for (Field f : o.getClass().getDeclaredFields()) {
					String k = f.getName();
					Object fieldValue = null;
					if (Modifier.isStatic(f.getModifiers()) || Modifier.isFinal(f.getModifiers())) {
						continue;
					}
					try {
						if (f.isAccessible()) {
							fieldValue = f.get(o);
						} else {
							f.setAccessible(true);
							fieldValue = f.get(o);
							f.setAccessible(false);
						}
					} catch (Exception e) {
						LOG.error("�������", e);
					}

					req.setAttribute(k, fieldValue);
				}
			} 
			// ֱ�Ӱ󶨶���
			else {
				req.setAttribute(key,o);
			}
		}
	}

	/**
	 * 
	 * Model
	 * 
	 * @param req
	 * @param o
	 */
	private static void bindObject(HttpServletRequest req, Object o) {
		for (Field f : o.getClass().getDeclaredFields()) {
			String key = f.getName();
			Object fieldValue = null;
			try {
				if (f.isAccessible()) {
					fieldValue = f.get(o);
				} else {
					f.setAccessible(true);
					fieldValue = f.get(o);
					f.setAccessible(false);
				}
			} catch (Exception e) {
				LOG.error("�������", e);
			}

			req.setAttribute(key, fieldValue);
		}
	}

}
