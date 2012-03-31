/**
 * HttpUtils.java 9:25:12 AM Mar 31, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.demo.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.cc.core.common.CcException;

/**
 * 
 * 
 * @author dixingxing	
 * @date Mar 31, 2012
 */
public class HttpUtils {

	/**
	 * return null if error occured
	 * 
	 * @param link
	 * @return
	 */
	public static String getResponseAsString(String link){
		HttpURLConnection conn = null;
		URL url = null;
		String result = "";
		try {
			url = new java.net.URL(link);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(2000);
			conn.connect();

			InputStream urlStream = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(urlStream));
			String s = "";
			while ((s = reader.readLine()) != null) {
				result += s;
			}
			reader.close();
			urlStream.close();
			conn.disconnect();
			return result;
		} catch(Exception e){
			throw new CcException(e);
		}
	}
	
}
