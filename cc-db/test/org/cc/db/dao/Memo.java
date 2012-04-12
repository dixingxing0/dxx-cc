/**
 * Job.java 3:14:14 PM Mar 30, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.db.dao;



/**
 * 
 * @author dixingxing	
 * @date Mar 30, 2012
 */
@SuppressWarnings("serial")
public class Memo  {
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
		return id + "-" + name + "-" + createTime;
	}
}
