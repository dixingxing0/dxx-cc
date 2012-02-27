package json;

import java.util.Date;

import org.junit.Test;

import data.B;

import po.Ad;

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
	@Test
	public void json() {
		Ad ad = new Ad();
		ad.setId(123L);
		ad.setName("name_123");
		ad.setPublishTime(new Date());
		ad.setPublishMan("publishMan_123");
		ad.setUpdateTime(new Date());

		B b = new B();
		b.setId(999L);
		b.setName("name");
		ad.setB(b);

		System.out.println(JsonUtils.toJson(ad));
		ad = JsonUtils.toObject(JsonUtils.toJson(ad), Ad.class);
		System.out.println(ad.getB().getName());
	}
}
