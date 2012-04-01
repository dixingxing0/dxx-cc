/**
 * MyBeanProcessor.java 6:10:37 PM Jan 31, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.core.dao;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.lang.StringUtils;

/**
 * 数据库字段转换成bean属性的特殊处理
 * 
 * @author dixingxing
 * @date Jan 31, 2012
 */
public final class DbUtilsBeanProcessor extends BeanProcessor {
	private final static Pattern P = Pattern.compile("([A-Z])");
	
	/**
	 * 数据库列名 -> java属性名
	 * 
	 * @param column
	 * @return
	 */
	private static String db2j(String column) {
		String[] strs = column.split("_");
		StringBuilder conventName = new StringBuilder();
		for (int i = 0; i < strs.length; i++) {
			conventName.append(StringUtils.capitalize(strs[i]));
		}
		return StringUtils.uncapitalize(conventName.toString());
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

	@Override
	protected int[] mapColumnsToProperties(ResultSetMetaData rsmd,
			PropertyDescriptor[] props) throws SQLException {

		int cols = rsmd.getColumnCount();
		int[] columnToProperty = new int[cols + 1];
		Arrays.fill(columnToProperty, PROPERTY_NOT_FOUND);

		for (int col = 1; col <= cols; col++) {
			String columnName = rsmd.getColumnLabel(col);
			if (null == columnName || 0 == columnName.length()) {
				columnName = rsmd.getColumnName(col);
			}
			columnName = db2j(columnName);
			for (int i = 0; i < props.length; i++) {

				if (columnName.equalsIgnoreCase(props[i].getName())) {
					columnToProperty[col] = i;
					break;
				}
			}
		}

		return columnToProperty;
	}

	@Override
	protected Object processColumn(ResultSet rs, int index, Class<?> propType)
			throws SQLException {
		if (!propType.isPrimitive() && rs.getObject(index) == null) {
			return null;
		}

		if (propType.equals(String.class)) {
			return rs.getString(index);

		} 
		if (propType.equals(Integer.TYPE) || propType.equals(Integer.class)) {
			return Integer.valueOf(rs.getInt(index));

		} 
		if (propType.equals(Boolean.TYPE) || propType.equals(Boolean.class)) {
			return Boolean.valueOf(rs.getBoolean(index));

		} 
		if (propType.equals(Long.TYPE) || propType.equals(Long.class)) {
			return Long.valueOf(rs.getLong(index));

		} 
		if (propType.equals(Double.TYPE) || propType.equals(Double.class)) {
			return Double.valueOf(rs.getDouble(index));

		} 
		if (propType.equals(Float.TYPE) || propType.equals(Float.class)) {
			return Float.valueOf(rs.getFloat(index));

		} 
		if (propType.equals(Short.TYPE) || propType.equals(Short.class)) {
			return Short.valueOf(rs.getShort(index));

		}
		if (propType.equals(Byte.TYPE) || propType.equals(Byte.class)) {
			return Byte.valueOf(rs.getByte(index));

		} 
		if (propType.equals(Timestamp.class)) {
			return rs.getTimestamp(index);
		}
		// 增加date类型
		if (propType.equals(Date.class)) {
			return new Date(rs.getTimestamp(index).getTime());
		} 
		return rs.getObject(index);
	}

}