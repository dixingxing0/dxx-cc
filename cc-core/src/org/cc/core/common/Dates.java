/**
 * Dates.java 3:38:07 PM Apr 17, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.core.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * <p>Date工具类</p>
 * 
 * @author dixingxing	
 * @date Apr 20, 2012
 */
public class Dates {
	private Dates() {}
	
	/**
	 * 
	 * <p>字符串转date对象，多个pattern</p>
	 *
	 * @param dateStr
	 * @param datePattens
	 * @return
	 */
	public static Date parse(String dateStr, String[] datePattens) {
		for(String p : datePattens) {
			Date d = parse(dateStr, p);
			if(d != null) {
				return d;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * <p>字符串转date对象</p>
	 *
	 * @param dateStr
	 * @param datePatten
	 * @return
	 */
	public static Date parse(String dateStr , String datePatten) {
		SimpleDateFormat sdf = new SimpleDateFormat(datePatten);
		try {
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}
	
}
