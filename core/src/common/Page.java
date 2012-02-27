package common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dao.SqlBuilder;

public class Page<T> implements Serializable {
	private static final long serialVersionUID = -1241179900114637258L;
	private int size = 20; // 每页显示记录
	private int totalPage = 0; // 总页
	private int totalResult = 0; // 总记录数
	private int currentPage = 0; // 当前
	private int currentResult = 0; // 当前记录起始索引

	private String sql; // 用于查询的sql

	private String pageSql;
	private String countSql;

	private List<T> result = new ArrayList<T>();

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("总数:").append(totalResult).append(",当前页:").append(
				getCurrentPage());
		sb.append(",总页数:").append(getTotalPage());
		return sb.toString();
	}

	public Page(String sql, int currentPage, int size) {
		this.sql = sql;
		this.size = size;
		this.currentPage = currentPage;
		this.pageSql = SqlBuilder.pageSql(sql, getCurrentResult(),
				getCurrentResult() + getSize());
		this.countSql = SqlBuilder.countSql(sql);
	}

	public List<T> getResult() {
		if (result == null) {
			return new ArrayList<T>();
		}
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

	public int getTotalPage() {
		if (totalResult % size == 0)
			totalPage = totalResult / size;
		else
			totalPage = totalResult / size + 1;
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getTotalResult() {
		return totalResult;
	}

	public void setTotalResult(int totalResult) {
		this.totalResult = totalResult;
	}

	public int getCurrentPage() {
		if (currentPage <= 0)
			currentPage = 1;
		if (currentPage > getTotalPage())
			currentPage = getTotalPage();
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		if (size == 0) {
			size = 10;
		}
		this.size = size;
	}

	public int getCurrentResult() {
		currentResult = (getCurrentPage() - 1) * getSize();
		if (currentResult < 0)
			currentResult = 0;
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