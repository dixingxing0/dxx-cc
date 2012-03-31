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
 * ���pojo����SqlHolder
 * 
 * @see SqlHolder
 * @author dixingxing
 * @date Feb 6, 2012
 */
public class SqlBuilder {
	private final static Logger LOG = Logger.getLogger(SqlBuilder.class);

	/**
	 * ����insert
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
		LOG.debug(holder);
		return holder;

	}

	/**
	 * ����update
	 * 
	 * @param obj
	 * @param where
	 *            ������Ϊ��
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
		LOG.debug(holder);
		return holder;

	}

	/**
	 * ɾ������Ǹ�,��
	 * 
	 * @param sb
	 */
	private static void deleteLastComma(StringBuilder sb) {
		if (sb.lastIndexOf(",") == sb.length() - 1) {
			sb.deleteCharAt(sb.length() - 1);
		}
	}

	/**
	 * ��ȡ����<br/> MyBeanProcessor�ж����˲�ѯʱ����ݿ��ֶ�ת -> po���� �Ĺ���,<br />
	 * �˴�po���� -> ��ݿ��ֶ� �Ĺ����ǰ�汣��һ��
	 * 
	 * @see MyBeanProcessor#prop2column(String)
	 * @param f
	 * @return
	 */
	private static String columnName(Field f) {
		return MyBeanProcessor.prop2column(f.getName());
	}

	/**
	 * ��ȡ������ĳ�����Ե�ֵ(û��ͨ��getter������ȡ)
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
			LOG.error("��ȡ����ֵʧ�ܣ�", e);
			throw new RuntimeException("��ȡ����ֵʧ�ܣ�", e);
		}
		return o;
	}

	/**
	 * 
	 * �����Ƿ�����޸�
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
	 * �����Ƿ���Ҫ�־û�
	 * 
	 * @param obj
	 * @return
	 */
	private static boolean isTransient(Field f) {
		int m = f.getModifiers();
		// static �� final����Ϊ��
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
	 * ��ȡ����
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
	 * java����ת������ݿ�����
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
