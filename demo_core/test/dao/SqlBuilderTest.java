/**
 * SqlBuilderTest.java 10:40:01 AM Feb 9, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package dao;

import data.B;
import junit.framework.TestCase;

/**
 * 
 * @author dixingxing
 * @date Feb 9, 2012
 */
public class SqlBuilderTest extends TestCase {
	public void testBuildInsert() {
		B b = new B();
		b.setId(123L);
		b.setName("name_b");
		SqlBuilder.buildInsert(b);
	}

	public void testBuildUpdate() {
		B b = new B();
		b.setId(123L);
		b.setName("name_b");
		SqlBuilder.buildUpdate(b);
	}

}
