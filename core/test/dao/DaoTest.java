/**
 * DaoTest.java 2:16:46 PM Feb 9, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package dao;

import common.Page;
import data.B;

import junit.framework.TestCase;

/**
 * 
 * 
 * @author dixingxing
 * @date Feb 9, 2012
 */
public class DaoTest extends TestCase {
	public void testQueryPage() {
		Page<B> page = Dao.queryPage("select * from ad", B.class, 1, 10);

		System.out.println(page.getTotalResult());
		for (B b : page.getResult()) {
			System.out.println(b.getId() + " : " + b.getName());
		}
	}
}
