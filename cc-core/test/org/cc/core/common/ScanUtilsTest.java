/**
 * ScanUtilsTest.java 5:56:28 PM Mar 30, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.core.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * 
 * 
 * @author dixingxing	
 * @date Mar 30, 2012
 */
public class ScanUtilsTest {
	private final static Logger logger = Logger.getLogger(ScanUtilsTest.class);
	@Test
	public void test() {
		// 自定义过滤规则
		List<String> classFilters = new ArrayList<String>();
		// classFilters.add("File*");

		// 创建一个扫描处理器，排除内部类 扫描符合条件的类
		ScanUtils handler = new ScanUtils(true, true, classFilters);

		Set<Class<?>> calssList = handler.getPackageAllClasses("web", true);
		for (Class<?> cla : calssList) {
			logger.debug(cla.getName());
		}
	}
}
