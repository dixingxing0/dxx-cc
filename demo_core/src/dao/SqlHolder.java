/**
 * Temp.java 3:31:49 PM Feb 6, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 封装sql 和 执行sql需要的参数
 * 
 * @author dixingxing
 * @date Feb 6, 2012
 */
public class SqlHolder {
	private String sql;
	private List<Object> params = new ArrayList<Object>();

	public SqlHolder(String sql, Object... params) {
		this.sql = sql;
		this.params.addAll(Arrays.asList(params));
	}

	public SqlHolder() {
	}

	public void addParam(Object o) {
		params.add(o);
	}

	public Object[] getParams() {
		return params.toArray();
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("");
		sb.append(sql);
		if (params.size() == 0) {
			return sb.toString();
		}
		sb.append("  parameters : ");
		for (Object obj : params) {
			sb.append(obj).append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
}
