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
 * 
 * @author dixingxing	
 * @date Apr 17, 2012
 */
public class Dates {
	private Dates() {}
	/**
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
	
	public static Date parse(String dateStr , String datePatten) {
		SimpleDateFormat sdf = new SimpleDateFormat(datePatten);
		try {
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}
	
}
