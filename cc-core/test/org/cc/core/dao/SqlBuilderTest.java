/**
 * SqlBuilderTest.java 10:40:01 AM Feb 9, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package org.cc.core.dao;

import org.cc.core.data.B;
import org.junit.Test;

/**
 * 
 * @author dixingxing
 * @date Feb 9, 2012
 */
public class SqlBuilderTest {
	@Test
	public void testBuildInsert() {
		B b = new B();
		b.setId(123L);
		b.setName("name_b");
		SqlBuilder.buildInsert(b);
	}
	@Test
	public void testBuildUpdate() {
		B b = new B();
		b.setId(123L);
		b.setName("name_b");
		SqlBuilder.buildUpdate(b);
	}

}
