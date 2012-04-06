/**
 * Binder.java 5:23:54 PM Feb 7, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.core.web.binding;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.cc.core.common.CcException;
import org.cc.core.common.ReflectUtils;
import org.cc.core.conf.WebConfig;
import org.cc.core.web.Model;
import org.cc.core.web.WebMethod;



/**
 * 数据绑定类 <br/>
 * 目前PathVar可以绑定的类型有 ：java.lang.Long<br/>java.lang.Double<br/>java.lang.Integer<br/>java.lang.String<br/>java.util.Date
 * 
 * @author dixingxing
 * @date Feb 7, 2012
 */
public class BinderImpl implements Binder{
	private static final Logger LOG = Logger.getLogger(BinderImpl.class);
	
	private PathVarBinder pathVarBinder = WebConfig.getPathVarBinder();
	
	private ObjectBuilder objectBuilder = WebConfig.getObjectBuilder();
	
	
	public Object[] fromRequest(HttpServletRequest request,HttpServletResponse response, WebMethod webMethod){
		LOG.debug("绑定controller中方法参数");
		// 方法的参数
		Class<?>[] paramClasses = webMethod.getMethod().getParameterTypes();

		Annotation[][] annotations = webMethod.getMethod().getParameterAnnotations();

		Object[] paramValues = new Object[paramClasses.length];
		
		// 如果方法没有定义任何参数，则直接返回
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
			else if (annotations[i] != null && pathVarBinder.isPathVar(annotations[i])) {
				// 从uri中解析出来的path variable
				paramValues[i] = pathVarBinder.getValue(request.getServletPath(), webMethod, cls,annotations[i]);
			}
			// pojo
			else {
				try {
					paramValues[i] = bind(request, cls.newInstance());
				} catch (Exception e) {
					throw new CcException(e);
				} 
			}
		}
		return paramValues;
	}

	public void toRequest(HttpServletRequest req, Object[] objects) {
		LOG.debug("将对象绑定到request中,供视图使用");
		for (Object o : objects) {
			if(o == null ) {
				continue;
			}
			Class<?> clazz = o.getClass();
			if (clazz.isAssignableFrom(Model.class)) {
				bindModel2Request(req, (Model) o);
			} else {
				bindObject2Request(req, o);
			}
		}
	}

	/**
	 * 
	 * 把model中的对象放到request中，<br/>如果是调用model.addAttribute(Object)方法放到model中的，那么把此object的所有属性值放到request中
	 * 
	 * @see Model#addAttribute(Object)
	 * @param req
	 * @param m
	 */
	protected void bindModel2Request(HttpServletRequest req, Model m) {
		for (String key : m.keySet()) {
			Object o = m.get(key);
			// 绑定对象里的属性
			if (m.isDefaultKey(key)) {
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
	protected void bindObject2Request(HttpServletRequest req, Object o) {
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
	 * 通过反射把request中的参数赋给obj对应的属性
	 * 
	 * @param request
	 * @param obj
	 * @throws IllegalAccessException
	 */
	protected Object bind(HttpServletRequest request, Object obj) {
		for (Field f : ReflectUtils.getVariableFields(obj.getClass())) {

			String param = request.getParameter(f.getName());

			if (param == null) {
				continue;
			}
			Class<?> clazz = f.getType();
			Object value = objectBuilder.build(clazz, param);
			if (value == null) {
				continue;
			}

			ReflectUtils.set(obj, f, value);
		}
		return obj;
	}
}