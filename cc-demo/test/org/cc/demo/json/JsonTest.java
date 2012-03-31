package org.cc.demo.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.cc.demo.domain.Memo;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;



/**
 * JsonTest.java 10:49:45 AM Feb 15, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
/**
 * 
 * 
 * @author dixingxing
 * @date Feb 15, 2012
 */
public class JsonTest {
	private final static Logger LOG = Logger.getLogger(JsonTest.class);
	private List<Memo> memos;
	
	private Memo memo;
	
	@Before
	public void setUp() {
		JsonUtils.toJson("");
		JsonUtils.toObject("{}", Memo.class);
		
		memos = new ArrayList<Memo>();
		Memo m1 = new Memo();
		m1.setCreateTime("2011-11-11");
		m1.setId(1L);
		m1.setName("memo1");
		Memo m2 = new Memo();
		m2.setCreateTime("2011-11-12");
		m2.setId(2L);
		m2.setName("memo2");
		memos.add(m1);
		memos.add(m2);
		memo = m1;
	}
	
	@Test
	public void json() {
		memo = JsonUtils.toObject(JsonUtils.toJson(memo), Memo.class);
		Assert.assertEquals(memo.getName(), "memo1");
	}
	
	@Test
	public void toObject() {
		String json = JsonUtils.toJson(memos);
		LOG.debug(json);
		List<Memo> list = JsonUtils.toObject(json, new TypeReference<List<Memo>>(){});
		Assert.assertEquals(2, list.size());
		for(Memo m : list) {
			LOG.debug(m);
		}
	}
}
