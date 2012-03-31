/**
 * Job.java 3:14:14 PM Mar 30, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.demo.domain;

import org.cc.core.dao.Dao;
import org.cc.core.dao.annotation.Table;
import org.cc.demo.json.JsonUtils;

/**
 * 
 * @author dixingxing	
 * @date Mar 30, 2012
 */
@Table("memo")
public class Memo extends Dao<Memo>{
	/** 设置一个单例 ，便于泛型查询 Memo.DB.queryList(...)  */
	public final static Memo DB = new Memo();
	
	private Long id;
	
	private String name;
	
	private String createTime;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}
