/**
 * Strings.java 9:25:16 AM Apr 11, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.core.common;

import java.util.Collection;


/**
 * 
 * String 工具类
 * 
 * @author dixingxing	
 * @date Apr 11, 2012
 */
public class Strings {
	private Strings() {}
	
	/**
	 * 
	 * <p>return s == null || s.trim().equals("");</p>
	 *
	 * @param s
	 * @return
	 */
	public static boolean isBlank(String s) {
		return s == null || s.trim().equals("");
	}
	
	/***
	 * 
	 * <p>return !isBlank(s);</p>
	 *
	 * @param s
	 * @return
	 */
	public static boolean isNotBlank(String s) {
		return !isBlank(s);
	}

	/**
	 * 
	 * <p>首字母大写</p>
	 *
	 * @param str
	 * @return
	 */
	public static String capitalize(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        return new StringBuilder(str.length())
            .append(Character.toTitleCase(str.charAt(0)))
            .append(str.substring(1))
            .toString();
    }
	
	/**
	 * 首字母小写
	 * <p></p>
	 *
	 * @param str
	 * @return
	 */
	public static String uncapitalize(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        return new StringBuilder(str.length())
            .append(Character.toLowerCase(str.charAt(0)))
            .append(str.substring(1))
            .toString();
    }
	
	/**
	 * 
	 * <p>数据转字符串，用“,”分隔</p>
	 *
	 * @param ss
	 * @return
	 */
	public static String join(Object[] ss) {
		if(ss == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for(Object s : ss) {
			sb.append(s).append(",");
		}
		if(sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}
	
	/**
	 * 
	 * <p>collection转字符串，用“,”分隔</p>
	 *
	 * @param ss
	 * @return
	 */
	public static String join(Collection<?> c) {
		if(c == null || c.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for(Object s : c) {
			sb.append(s).append(",");
		}
		if(sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}
}
