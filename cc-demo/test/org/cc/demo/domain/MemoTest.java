/**
 * MemoTest.java 3:25:12 PM Mar 30, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.demo.domain;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * 
 * 
 * @author dixingxing	
 * @date Mar 30, 2012
 */
public class MemoTest {
	private final static Logger LOG = Logger.getLogger(MemoTest.class);
	
	@Test
	public void queryList() {
		List<Memo> list = Memo.DB.queryList("select * from memo");
		for(Memo m : list) {
			LOG.debug(m);
		}
		
	}
	
	@Test
	public void insert() {
		Memo m  = new Memo();
		m.setName("memo1");
		m.setCreateTime("2012-03-30 16:10:00");
		long id = m.insert();
		LOG.debug("generate id :" + id);
	}
}
