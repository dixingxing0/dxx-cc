/**
 * DaoTest.java 2:16:46 PM Feb 9, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package dao;

import junit.framework.TestCase;

import common.Page;

import data.B;

/**
 * 
 * 
 * @author dixingxing
 * @date Feb 9, 2012
 */
public class DaoTest extends TestCase {
	public void testQueryPage() {
		Page<B> page = new BDao().queryPage(
				"select * from Ad", 1, 10);

		System.out.println(page.getTotalResult());
		for (B b : page.getResult()) {
			System.out.println(b.getId() + " : " + b.getName());
		}
	}

}
