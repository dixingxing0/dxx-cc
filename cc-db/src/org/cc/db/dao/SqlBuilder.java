/**
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.db.dao;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.log4j.Logger;
import org.cc.core.common.ReflectUtils;
import org.cc.core.common.Strings;
import org.cc.db.annotation.Column;
import org.cc.db.annotation.Table;
import org.cc.db.annotation.Transient;



/**
 * 根据pojo构造SqlHolder <br />
 * 生成oracle的sql
 * 
 * @see SqlHolder
 * @author dixingxing
 * @date Feb 6, 2012
 */
public final class SqlBuilder {
	public static final Logger LOG = Logger.getLogger(SqlBuilder.class);
	public static final String ID = "id";

	private SqlBuilder(){}
	
	/**
	 * 构造insert
	 * 
	 * @param po
	 * @return
	 */
	public static SqlHolder buildInsert(Object po) {
		SqlHolder holder = new SqlHolder();
		Field[] fields = ReflectUtils.getVariableFields(po.getClass());

		StringBuilder columns = new StringBuilder();
		StringBuilder values = new StringBuilder();

		for (Field f : fields) {
			if (isTransient(f)) {
				continue;
			}
			holder.addParam(convert(ReflectUtils.get(po, f)));
			columns.append(columnName(f)).append(",");
			values.append("?").append(",");

		}
		deleteLastComma(columns);
		deleteLastComma(values);

		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ").append(tableName(po.getClass())).append(" (");
		sql.append(columns).append(") ");
		sql.append(" VALUES(").append(values).append(") ");
		holder.setSql(sql.toString());
		return holder;

	}

	/**
	 * 更新条件 where id= po.getId();
	 * 
	 * 
	 * @param po
	 * @return
	 */
	public static SqlHolder buildUpdate(Object po) {
		return buildUpdate(po, "id=" + ReflectUtils.getValueByFieldName(po, ID));
	}

	/**
	 * 
	 * <p>oracle 查询序列</p>
	 *
	 * @param po
	 * @return
	 */
	public static String buildGetSeq(Object po) {
		if(po.getClass().isAnnotationPresent(Table.class)) {
			Table t = po.getClass().getAnnotation(Table.class);
			if(Strings.isNotBlank(t.seq())) {
				return String.format("select %s.nextval from dual",t.seq());
			}
		}
		return null;
	}
	
	/**
	 * sqlite3 insert 后返回生成的主键（id）
	 * 
	 * @param poClass
	 * @return
	 */
    public static String buildGetInsertId(Class<?> poClass) {
        return "select max(id) from " + tableName(poClass);
    }
    
    
    
    /**
     *  生成按id查询的sql
     * 
     * @param poClass
     * @param id
     * @return
     */
    public static SqlHolder buildQueryById(Class<?> poClass,Long id) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("select * from ").append(tableName(poClass));
    	sb.append(" where id = ?");
    	return new SqlHolder(sb.toString(),id);
    }
    
    /**
     * 生成根据id删除的sql
     * 
     * @param po
     * @param id
     * @return
     */
    public static SqlHolder buildDelete(Class<?> po,Long id) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("delete from ").append(tableName(po));
    	sb.append(" where id = ?");
    	return new SqlHolder(sb.toString(),id);
    }
	
	/**
	 * 把sql封装成分页的sql
	 * 
	 * @param sql
	 *            完整的查询语句
	 * @param start
	 * @param end
	 * @return
	 */
	public static String pageSql(String sql, int start, int end) {
		StringBuilder pageSql = new StringBuilder();
		pageSql.append(sql).append(" limit ").append(start).append(",").append(end);
		return pageSql.toString();
	}

	/**
	 * 
	 * 把sql转换成查询总数的sql
	 * 
	 * @param sql
	 *            完整的查询语句
	 * @return
	 */
	public static String countSql(String sql) {
		if (Strings.isBlank(sql)) {
			return sql;
		}
		return sql.replaceFirst("select .* from", "select count(1) from");
	}

	/**
	 * 构造update
	 * 
	 * @param po
	 * @param where
	 *            不允许为空
	 * @return
	 */
	private static SqlHolder buildUpdate(Object po, String where) {
		SqlHolder holder = new SqlHolder();
		Field[] fields = ReflectUtils.getVariableFields(po.getClass());

		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ").append(tableName(po.getClass())).append(" SET ");

		for (Field f : fields) {
			if (isTransient(f) || !isUpdatable(f)) {
				continue;
			}
			holder.addParam(convert(ReflectUtils.get(po, f)));
			sql.append(columnName(f)).append("=?").append(",");

		}
		deleteLastComma(sql);
		sql.append(" WHERE ").append(where);
		holder.setSql(sql.toString());
		LOG.debug(holder);
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
	 * 获取列名<br/>
	 * MyBeanProcessor中定义了查询时从数据库字段转 -> po属性 的规则,<br />
	 * 此处po属性 -> 数据库字段 的规则和前面保持一致
	 * 
	 * @see #j2db(String)
	 * @param f
	 * @return
	 */
	private static String columnName(Field f) {
		return Converter.j2db(f.getName());
	}
	

	/**
	 * 
	 * 属性是否可以修改
	 * 
	 * @param obj
	 * @return
	 */
	private static boolean isUpdatable(Field f) {
		// id 不能修改
		if (ID.equals(f.getName())) {
			return false;
		}
		Column c = f.getAnnotation(Column.class);
		return c == null ? true : c.updatable();
	}

	/**
	 * 
	 * 属性是否不需要持久化
	 * 
	 * @param obj
	 * @return
	 */
	private static boolean isTransient(Field f) {
		Transient t = f.getAnnotation(Transient.class);
		if (t != null && t.value()) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * 获取表名
	 * 
	 * @param poClass
	 * @return
	 */
	private static String tableName(Class<?> poClass) {
		String tableName = poClass.getSimpleName();
		Table table = poClass.getAnnotation(Table.class);
		if (table != null && Strings.isNotBlank(table.value())) {
			tableName = table.value();
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
			return new Timestamp(date.getTime());
		}
		return o;
	}
}