/**
 * AgeHandler.java 1:42:16 PM Feb 15, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package rpc.handler;

import java.math.BigDecimal;

import po.Ad;

/**
 * 
 * 
 * @author dixingxing
 * @date Feb 15, 2012
 */
public class AgeHandler {
	public Ad getAge() {
		Ad ad = new Ad();
		ad.setId(new BigDecimal(Math.random() * 10).longValue());
		return ad;
	}
}
