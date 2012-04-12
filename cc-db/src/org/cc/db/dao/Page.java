package org.cc.db.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 分页工具类
 * 
 * @author dixingxing	
 * @date Mar 30, 2012
 */
public class Page<T> implements Serializable {
	private static final long serialVersionUID = -1241179900114637258L;
	
	public static final int DEFAULT_SIZE = 10;
	
	private int size; // 每页显示记录
	private int totalResult; // 总记录数
	private int currentPage; // 当前
	private int currentResult = 1; // 当前记录起始索引

	private String sql; // 用于查询的sql

	private String pageSql;
	private String countSql;

	private List<T> result = new ArrayList<T>();

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("总数:").append(totalResult).append(",当前:").append(
				getCurrentPage());
		sb.append(",总页:").append(getTotalPage());
		return sb.toString();
	}

	/**
	 * 
	 * 
	 * @param sql 要执行的sql
	 * @param currentPage 当前页数
	 * @param size 每页条数
	 */
	public Page(String sql, Integer currentPage, Integer size) {
		this.sql = sql;
		this.currentPage = (currentPage == null || currentPage < 1) ? 1 : currentPage;
		this.size = (size == null || size < 0 )? DEFAULT_SIZE : size;
		this.currentResult = (this.currentPage - 1) * this.size;
		this.pageSql = SqlBuilder.pageSql(sql, this.currentResult, this.currentResult + size);
		this.countSql = SqlBuilder.countSql(sql);
	}

	/**
	 * 取得查询返回的数据
	 * 
	 * @return
	 */
	public List<T> getResult() {
		if (result == null) {
			return new ArrayList<T>();
		}
		return result;
	}

	/**
	 * 设置查询返回的数据
	 * 
	 * @param result
	 */
	public void setResult(List<T> result) {
		this.result = result;
	}

	
	/**
	 * 总页数
	 * 
	 * @return
	 */
	public int getTotalPage() {
		if (totalResult % size == 0) {
			return totalResult / size;
		}
		return totalResult / size + 1;
	}

	/**
	 * 总条数
	 * 
	 * @return
	 */
	public int getTotalResult() {
		return totalResult;
	}

	/**
	 * 设置总条数
	 * 
	 * @param totalResult
	 */
	public void setTotalResult(int totalResult) {
		this.totalResult = totalResult;
	}

	/**
	 * 当前是第几页 (最小为1)
	 * 
	 * @return
	 */
	public int getCurrentPage() {
		if (currentPage <= 0) {
			return 1;
		}
		if (currentPage > getTotalPage()) {
			return getTotalPage();
		}
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * 当前是第几条
	 * 
	 * @return
	 */
	public int getCurrentResult() {
		return currentResult;
	}

	public void setCurrentResult(int currentResult) {
		this.currentResult = currentResult;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	/**
	 * 分页用的sql
	 * 
	 * @return
	 */
	public String getPageSql() {
		return pageSql;
	}

	/**
	 * 查询总数用的sql
	 * 
	 * @return
	 */
	public String getCountSql() {
		return countSql;
	}

}