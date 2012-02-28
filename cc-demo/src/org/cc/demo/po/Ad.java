/**
 * Ad.java 3:55:00 PM Jan 6, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.demo.po;

import java.io.Serializable;
import java.util.Date;


import org.cc.core.dao.annotation.Column;
import org.cc.core.dao.annotation.Table;
import org.cc.core.dao.annotation.Transient;
import org.cc.demo.json.DateDeserializer;
import org.cc.demo.json.DateSerializer;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonCachable;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;


/**
 * 广告信息实体类
 * 
 * @author dixingxing
 * @date Jan 6, 2012
 */
@Table(name = "AD")
@JsonCachable
public class Ad implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	// 广告标示
	private String name;
	// 广告详情
	private String detail;
	// 显示位置
	private Long position;
	// 状态
	private Long state;
	// 发布时间
	@Column(updatable = false)
	@JsonSerialize(using = DateSerializer.class)
	@JsonDeserialize(using = DateDeserializer.class)
	private Date publishTime;
	// 发布人
	@Column(updatable = false)
	private String publishMan;
	// 修改时间
	private Date updateTime;
	// 修改人
	private String updateMan;

	@JsonIgnore
	private Long providerId;

	// 这个属性 测试多表查询
	@Transient
	@JsonIgnore
	private String providerName;

	public Long getProviderId() {
		return providerId;
	}

	public void setProviderId(Long providerId) {
		this.providerId = providerId;
	}

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

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Long getPosition() {
		return position;
	}

	public void setPosition(Long position) {
		this.position = position;
	}

	public Long getState() {
		return state;
	}

	public void setState(Long state) {
		this.state = state;
	}

	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	public String getPublishMan() {
		return publishMan;
	}

	public void setPublishMan(String publishMan) {
		this.publishMan = publishMan;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateMan() {
		return updateMan;
	}

	public void setUpdateMan(String updateMan) {
		this.updateMan = updateMan;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id:").append(id).append(",").append("name:").append(name)
				.append(",").append("publishMan:").append(publishMan).append(
						",publishTime:").append(publishTime).append(
						",updateTime:").append(updateTime);
		return sb.toString();
	}
}