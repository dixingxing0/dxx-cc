/**
 * RpcTest.java 3:43:53 PM Feb 15, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package rpc;

import org.junit.Test;

import po.Ad;

/**
 * 
 * 
 * @author dixingxing
 * @date Feb 15, 2012
 */
public class RpcTest {
	@Test
	public void rpc() throws Exception {
		Object[] params = new Object[] {};
		for (int i = 0; i < 100; i++) {
			Ad result = (Ad) Client.get().execute("AgeHandler.getAge", params);
			System.out.println("从xmlrpc返回的数据：" + result.getId());
			Thread.sleep(2000L);
		}
	}
}
