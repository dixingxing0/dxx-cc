/**
 * Strings.java 9:25:16 AM Apr 11, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.core.common;

import java.util.Collection;


/**
 * 
 * 
 * @author dixingxing	
 * @date Apr 11, 2012
 */
public class Strings {
	
	public static boolean isBlank(String s) {
		return s == null || s.trim().equals("");
	}
	
	public static boolean isNotBlank(String s) {
		return !isBlank(s);
	}

	public static String capitalize(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        return new StringBuilder(strLen)
            .append(Character.toTitleCase(str.charAt(0)))
            .append(str.substring(1))
            .toString();
    }
	
	public static String uncapitalize(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        return new StringBuilder(strLen)
            .append(Character.toLowerCase(str.charAt(0)))
            .append(str.substring(1))
            .toString();
    }
	
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
