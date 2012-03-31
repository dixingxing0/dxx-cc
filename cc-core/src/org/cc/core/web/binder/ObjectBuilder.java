/**
 * Convertor.java 5:22:22 PM Mar 31, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.core.web.binder;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

/**
 * 
 * 
 * @author dixingxing	
 * @date Mar 31, 2012
 */
public final class ObjectBuilder {
	private static final Logger LOG = Logger.getLogger(ObjectBuilder.class);
	
	private static final String[] datePattens = {"yyyy-MM-dd","yyyy-MM-dd HH:mm:ss"};
	
	/**
	 * 解析date
	 * 
	 * 
	 * @param s
	 * @return
	 */
	private static Date parseDate(String s) {
		Date d = null;
		if(StringUtils.isBlank(s)) {
			return d;
		}
		try {
			d = DateUtils.parseDate(s, datePattens);
		} catch (ParseException e) {
			LOG.error("解析date出错，格式仅支持" + StringUtils.join(datePattens,","), e);
		}
		return d;
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
	public static Object build(Class<?> cls, String v) {
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
				LOG.warn("不支持的绑定类型 " + cls.getName());
			}
		} catch (Exception e) {
			LOG.warn("类型转换错误 ：" + v + "->" + cls.getName(), e);
		}
		return value;
	}
}
