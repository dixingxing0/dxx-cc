/**
 * HttpUtilsTest.java 9:30:49 AM Mar 31, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.demo.util;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.cc.demo.common.util.HttpUtils;
import org.cc.demo.domain.Memo;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Assert;
import org.junit.Test;


/**
 * 
 * 
 * @author dixingxing	
 * @date Mar 31, 2012
 */
public class HttpUtilsTest {
	private static final Logger LOG = Logger.getLogger(HttpUtilsTest.class);

	@Test
	public void getResponseAsString() {
		String result = HttpUtils.getResponseAsString("http://www.baidu.com");
		Assert.assertTrue(StringUtils.isNotEmpty(result));
		LOG.debug(result);
	}
	
	@Test
	public void getObject() {
//		long start = System.currentTimeMillis();
//		List<Memo> memos = HttpUtils.getObject("http://localhost:8080/memo/json", new TypeReference<List<Memo>>(){});
//		for(Memo m :memos) {
//			LOG.debug(m);
//		}
//		LOG.debug("cost : " + (System.currentTimeMillis() - start));
	}
	
}
