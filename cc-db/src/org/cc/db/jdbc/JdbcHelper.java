/**
 * BaseDao.java 7:23:00 PM Apr 10, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.db.jdbc;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cc.core.CcException;
import org.cc.core.common.Exceptions;
import org.cc.db.annotation.Transient;
import org.cc.db.dao.Converter;

/**
 * 封装一些jdbc方法，使代码看起来干净些
 * 
 * @author dixingxing
 * @date Apr 10, 2012
 */
public final class JdbcHelper {
	
	private JdbcHelper() {}

	public static PreparedStatement getPstmt(Connection cn, String sql) {
		try {
			return cn.prepareStatement(sql);
		} catch (SQLException e) {
			Exceptions.uncheck(e);
			return null;
		}
	}

	public static void setParams(PreparedStatement pstmt, Object... params) {
		if (params == null) {
			return;
		}
		try {
			for (int i = 0; i < params.length; i++) {
				pstmt.setObject(i + 1, params[i]);
			}
		} catch (SQLException e) {
			Exceptions.uncheck(e);
		}
	}

	public static ResultSet executeQuery(PreparedStatement pstmt) {
		try {
			return pstmt.executeQuery();
		} catch (Exception e) {
			Exceptions.uncheck(e);
			return null;
		}
	}

	public static void executeUpdate(PreparedStatement pstmt) {
		try {
			pstmt.executeUpdate();
		} catch (Exception e) {
			Exceptions.uncheck(e);
		}
	}

	public static void close(ResultSet rs, PreparedStatement pstmt) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
		} catch (SQLException e) {
			Exceptions.uncheck(e);
		}
	}

	public static boolean rsNext(ResultSet rs) {
		try {
			return rs.next();
		} catch (SQLException e) {
			Exceptions.uncheck(e);
			return false;
		}
	}

	public static List<Map<String, Object>> getMap(ResultSet rs, Field[] fields) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		while (rsNext(rs)) {
			Map<String, Object> map = new HashMap<String, Object>();
			StringBuilder sb = new StringBuilder("data : ");
			for (Field f : fields) {
				if(f.isAnnotationPresent(Transient.class)) {
					continue;
				}
				map.put(f.getName(), rsGet(rs, f.getType(), Converter.j2db(f.getName())));
				sb.append(f.getName()).append(":").append(map.get(f.getName()))
						.append(",");
			}
			// LOG.debug(sb.toString());
			list.add(map);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getNumber(ResultSet rs, Class<?> numberClazz) {
		T number = null;
		while (rsNext(rs)) {
			number = (T) rsGet(rs, numberClazz, 1);
			if (rsNext(rs)) {
				throw new CcException("期望返回单条数据，实际返回多条记录");
			}
		}
		return number;
	}

	public static Object rsGet(ResultSet rs, Class<?> propType, String name) {
		try {
			if (propType.equals(String.class)) {
				return rs.getString(name);
			}
			if (propType.equals(Integer.TYPE) || propType.equals(Integer.class)) {
				return Integer.valueOf(rs.getInt(name));

			}
			if (propType.equals(Boolean.TYPE) || propType.equals(Boolean.class)) {
				return Boolean.valueOf(rs.getBoolean(name));

			}
			if (propType.equals(Long.TYPE) || propType.equals(Long.class)) {
				return Long.valueOf(rs.getLong(name));

			}
			if (propType.equals(Double.TYPE) || propType.equals(Double.class)) {
				return Double.valueOf(rs.getDouble(name));

			}
			if (propType.equals(Float.TYPE) || propType.equals(Float.class)) {
				return Float.valueOf(rs.getFloat(name));

			}
			if (propType.equals(Short.TYPE) || propType.equals(Short.class)) {
				return Short.valueOf(rs.getShort(name));

			}
			if (propType.equals(Byte.TYPE) || propType.equals(Byte.class)) {
				return Byte.valueOf(rs.getByte(name));

			}
			if (propType.equals(Timestamp.class)) {
				return rs.getTimestamp(name);
			}
			if (propType.equals(Date.class)) {
				return new Date(rs.getTimestamp(name).getTime());
			}
			return rs.getObject(name);
		} catch (SQLException e) {
			Exceptions.uncheck(e);
			return null;
		}

	}

	public static Object rsGet(ResultSet rs, Class<?> propType, int index) {
		try {
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
			if (propType.equals(Date.class)) {
				return new Date(rs.getTimestamp(index).getTime());
			}
			return rs.getObject(index);
		} catch (SQLException e) {
			Exceptions.uncheck(e);
			return null;
		}

	}
}
