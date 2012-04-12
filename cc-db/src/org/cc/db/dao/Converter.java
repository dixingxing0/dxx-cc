/**
 * Convertor.java 10:55:00 AM Apr 11, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.db.dao;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cc.core.common.Strings;

/**
 * 
 * 
 * @author dixingxing
 * @date Apr 11, 2012
 */
public class Converter {
	private static final Pattern P = Pattern.compile("([A-Z])");

	/**
	 * 数据库列名 -> java属性名
	 * 
	 * @param column
	 * @return
	 */
	public static String db2j(String column) {
		String[] strs = column.split("_");
		StringBuilder conventName = new StringBuilder();
		for (int i = 0; i < strs.length; i++) {
			conventName.append(Strings.capitalize(strs[i]));
		}
		return Strings.uncapitalize(conventName.toString());
	}

	/**
	 * java属性 -> 数据库列名
	 * 
	 * @param prop
	 *            命名规则为驼峰命名法，不支持连续两个大写字母
	 * @return
	 */
	public static String j2db(String prop) {
		String result = prop;
		Matcher m = P.matcher(prop);
		while (m.find()) {
			String s = m.group(1);
			result = result.replaceFirst(s, "_" + s.toLowerCase());
		}

		return result;
	}
}
