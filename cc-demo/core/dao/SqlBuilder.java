/**
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package dao;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import dao.annotation.Column;
import dao.annotation.Table;
import dao.annotation.Transient;

import po.Ad;

/**
 * 根据pojo构造SqlHolder
 * 
 * @see SqlHolder
 * @author dixingxing
 * @date Feb 6, 2012
 */
public class SqlBuilder {
	private final static Logger logger = Logger.getLogger(SqlBuilder.class);

	/**
	 * 构造insert
	 * 
	 * @param po
	 * @return
	 */
	public static SqlHolder buildInsert(Object po) {
		SqlHolder holder = new SqlHolder();
		Field[] fields = po.getClass().getDeclaredFields();

		StringBuilder columns = new StringBuilder();
		StringBuilder values = new StringBuilder();

		for (Field f : fields) {
			if (isTransient(f)) {
				continue;
			}
			holder.addParam(convert(getValue(po, f)));
			columns.append(columnName(f)).append(",");
			values.append("?").append(",");

		}
		deleteLastComma(columns);
		deleteLastComma(values);

		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ").append(tableName(po)).append(" (");
		sql.append(columns).append(") ");
		sql.append(" VALUES(").append(values).append(") ");
		holder.setSql(sql.toString());
		logger.debug(holder);
		return holder;

	}

	/**
	 * 构造update
	 * 
	 * @param obj
	 * @param where
	 *            不允许为空
	 * @return
	 */
	public static SqlHolder buildUpdate(Object obj, String where) {
		SqlHolder holder = new SqlHolder();
		Field[] fields = obj.getClass().getDeclaredFields();

		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ").append(tableName(obj)).append(" SET ");

		for (Field f : fields) {
			if (isTransient(f) || !isUpdatable(f)) {
				continue;
			}
			holder.addParam(convert(getValue(obj, f)));
			sql.append(columnName(f)).append("=?").append(",");

		}
		deleteLastComma(sql);
		sql.append(" WHERE ");
		sql.append(StringUtils.isNotBlank(where) ? where : "1=2");
		holder.setSql(sql.toString());
		logger.debug(holder);
		return holder;

	}

	/**
	 * 删除最后那个“,”
	 * 
	 * @param sb
	 */
	private static void deleteLastComma(StringBuilder sb) {
		if (sb.lastIndexOf(",") == sb.length() - 1) {
			sb.deleteCharAt(sb.length() - 1);
		}
	}

	/**
	 * 获取列名<br/> MyBeanProcessor中定义了查询时从数据库字段转 -> po属性 的规则,<br />
	 * 此处po属性 -> 数据库字段 的规则和前面保持一致
	 * 
	 * @see MyBeanProcessor#prop2column(String)
	 * @param f
	 * @return
	 */
	private static String columnName(Field f) {
		return MyBeanProcessor.prop2column(f.getName());
	}

	/**
	 * 获取对象中某个属性的值(没有通过getter方法获取)
	 * 
	 * @param obj
	 * @param f
	 * @return
	 */
	private static Object getValue(Object obj, Field f) {
		Object o = null;
		try {
			if (f.isAccessible()) {
				o = f.get(obj);
			} else {
				f.setAccessible(true);
				o = f.get(obj);
				f.setAccessible(false);
			}
		} catch (Exception e) {
			logger.error("获取属性值失败！", e);
			throw new RuntimeException("获取属性值失败！", e);
		}
		return o;
	}

	/**
	 * 
	 * 属性是否可以修改
	 * 
	 * @param obj
	 * @return
	 */
	private static boolean isUpdatable(Field f) {
		Column c = f.getAnnotation(Column.class);
		if (c != null) {
			return c.updatable();
		}
		return true;
	}

	/**
	 * 
	 * 属性是否不需要持久化
	 * 
	 * @param obj
	 * @return
	 */
	private static boolean isTransient(Field f) {
		int m = f.getModifiers();
		// static 或 final的视为常量
		if (Modifier.isStatic(m) || Modifier.isFinal(m)) {
			return true;
		}
		Transient t = f.getAnnotation(Transient.class);
		if (t != null && t.value() == true) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * 获取表名
	 * 
	 * @param obj
	 * @return
	 */
	private static String tableName(Object obj) {
		String tableName = obj.getClass().getSimpleName();
		Table table = obj.getClass().getAnnotation(Table.class);
		if (table != null && StringUtils.isNotEmpty(table.name())) {
			tableName = table.name();
		}
		return tableName;
	}

	/**
	 * 
	 * java类型转换成数据库类型
	 * 
	 * @param o
	 * @return
	 */
	private static Object convert(Object o) {
		if (o == null) {
			return null;
		}
		if (o instanceof Date) {
			Date date = (Date) o;
			Timestamp t = new Timestamp(date.getTime());
			return t;
		}
		return o;
	}

	public static void main(String[] args) {
		Ad ad = new Ad();
		ad.setId(101L);
		ad.setName("123");
		ad.setPosition(1L);
		ad.setState(1L);
		ad.setPublishTime(new Date());
		ad.setPublishMan("publishtest");
		ad.setUpdateTime(new Date());
		ad.setUpdateMan("updatetest");
		ad.setDetail("detail");
		ad.setProviderId(100001790735L);
		buildUpdate(ad, null);
	}

}
