/**
 * HttpUtils.java 9:25:12 AM Mar 31, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.demo.common.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;
import org.cc.core.common.CcException;
import org.cc.demo.common.constant.Constant;
import org.cc.demo.json.JsonUtils;
import org.codehaus.jackson.type.TypeReference;

/**
 * json协议的rpc访问
 * 
 * @author dixingxing	
 * @date Mar 31, 2012
 */
public final class HttpUtils {
	private static final Logger LOG = Logger.getLogger(HttpUtils.class);

	private HttpUtils(){}
	
	/**
	 * http get 方式访问网页，以文本方式返回
	 * 
	 * @param link
	 * @return 
	 */
	public static String getResponseAsString(String link){
		long start = System.currentTimeMillis();
		String result = "";
		try {
			URL url = new URL(link);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置连接超时时间为2秒
			conn.setConnectTimeout(Constant.TIME_SECONDE_2);
			conn.connect();

			InputStream urlStream = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(urlStream));
			
			String s = "";
			while ((s = reader.readLine()) != null) {
				result += s;
			}
			LOG.debug(System.currentTimeMillis() - start);
			return result;
		} catch(Exception e){
			throw new CcException(e);
		}
	}
	
	/**
	 * 访问远程，获取json并解析成对象返回 (单个对象)
	 * 
	 * @param <T>
	 * @param link
	 * @param clazz
	 * @return
	 */
	public static <T> T getObject(String link,Class<T> clazz) {
		String s  = getResponseAsString(link);
		return JsonUtils.toObject(s, clazz);
	}
	
	/**
	 * 访问远程，获取json并解析成对象返回 (list/map)
	 * 
	 * @param <T>
	 * @param link
	 * @param typeReference new TypeReference<List<MyBean>>() {}
	 * @return
	 */
	public static <T> T getObject(String link,TypeReference<T> typeReference) {
		String s  = getResponseAsString(link);
		return JsonUtils.toObject(s, typeReference);
	}
	
}
