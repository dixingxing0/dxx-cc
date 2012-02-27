/**
 * JsonUtil.java 11:15:23 AM Feb 15, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.demo.json;

import java.io.StringWriter;
import java.text.SimpleDateFormat;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

/**
 * json 工具�?
 * 
 * @author dixingxing
 * @date Feb 15, 2012
 */
public class JsonUtils {
	public final static String dateFormat = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 使用jackson 序列化成json字符�?
	 * 
	 * @param o
	 * @return
	 */
	public static String toJson(Object o) {
		ObjectMapper mapper = sMapper();
		StringWriter sw = new StringWriter();
		try {
			mapper.writeValue(sw, o);
			return sw.toString();
		} catch (Exception e) {
			throw new RuntimeException("序列化对象出�?", e);
		}
	}

	/**
	 * 使用jackson 把json字符串反序列化成java对象
	 * 
	 * @param <T>
	 * @param s
	 * @param clazz
	 * @return
	 */
	public static <T> T toObject(String s, Class<T> clazz) {
		ObjectMapper mapper = dMapper();
		try {
			return mapper.readValue(s, clazz);
		} catch (Exception e) {
			throw new RuntimeException("反序列化对象出错", e);
		}
	}

	private static ObjectMapper sMapper() {
		ObjectMapper mapper = new ObjectMapper();
		SerializationConfig cfg = mapper.getSerializationConfig()
				.withDateFormat(new SimpleDateFormat(dateFormat));
		mapper.setSerializationConfig(cfg);
		return mapper;
	}

	private static ObjectMapper dMapper() {
		ObjectMapper mapper = new ObjectMapper();
		DeserializationConfig cfg = mapper.getDeserializationConfig()
				.withDateFormat(new SimpleDateFormat(dateFormat));
		mapper.setDeserializationConfig(cfg);
		return mapper;
	}

}
