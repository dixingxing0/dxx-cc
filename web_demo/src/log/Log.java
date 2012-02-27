/**
 * Log.java 5:23:46 PM Feb 15, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package log;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;

import json.JsonUtils;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * notifyTypes 格式为"1,2"
 * 
 * @author dixingxing
 * @date Feb 16, 2012
 */
@SuppressWarnings("serial")
@JsonAutoDetect
public class Log implements Serializable {
	@XmlAttribute
	@JsonProperty
	public String method;

	@XmlAttribute
	@JsonProperty
	public int fid;

	@XmlAttribute
	@JsonProperty
	public String comment;

	@XmlAttribute
	@JsonProperty
	public long maxTime;

	@XmlAttribute
	@JsonIgnore
	public String users;

	@XmlAttribute
	@JsonProperty
	public String notifyTypes;

	@JsonProperty
	private long cost;

	@JsonProperty
	// 根据xml 中配置的user ，注入user对象
	public List<LogUser> owners = new ArrayList<LogUser>();
	// 服务器（本机）ip
	@JsonProperty
	public String ip;

	/** 1normal,2warn,3error */
	@JsonProperty
	public int type;

	@JsonProperty
	public String message;

	/**
	 * 根据xml 中配置的user ，注入user对象
	 * 
	 * @param allUsers
	 */
	public void init(List<LogUser> allUsers, String ip) {
		this.ip = ip;
		String temp = "," + users + ",";
		for (LogUser u : allUsers) {
			if (temp.indexOf("," + u.name + ",") >= 0) {
				owners.add(u);
			}
		}
	}

	public long getCost() {
		return cost;
	}

	public void setCost(long cost) {
		this.cost = cost;
		if (cost > maxTime) {
			type = 2;
		} else {
			type = 1;
		}
	}

	public void setError(Throwable e) {
		message = getStackTrace(e);
		type = 3;
	}

	private static String getStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		try {
			t.printStackTrace(pw);
			return sw.toString();
		} finally {
			pw.close();
		}
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}

}
