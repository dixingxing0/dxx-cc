/**
 * LogUser.java 6:07:32 PM Feb 15, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package log;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;

import json.JsonUtils;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 
 * 
 * @author dixingxing
 * @date Feb 15, 2012
 */
@SuppressWarnings("serial")
public class LogUser implements Serializable {
	@XmlAttribute
	@JsonProperty
	String name;

	@XmlAttribute
	@JsonProperty
	String emails;

	@XmlAttribute
	@JsonProperty
	String tels;

	public static void main(String[] args) {
		LogUser u = new LogUser();
		u.name = "123";
		u.emails = "123";
		u.tels = "123";
		System.out.println(JsonUtils.toJson(u));
	}

}
